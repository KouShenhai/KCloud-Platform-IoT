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

package org.laokou.admin.actionimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.domain.action.StartFlowAction;
import org.laokou.admin.dto.resource.TaskStartCmd;
import org.laokou.admin.gatewayimpl.rpc.TasksFeignClient;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.openfeign.utils.FeignUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component("startFlowAction")
@RequiredArgsConstructor
public class StartFlowActionImpl implements StartFlowAction {

	private final TasksFeignClient tasksFeignClient;

	@Override
	public boolean start(String businessKey, String instanceName, String definitionKey) {
		if (StringUtil.isNotEmpty(definitionKey)) {
			log.info("<<>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<名称:{}", definitionKey);
			return FeignUtil.result(tasksFeignClient.start(convert(businessKey, instanceName, definitionKey)));
		}
		else {
			log.info("<<<<<<<<<<<ID:{}", businessKey);
			return true;
		}
	}

	@Override
	public boolean compensateStart(String businessKey, String definitionKey) {
		return true;
	}

	private TaskStartCmd convert(String businessKey, String instanceName, String definitionKey) {
		return new TaskStartCmd(definitionKey, businessKey, instanceName);
	}

}
