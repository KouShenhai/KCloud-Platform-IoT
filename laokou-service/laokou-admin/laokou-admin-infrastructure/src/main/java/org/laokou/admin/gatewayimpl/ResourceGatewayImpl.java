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
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.ResourceConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.ResourceGateway;
import org.laokou.admin.domain.resource.Resource;
import org.laokou.admin.domain.resource.Status;
import org.laokou.admin.dto.resource.TaskStartCmd;
import org.laokou.admin.dto.resource.clientobject.StartCO;
import org.laokou.admin.gatewayimpl.database.ResourceAuditMapper;
import org.laokou.admin.gatewayimpl.database.ResourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceAuditDO;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceDO;
import org.laokou.admin.gatewayimpl.feign.TasksFeignClient;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.laokou.admin.common.Constant.KEY;
import static org.laokou.common.mybatisplus.template.DsConstant.BOOT_SYS_RESOURCE;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceGatewayImpl implements ResourceGateway {

    private final ResourceMapper resourceMapper;
    private final ResourceAuditMapper resourceAuditMapper;
    private final TasksFeignClient tasksFeignClient;

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

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public Boolean update(Resource resource) {
        return updateResource(resource,resourceMapper.getVersion(resource.getId(),ResourceDO.class));
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateResource(Resource resource,Integer version) {
        log.info("分布式事务，XID:{}", RootContext.getXID());
        Boolean flag = insertResourceAudit(resource);
        StartCO co = startTask(resource);
        int status = Status.PENDING_APPROVAL;
        return flag && updateResourceStatus(resource, status,version, co.getInstanceId());
    }

    private StartCO startTask(Resource resource) {
        TaskStartCmd cmd = new TaskStartCmd();
        cmd.setBusinessKey(resource.getId().toString());
        cmd.setDefinitionKey(KEY);
        cmd.setInstanceName(resource.getTitle());
        Result<StartCO> result = tasksFeignClient.start(cmd);
        if (result.fail()) {
            throw new GlobalException(result.getMsg());
        }
        return result.getData();
    }

    private Boolean updateResource(ResourceDO resourceDO) {
        return resourceMapper.updateById(resourceDO) > 0;
    }

    private Boolean updateResourceStatus(Resource resource,int status,Integer version,String instanceId) {
        ResourceDO resourceDO = new ResourceDO();
        resourceDO.setId(resource.getId());
        resourceDO.setStatus(status);
        resourceDO.setInstanceId(instanceId);
        resourceDO.setVersion(version);
        return updateResource(resourceDO);
    }

    private Boolean insertResourceAudit(Resource resource) {
        ResourceAuditDO resourceAuditDO = ConvertUtil.sourceToTarget(resource, ResourceAuditDO.class);
        resourceAuditDO.setResourceId(resource.getId());
        return resourceAuditMapper.insertTable(resourceAuditDO);
    }

}
