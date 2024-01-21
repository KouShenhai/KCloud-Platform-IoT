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
import org.laokou.flowable.command.definition.DefinitionDeleteCmdExe;
import org.laokou.flowable.command.definition.DefinitionInsertCmdExe;
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

	private final DefinitionInsertCmdExe definitionInsertCmdExe;

	private final DefinitionListQryExe definitionListQryExe;

	private final DefinitionActivateCmdExe definitionActivateCmdExe;

	private final DefinitionSuspendCmdExe definitionSuspendCmdExe;

	private final DefinitionDeleteCmdExe definitionDeleteCmdExe;

	private final DefinitionDiagramGetQryExe definitionDiagramGetQryExe;

	/**
	 * 新增流程.
	 * @param cmd 新增流程参数
	 * @return 新增结果
	 */
	@Override
	public Result<Boolean> insert(DefinitionInsertCmd cmd) {
		return definitionInsertCmdExe.execute(cmd);
	}

	/**
	 * 查询流程列表.
	 * @param qry 查询流程列表参数
	 * @return 流程列表
	 */
	@Override
	public Result<Datas<DefinitionCO>> list(DefinitionListQry qry) {
		return definitionListQryExe.execute(qry);
	}

	/**
	 * 查看流程图.
	 * @param qry 查看流程图参数
	 * @return 流程图
	 */
	@Override
	public Result<String> diagram(DefinitionDiagramGetQry qry) {
		return definitionDiagramGetQryExe.execute(qry);
	}

	/**
	 * 删除流程.
	 * @param cmd 删除流程参数
	 * @return 删除结果
	 */
	@Override
	public Result<Boolean> delete(DefinitionDeleteCmd cmd) {
		return definitionDeleteCmdExe.execute(cmd);
	}

	/**
	 * 挂起流程.
	 * @param cmd 挂起流程参数
	 * @return 挂起结果
	 */
	@Override
	public Result<Boolean> suspend(DefinitionSuspendCmd cmd) {
		return definitionSuspendCmdExe.execute(cmd);
	}

	/**
	 * 激活流程.
	 * @param cmd 激活流程参数
	 * @return 激活结果
	 */
	@Override
	public Result<Boolean> activate(DefinitionActivateCmd cmd) {
		return definitionActivateCmdExe.execute(cmd);
	}

}
