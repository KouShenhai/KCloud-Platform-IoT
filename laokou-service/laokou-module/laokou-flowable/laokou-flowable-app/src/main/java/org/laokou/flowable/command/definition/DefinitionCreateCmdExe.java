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

package org.laokou.flowable.command.definition;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.flowable.domain.definition.Deployment;
import org.laokou.flowable.domain.gateway.DefinitionGateway;
import org.laokou.flowable.dto.definition.DefinitionCreateCmd;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.FLOWABLE;

/**
 * 新增流程执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefinitionCreateCmdExe {

	private final DefinitionGateway definitionGateway;

	/**
	 * 执行新增流程.
	 * @param cmd 新增流程参数
	 */
	@DS(FLOWABLE)
	@SneakyThrows
	public void executeVoid(DefinitionCreateCmd cmd) {
		definitionGateway.create(new Deployment(cmd.getFile()));
	}

}
