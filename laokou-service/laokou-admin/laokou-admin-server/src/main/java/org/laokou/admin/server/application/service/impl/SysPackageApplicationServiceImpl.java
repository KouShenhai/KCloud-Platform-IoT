/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.SysPackageApplicationService;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.core.exception.CustomException;
import org.laokou.common.core.utils.ValidatorUtil;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.tenant.dto.SysPackageDTO;
import org.laokou.tenant.entity.SysPackageDO;
import org.laokou.tenant.entity.SysPackageMenuDO;
import org.laokou.tenant.qo.SysPackageQo;
import org.laokou.tenant.service.SysPackageMenuService;
import org.laokou.tenant.service.SysPackageService;
import org.laokou.tenant.vo.SysPackageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysPackageApplicationServiceImpl implements SysPackageApplicationService {

    private final SysPackageService sysPackageService;
    private final SysPackageMenuService sysPackageMenuService;
    private final BatchUtil<SysPackageMenuDO> batchUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertPackage(SysPackageDTO dto) {
        ValidatorUtil.validateEntity(dto);
        long count = sysPackageService.count(Wrappers.lambdaQuery(SysPackageDO.class).eq(SysPackageDO::getName, dto.getName()));
        if (count > 0) {
            throw new CustomException("套餐名称已存在，请重新填写");
        }
        SysPackageDO sysPackageDO = ConvertUtil.sourceToTarget(dto, SysPackageDO.class);
        sysPackageDO.setCreator(UserUtil.getUserId());
        sysPackageService.save(sysPackageDO);
        return saveOrUpdate(dto.getMenuIds(),sysPackageDO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePackage(SysPackageDTO dto) {
        ValidatorUtil.validateEntity(dto);
        Long id = dto.getId();
        if (id == null) {
            throw new CustomException("套餐编号不为空");
        }
        long count = sysPackageService.count(Wrappers.lambdaQuery(SysPackageDO.class).eq(SysPackageDO::getName, dto.getName()).ne(SysPackageDO::getId,id));
        if (count > 0) {
            throw new CustomException("套餐名称已存在，请重新填写");
        }
        Integer version = sysPackageService.getVersion(id);
        SysPackageDO sysPackageDO = ConvertUtil.sourceToTarget(dto, SysPackageDO.class);
        sysPackageDO.setVersion(version);
        sysPackageDO.setEditor(UserUtil.getUserId());
        sysPackageService.updateById(sysPackageDO);
        sysPackageMenuService.remove(Wrappers.lambdaQuery(SysPackageMenuDO.class).eq(SysPackageMenuDO::getPackageId,id));
        return saveOrUpdate(dto.getMenuIds(),id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePackage(Long id) {
        return sysPackageService.deletePackage(id);
    }

    @Override
    public IPage<SysPackageVO> queryPackagePage(SysPackageQo qo) {
        ValidatorUtil.validateEntity(qo);
        IPage<SysPackageVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysPackageService.queryPackagePage(page,qo);
    }

    @Override
    public SysPackageVO getPackageById(Long id) {
        return sysPackageService.getPackageById(id);
    }

    @Override
    public List<OptionVO> getOptionList() {
        return sysPackageService.getOptionList();
    }

    private boolean saveOrUpdate(List<Long> menuIds,Long id) {
        if (CollectionUtils.isNotEmpty(menuIds)) {
            List<SysPackageMenuDO> list = new ArrayList<>(menuIds.size());
            for (Long menuId : menuIds) {
                SysPackageMenuDO sysPackageMenuDO = new SysPackageMenuDO();
                sysPackageMenuDO.setPackageId(id);
                sysPackageMenuDO.setMenuId(menuId);
                list.add(sysPackageMenuDO);
            }
            batchUtil.insertBatch(list,500,sysPackageMenuService);
        }
        return true;
    }

}