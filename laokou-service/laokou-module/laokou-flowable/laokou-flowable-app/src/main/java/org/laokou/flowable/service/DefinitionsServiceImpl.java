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

package org.laokou.flowable.service;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.api.DefinitionsServiceI;
import org.laokou.flowable.command.definition.DefinitionActivateCmdExe;
import org.laokou.flowable.command.definition.DefinitionRemoveCmdExe;
import org.laokou.flowable.command.definition.DefinitionCreateCmdExe;
import org.laokou.flowable.command.definition.DefinitionSuspendCmdExe;
import org.laokou.flowable.command.definition.query.DefinitionDiagramGetQryExe;
import org.laokou.flowable.command.definition.query.DefinitionListQryExe;
import org.laokou.flowable.dto.definition.*;
import org.laokou.flowable.dto.definition.clientobject.DefinitionCO;
import org.springframework.stereotype.Service;

/**
 * 定义流程.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DefinitionsServiceImpl implements DefinitionsServiceI {

	private final DefinitionCreateCmdExe definitionCreateCmdExe;

	private final DefinitionListQryExe definitionListQryExe;

	private final DefinitionActivateCmdExe definitionActivateCmdExe;

	private final DefinitionSuspendCmdExe definitionSuspendCmdExe;

	private final DefinitionRemoveCmdExe definitionRemoveCmdExe;

	private final DefinitionDiagramGetQryExe definitionDiagramGetQryExe;

	/**
	 * 新增流程.
	 * @param cmd 新增流程参数
	 */
	@Override
	public void create(DefinitionCreateCmd cmd) {
		definitionCreateCmdExe.executeVoid(cmd);
	}

	/**
	 * 查询流程列表.
	 * @param qry 查询流程列表参数
	 * @return 流程列表
	 */
	@Override
	public Result<Datas<DefinitionCO>> findList(DefinitionListQry qry) {
		return definitionListQryExe.execute(qry);
	}

	/**
	 * 查看流程图.
	 * @param qry 查看流程图参数
	 * @return 流程图
	 */
	@Override
	public Result<String> findDiagram(DefinitionDiagramGetQry qry) {
		return definitionDiagramGetQryExe.execute(qry);
	}

	/**
	 * 删除流程.
	 * @param cmd 删除流程参数
	 */
	@Override
	public void remove(DefinitionRemoveCmd cmd) {
		definitionRemoveCmdExe.executeVoid(cmd);
	}

	/**
	 * 挂起流程.
	 * @param cmd 挂起流程参数
	 */
	@Override
	public void suspend(DefinitionSuspendCmd cmd) {
		definitionSuspendCmdExe.executeVoid(cmd);
	}

	/**
	 * 激活流程.
	 * @param cmd 激活流程参数
	 */
	@Override
	public void activate(DefinitionActivateCmd cmd) {
		definitionActivateCmdExe.executeVoid(cmd);
	}

}
