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

package org.laokou.flowable.command.definition.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.security.utils.UserUtil;
import org.laokou.flowable.dto.definition.DefinitionListQry;
import org.laokou.flowable.dto.definition.clientobject.DefinitionCO;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.i18n.common.DatasourceConstant.FLOWABLE;
import static org.laokou.common.i18n.common.StringConstant.PERCENT;

/**
 * 查询流程列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DefinitionListQryExe {

	private final RepositoryService repositoryService;

	/**
	 * 执行查询流程列表.
	 * @param qry 查询流程列表参数
	 * @return 流程列表
	 */
	@DS(FLOWABLE)
	public Result<Datas<DefinitionCO>> execute(DefinitionListQry qry) {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
			.processDefinitionTenantId(UserUtil.getTenantId().toString())
			.latestVersion()
			.orderByProcessDefinitionKey()
			.asc();
		String name = qry.getName();
		if (StringUtil.isNotEmpty(name)) {
			query.processDefinitionNameLike(PERCENT.concat(name.concat(PERCENT)));
		}
		int pageNum = qry.getPageNum();
		int pageSize = qry.getPageSize();
		List<ProcessDefinition> definitionList = query.listPage(pageSize * (pageNum - 1), pageSize);
		return Result.ok(Datas.of(definitionList.stream().map(this::convert).toList(), query.count()));
	}

	/**
	 * 转换为定义流程命令请求.
	 * @param definition 流程对象
	 * @return 定义流程命令请求
	 */
	private DefinitionCO convert(ProcessDefinition definition) {
		return DefinitionCO.builder()
			.definitionId(definition.getId())
			.processKey(definition.getKey())
			.processName(definition.getName())
			.deploymentId(definition.getDeploymentId())
			.isSuspended(definition.isSuspended())
			.build();
	}

}
