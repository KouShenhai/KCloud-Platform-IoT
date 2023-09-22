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

package org.laokou.flowable.command.definition;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.util.io.InputStreamSource;
import org.flowable.engine.RepositoryService;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.dto.definition.DefinitionInsertCmd;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DefinitionInsertCmdExe {

	private static final String BPMN_FILE_SUFFIX = ".bpmn";

	private final RepositoryService repositoryService;

	@SneakyThrows
	public Result<Boolean> execute(DefinitionInsertCmd cmd) {
		BpmnXMLConverter converter = new BpmnXMLConverter();
		InputStreamSource inputStreamSource = new InputStreamSource(cmd.getFile().getInputStream());
		BpmnModel bpmnModel = converter.convertToBpmnModel(inputStreamSource, true, true);
		Process process = bpmnModel.getProcesses().stream().findFirst().orElse(new Process());
		String key = process.getId();
		String name = process.getName() + BPMN_FILE_SUFFIX;
		long count = repositoryService.createDeploymentQuery().deploymentKey(key).count();
		if (count > 0) {
			throw new GlobalException("流程已存在，请更换流程图并上传");
		}
		return Result.of(deploy(key, name, bpmnModel));
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean deploy(String key, String name, BpmnModel bpmnModel) {
		return repositoryService.createDeployment().name(name).key(key).addBpmnModel(name, bpmnModel).deploy().isNew();
	}

}
