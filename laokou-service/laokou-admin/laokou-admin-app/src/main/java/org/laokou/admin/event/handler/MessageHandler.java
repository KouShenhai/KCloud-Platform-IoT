/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.event.handler;

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.command.message.MessageInsertCmdExe;
import org.laokou.admin.dto.message.MessageInsertCmd;
import org.laokou.admin.dto.message.clientobject.MessageCO;
import org.laokou.admin.dto.message.domainevent.MessageEvent;
import org.laokou.admin.dto.resource.clientobject.AssigneeCO;
import org.laokou.admin.gatewayimpl.feign.TasksFeignClient;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
public class MessageHandler implements ApplicationListener<MessageEvent> {

	private final MessageInsertCmdExe messageInsertCmdExe;

	private final TasksFeignClient tasksFeignClient;

	@Override
	public void onApplicationEvent(MessageEvent event) {
		MessageCO co = ConvertUtil.sourceToTarget(event, MessageCO.class);
		if (CollectionUtil.isEmpty(co.getReceiver())) {
			Result<AssigneeCO> result = tasksFeignClient.assignee(event.getInstanceId());
			co.setReceiver(Collections.singleton(result.getData().getAssignee()));
		}
		messageInsertCmdExe.execute(new MessageInsertCmd(co));
	}

}
