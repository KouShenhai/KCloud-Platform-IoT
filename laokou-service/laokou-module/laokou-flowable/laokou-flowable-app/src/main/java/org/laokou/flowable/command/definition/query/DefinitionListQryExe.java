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

package org.laokou.flowable.command.definition.query;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.flowable.dto.definition.DefinitionListQry;
import org.laokou.flowable.dto.definition.clientobject.DefinitionCO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DefinitionListQryExe {

	private final RepositoryService repositoryService;

	public Result<Datas<DefinitionCO>> execute(DefinitionListQry qry) {
		String name = qry.getName();
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
			.latestVersion()
			.orderByProcessDefinitionKey()
			.asc();
		if (StringUtil.isNotEmpty(name)) {
			query.processDefinitionNameLike(StringUtil.like(name));
		}
		long total = query.count();
		int pageNum = qry.getPageNum();
		int pageSize = qry.getPageSize();
		int pageIndex = pageSize * (pageNum - 1);
		List<ProcessDefinition> definitionList = query.listPage(pageIndex, pageSize);
		List<DefinitionCO> list = new ArrayList<>(definitionList.size());
		for (ProcessDefinition definition : definitionList) {
			list.add(toDefinitionCO(definition));
		}
		return Result.of(new Datas<>(total, list));
	}

	private DefinitionCO toDefinitionCO(ProcessDefinition definition) {
		DefinitionCO co = new DefinitionCO();
		co.setDefinitionId(definition.getId());
		co.setProcessKey(definition.getKey());
		co.setProcessName(definition.getName());
		co.setDeploymentId(definition.getDeploymentId());
		co.setIsSuspended(definition.isSuspended());
		return co;
	}

}
