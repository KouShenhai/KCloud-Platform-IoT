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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.DefinitionsServiceI;
import org.laokou.admin.dto.definition.*;
import org.laokou.admin.dto.definition.clientobject.DefinitionCO;
import org.laokou.admin.command.definition.*;
import org.laokou.admin.command.definition.query.DefinitionDiagramGetQryExe;
import org.laokou.admin.command.definition.query.DefinitionListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 定义流程.
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DefinitionsServiceImpl implements DefinitionsServiceI {

	private final DefinitionInsertCmdExe definitionInsertCmdExe;

	private final DefinitionListQryExe definitionListQryExe;

	private final DefinitionDiagramGetQryExe definitionDiagramGetQryExe;

	private final DefinitionDeleteCmdExe definitionDeleteCmdExe;

	private final DefinitionSuspendCmdExe definitionSuspendCmdExe;

	private final DefinitionActiveCmdExe definitionActiveCmdExe;

	private final DefinitionTemplateCmdExe definitionTemplateCmdExe;

	/**
	 * 新增流程.
	 * @param cmd 新增流程参数
	 * @return 执行新增结果
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
	 * @param qry 流程图查看参数
	 * @return 流程图
	 */
	@Override
	public Result<String> diagram(DefinitionDiagramGetQry qry) {
		return definitionDiagramGetQryExe.execute(qry);
	}

	/**
	 * 删除流程.
	 * @param cmd 删除流程参数
	 * @return 执行删除结果
	 */
	@Override
	public Result<Boolean> delete(DefinitionDeleteCmd cmd) {
		return definitionDeleteCmdExe.execute(cmd);
	}

	/**
	 * 挂起流程.
	 * @param cmd 挂起流程参数
	 * @return 执行挂起结果
	 */
	@Override
	public Result<Boolean> suspend(DefinitionSuspendCmd cmd) {
		return definitionSuspendCmdExe.execute(cmd);
	}

	/**
	 * 激活流程.
	 * @param cmd 激活流程参数
	 * @return 执行激活结果
	 */
	@Override
	public Result<Boolean> activate(DefinitionActiveCmd cmd) {
		return definitionActiveCmdExe.execute(cmd);
	}

	/**
	 * 下载流程模板.
	 * @param qry 下载流程模板参数
	 */
	@Override
	public void template(DefinitionTemplateCmd qry) {
		definitionTemplateCmdExe.executeVoid(qry);
	}

}
