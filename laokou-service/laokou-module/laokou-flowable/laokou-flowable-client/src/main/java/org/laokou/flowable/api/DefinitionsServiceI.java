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

package org.laokou.flowable.api;

import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.dto.definition.*;
import org.laokou.flowable.dto.definition.clientobject.DefinitionCO;

/**
 * 定义流程.
 *
 * @author laokou
 */
public interface DefinitionsServiceI {

	/**
	 * 新增流程.
	 * @param cmd 新增流程参数
	 */
	void create(DefinitionCreateCmd cmd);

	/**
	 * 查询流程列表.
	 * @param qry 查询流程列表参数
	 * @return 流程列表
	 */
	Result<Datas<DefinitionCO>> findList(DefinitionListQry qry);

	/**
	 * 查看流程图.
	 * @param qry 查看流程图参数
	 * @return 流程图
	 */
	Result<String> findDiagram(DefinitionDiagramGetQry qry);

	/**
	 * 删除流程.
	 * @param cmd 删除流程参数
	 */
	void remove(DefinitionRemoveCmd cmd);

	/**
	 * 挂起流程.
	 * @param cmd 挂起流程参数
	 */
	void suspend(DefinitionSuspendCmd cmd);

	/**
	 * 激活流程.
	 * @param cmd 激活流程参数
	 */
	void activate(DefinitionActivateCmd cmd);

}
