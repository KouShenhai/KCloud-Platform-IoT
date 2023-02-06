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
import org.laokou.tenant.dto.SysTenantSourceDTO;
import org.laokou.admin.server.application.service.SysTenantSourceApplicationService;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.common.swagger.utils.ValidatorUtil;
import org.laokou.tenant.entity.SysTenantSourceDO;
import org.laokou.tenant.qo.SysTenantSourceQo;
import org.laokou.tenant.service.SysTenantSourceService;
import org.laokou.tenant.vo.SysTenantSourceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysTenantSourceApplicationServiceImpl implements SysTenantSourceApplicationService {

    private final SysTenantSourceService sysTenantSourceService;
    @Override
    public IPage<SysTenantSourceVO> queryTenantSourcePage(SysTenantSourceQo qo) {
        ValidatorUtil.validateEntity(qo);
        IPage<SysTenantSourceVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysTenantSourceService.queryTenantSourcePage(page,qo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertTenantSource(SysTenantSourceDTO dto) {
        ValidatorUtil.validateEntity(dto);
        long count = sysTenantSourceService.count(Wrappers.lambdaQuery(SysTenantSourceDO.class).eq(SysTenantSourceDO::getName, dto.getName()));
        if (count > 0) {
            throw new CustomException("数据源名称已存在，请重新填写");
        }
        SysTenantSourceDO tenantSourceDO = ConvertUtil.sourceToTarget(dto, SysTenantSourceDO.class);
        tenantSourceDO.setCreator(UserUtil.getUserId());
        return sysTenantSourceService.save(tenantSourceDO);
    }

    @Override
    public Boolean updateTenantSource(SysTenantSourceDTO dto) {
        ValidatorUtil.validateEntity(dto);
        Long id = dto.getId();
        if (id == null) {
            throw new CustomException("数据源编号不为空");
        }
        long count = sysTenantSourceService.count(Wrappers.lambdaQuery(SysTenantSourceDO.class).eq(SysTenantSourceDO::getName, dto.getName()).ne(SysTenantSourceDO::getId,dto.getId()));
        if (count > 0) {
            throw new CustomException("数据源名称已存在，请重新填写");
        }
        Integer version = sysTenantSourceService.getVersion(id);
        SysTenantSourceDO sourceDO = ConvertUtil.sourceToTarget(dto, SysTenantSourceDO.class);
        sourceDO.setVersion(version);
        sourceDO.setEditor(UserUtil.getUserId());
        return sysTenantSourceService.updateById(sourceDO);
    }

    @Override
    public Boolean deleteTenantSource(Long id) {
        sysTenantSourceService.deleteTenantSource(id);
        return true;
    }

}
