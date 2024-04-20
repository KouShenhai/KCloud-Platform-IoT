/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.resource.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.dto.resource.ResourceListQry;
import org.laokou.admin.dto.resource.clientobject.ResourceCO;
import org.laokou.admin.gatewayimpl.database.ResourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.DatasourceConstant.BOOT_SYS_RESOURCE;
import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查询资源列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ResourceListQryExe {

	private final ResourceMapper resourceMapper;

	private final Executor executor;

	/**
	 * 执行查询资源列表.
	 * @param qry 查询资源列表参数
	 * @return 资源列表
	 */
	@SneakyThrows
	@DS(TENANT)
	@DataFilter(tableAlias = BOOT_SYS_RESOURCE)
	public Result<Datas<ResourceCO>> execute(ResourceListQry qry) {
		ResourceDO resourceDO = convert(qry);
		PageQuery page = qry.page();
		CompletableFuture<List<ResourceDO>> c1 = CompletableFuture
			.supplyAsync(() -> resourceMapper.selectListByCondition(resourceDO, page), executor);
		CompletableFuture<Long> c2 = CompletableFuture
			.supplyAsync(() -> resourceMapper.selectCountByCondition(resourceDO, page), executor);
		CompletableFuture.allOf(List.of(c1, c2).toArray(CompletableFuture[]::new)).join();
		return Result.of(Datas.of(c1.get().stream().map(this::convert).toList(), c2.get()));
	}

	private ResourceDO convert(ResourceListQry qry) {
		ResourceDO resourceDO = new ResourceDO();
		resourceDO.setId(qry.getId());
		resourceDO.setCode(qry.getCode());
		resourceDO.setTitle(qry.getTitle());
		resourceDO.setStatus(qry.getStatus());
		return resourceDO;
	}

	private ResourceCO convert(ResourceDO resourceDO) {
		return ResourceCO.builder()
			.id(resourceDO.getId())
			.title(resourceDO.getTitle())
			.remark(resourceDO.getRemark())
			.instanceId(resourceDO.getInstanceId())
			.code(resourceDO.getCode())
			.status(resourceDO.getStatus())
			.build();
	}

}
