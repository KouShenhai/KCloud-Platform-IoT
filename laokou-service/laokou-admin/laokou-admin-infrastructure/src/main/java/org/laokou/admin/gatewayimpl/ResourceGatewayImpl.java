/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.common.event.DomainEventPublisher;
import org.laokou.admin.common.utils.EventUtil;
import org.laokou.admin.config.DefaultConfigProperties;
import org.laokou.admin.convertor.ResourceConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.ResourceGateway;
import org.laokou.admin.domain.resource.Resource;
import org.laokou.admin.domain.resource.Status;
import org.laokou.admin.dto.resource.TaskStartCmd;
import org.laokou.admin.dto.resource.clientobject.StartCO;
import org.laokou.admin.gatewayimpl.database.ResourceAuditMapper;
import org.laokou.admin.gatewayimpl.database.ResourceMapper;
import org.laokou.admin.gatewayimpl.database.dataindex.ResourceIndex;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceAuditDO;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceDO;
import org.laokou.admin.gatewayimpl.feign.TasksFeignClient;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.openfeign.utils.FeignUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.Constant.*;
import static org.laokou.common.mybatisplus.constant.DsConstant.BOOT_SYS_RESOURCE;

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

	private final DomainEventPublisher domainEventPublisher;

	private final ResourceConvertor resourceConvertor;

	private final ElasticsearchTemplate elasticsearchTemplate;

	private final DefaultConfigProperties defaultConfigProperties;

	private final TransactionalUtil transactionalUtil;

	private final EventUtil eventUtil;

	@Override
	@DataFilter(alias = BOOT_SYS_RESOURCE)
	public Datas<Resource> list(Resource resource, PageQuery pageQuery) {
		IPage<ResourceDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		ResourceDO resourceDO = resourceConvertor.toDataObject(resource);
		IPage<ResourceDO> newPage = resourceMapper.getResourceListFilter(page, resourceDO, pageQuery);
		Datas<Resource> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(resourceConvertor.convertEntityList(newPage.getRecords()));
		return datas;
	}

	@Override
	public Resource getById(Long id) {
		return resourceConvertor.convertEntity(resourceMapper.selectById(id));
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	public Boolean insert(Resource resource) {
		return insertResource(resource);
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	public Boolean update(Resource resource) {
		return updateResource(resource, resourceMapper.getVersion(resource.getId(), ResourceDO.class));
	}

	@Override
	@SneakyThrows
	public Boolean sync() {
		List<String> resourceTime = resourceMapper.getResourceTime();
		if (CollectionUtil.isEmpty(resourceTime)) {
			throw new SystemException("同步失败，数据不能为空");
		}
		// 同步前
		syncBefore();
		// 删除索引
		deleteIndex(resourceTime);
		// 创建索引
		createIndex(resourceTime);
		// 同步索引
		syncIndex();
		// 同步后
		syncAfter();
		return true;
	}

	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.defaultExecute(rollback -> {
			try {
				return resourceMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.error(e.getMessage()), e);
				rollback.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private Boolean insertResource(Resource resource) {
		return updateResource(insertTable(resource), DEFAULT);
	}

	private Resource insertTable(Resource resource) {
		ResourceDO resourceDO = resourceConvertor.toDataObject(resource);
		resourceMapper.insertTable(resourceDO);
		resource.setId(resourceDO.getId());
		return resource;
	}

	private Boolean updateResource(Resource resource, Integer version) {
		log.info("开始任务分布式事务 XID：{}", RootContext.getXID());
		insertResourceAudit(resource);
		StartCO co = startTask(resource);
		int status = Status.PENDING_APPROVAL;
		updateResourceStatus(resource, status, version, co.getInstanceId());
		publishMessage(resource, co.getInstanceId());
		return true;
	}

	@Async
	public void publishMessage(Resource resource, String instanceId) {
		domainEventPublisher
			.publish(eventUtil.toAuditMessageEvent(null, resource.getId(), resource.getTitle(), instanceId));
	}

	private StartCO startTask(Resource resource) {
		TaskStartCmd cmd = new TaskStartCmd();
		cmd.setBusinessKey(resource.getId().toString());
		cmd.setDefinitionKey(defaultConfigProperties.getDefinitionKey());
		cmd.setInstanceName(resource.getTitle());
		return FeignUtil.result(tasksFeignClient.start(cmd));
	}

	private void updateResourceStatus(Resource resource, int status, Integer version, String instanceId) {
		ResourceDO resourceDO = new ResourceDO();
		resourceDO.setId(resource.getId());
		resourceDO.setStatus(status);
		resourceDO.setInstanceId(instanceId);
		resourceDO.setVersion(version);
		resourceMapper.updateById(resourceDO);
	}

	private void insertResourceAudit(Resource resource) {
		ResourceAuditDO resourceAuditDO = ConvertUtil.sourceToTarget(resource, ResourceAuditDO.class);
		Assert.isTrue(ObjectUtil.isNotNull(resourceAuditDO), "resource audit is null");
		resourceAuditDO.setResourceId(resource.getId());
		resourceAuditMapper.insertTable(resourceAuditDO);
	}

	private void createIndex(List<String> list) {
		list.forEach(ym -> {
			try {
				elasticsearchTemplate.createIndex(index(ym), RESOURCE_INDEX, ResourceIndex.class);
			}
			catch (Exception e) {
				log.error("索引创建失败，错误信息：{}，详情见日志", LogUtil.error(e.getMessage()), e);
				throw new SystemException("索引创建失败");
			}
		});
	}

	private void deleteIndex(List<String> list) {
		list.forEach(ym -> {
			try {
				elasticsearchTemplate.deleteIndex(index(ym));
			}
			catch (Exception e) {
				log.error("索引删除失败，错误信息：{}，详情见日志", LogUtil.error(e.getMessage()), e);
				throw new SystemException("索引删除失败");
			}
		});
	}

	private void syncIndex() {
		int chunkSize = 5000;
		List<ResourceIndex> list = Collections.synchronizedList(new ArrayList<>(chunkSize));
		resourceMapper.handleResourceIndex(result -> {
			ResourceIndex index = result.getResultObject();
			list.add(index);
			if (list.size() % chunkSize == 0) {
				syncIndex(list);
			}
		});
		if (list.size() % chunkSize != 0) {
			syncIndex(list);
		}
	}

	private void syncIndex(List<ResourceIndex> list) {
		Map<String, List<ResourceIndex>> listMap = list.stream().collect(Collectors.groupingBy(ResourceIndex::getYm));
		listMap.forEach((k, v) -> {
			try {
				elasticsearchTemplate.syncBatchIndex(index(k), JacksonUtil.toJsonStr(v));
			}
			catch (Exception e) {
				log.error("索引同步失败，错误信息：{}，详情见日志", LogUtil.error(e.getMessage()), e);
				throw new SystemException("索引同步失败");
			}
		});
		// 清除list
		list.clear();
	}

	private String index(String ym) {
		return RESOURCE_INDEX + UNDER + ym;
	}

	private void syncBefore() {
		log.info("开始同步数据");
	}

	private void syncAfter() {
		log.info("结束同步数据");
	}

}
