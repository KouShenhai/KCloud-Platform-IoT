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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.tenant.dto.SysSourceDTO;
import org.laokou.admin.server.application.service.SysSourceApplicationService;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.tenant.entity.SysSourceDO;
import org.laokou.tenant.qo.SysSourceQo;
import org.laokou.tenant.service.SysSourceService;
import org.laokou.tenant.vo.SysSourceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysSourceApplicationServiceImpl implements SysSourceApplicationService {

    private final SysSourceService sysSourceService;
    @Override
    public IPage<SysSourceVO> querySourcePage(SysSourceQo qo) {
        ValidatorUtil.validateEntity(qo);
        IPage<SysSourceVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysSourceService.querySourcePage(page,qo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertSource(SysSourceDTO dto) {
        ValidatorUtil.validateEntity(dto);
        long count = sysSourceService.count(Wrappers.lambdaQuery(SysSourceDO.class).eq(SysSourceDO::getName, dto.getName()));
        if (count > 0) {
            throw new CustomException("数据源名称已存在，请重新填写");
        }
        boolean sourceRegex = RegexUtil.sourceRegex(dto.getName());
        if (!sourceRegex) {
            throw new CustomException("数据源名称必须包含字母、下划线和数字，例如：tenant_000001");
        }
        SysSourceDO tenantSourceDO = ConvertUtil.sourceToTarget(dto, SysSourceDO.class);
        tenantSourceDO.setCreator(UserUtil.getUserId());
        return sysSourceService.save(tenantSourceDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateSource(SysSourceDTO dto) {
        ValidatorUtil.validateEntity(dto);
        Long id = dto.getId();
        if (id == null) {
            throw new CustomException("数据源编号不为空");
        }
        boolean sourceRegex = RegexUtil.sourceRegex(dto.getName());
        if (!sourceRegex) {
            throw new CustomException("数据源名称必须包含字母、下划线和数字，例如：tenant_000001");
        }
        long count = sysSourceService.count(Wrappers.lambdaQuery(SysSourceDO.class).eq(SysSourceDO::getName, dto.getName()).ne(SysSourceDO::getId,dto.getId()));
        if (count > 0) {
            throw new CustomException("数据源名称已存在，请重新填写");
        }
        Integer version = sysSourceService.getVersion(id);
        SysSourceDO sourceDO = ConvertUtil.sourceToTarget(dto, SysSourceDO.class);
        sourceDO.setVersion(version);
        sourceDO.setEditor(UserUtil.getUserId());
        return sysSourceService.updateById(sourceDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteSource(Long id) {
        sysSourceService.deleteSource(id);
        return true;
    }

    @Override
    public SysSourceVO getSourceById(Long id) {
        return sysSourceService.getSourceById(id);
    }

    @Override
    public List<OptionVO> getOptionList() {
        return sysSourceService.getOptionList();
    }

}
