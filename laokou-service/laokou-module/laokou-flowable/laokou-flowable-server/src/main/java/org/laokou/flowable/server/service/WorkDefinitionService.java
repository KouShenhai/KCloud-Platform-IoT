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
package org.laokou.flowable.server.service;

import org.laokou.flowable.dto.DefinitionDTO;
import org.laokou.flowable.vo.DefinitionVO;
import org.laokou.flowable.vo.PageVO;

import java.io.InputStream;

/**
 * @author laokou
 */
public interface WorkDefinitionService {

	/**
	 * 新增流程文件
	 * @param in
	 * @return
	 */
	Boolean insertDefinition(InputStream in);

	/**
	 * 分页查询流程
	 * @param dto
	 * @return
	 */
	PageVO<DefinitionVO> queryDefinitionPage(DefinitionDTO dto);

	/**
	 * 查看流程图
	 * @param definitionId
	 * @return
	 */
	String diagramDefinition(String definitionId);

	/**
	 * 删除流程
	 * @param deploymentId
	 * @return
	 */
	Boolean deleteDefinition(String deploymentId);

	/**
	 * 挂起流程
	 * @param definitionId
	 * @return
	 */
	Boolean suspendDefinition(String definitionId);

	/**
	 * 激活流程
	 * @param definitionId
	 * @return
	 */
	Boolean activateDefinition(String definitionId);

}
