/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.admin.gatewayimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.ResourceConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.ResourceGateway;
import org.laokou.admin.domain.resource.Resource;
import org.laokou.admin.gatewayimpl.database.ResourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.springframework.stereotype.Component;

import static org.laokou.common.mybatisplus.template.DsConstant.BOOT_SYS_RESOURCE;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ResourceGatewayImpl implements ResourceGateway {

    private final ResourceMapper resourceMapper;

    @Override
    @DataFilter(alias = BOOT_SYS_RESOURCE)
    public Datas<Resource> list(Resource resource, PageQuery pageQuery) {
        IPage<ResourceDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        ResourceDO resourceDO = ResourceConvertor.toDataObject(resource);
        IPage<ResourceDO> newPage = resourceMapper.getResourceListFilter(page, resourceDO, pageQuery);
        Datas<Resource> datas = new Datas<>();
        datas.setTotal(newPage.getTotal());
        datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(),Resource.class));
        return datas;
    }

    @Override
    public Resource getById(Long id) {
        ResourceDO resourceDO = resourceMapper.selectById(id);
        return ConvertUtil.sourceToTarget(resourceDO, Resource.class);
    }

}
