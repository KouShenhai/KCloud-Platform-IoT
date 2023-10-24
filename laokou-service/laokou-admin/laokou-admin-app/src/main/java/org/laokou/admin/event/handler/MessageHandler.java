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
import org.laokou.admin.domain.gateway.MessageGateway;
import org.laokou.admin.domain.message.Message;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.message.domainevent.MessageEvent;
import org.laokou.admin.dto.resource.clientobject.AssigneeCO;
import org.laokou.admin.gatewayimpl.feign.TasksFeignClient;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
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

	private final MessageGateway messageGateway;

	private final TasksFeignClient tasksFeignClient;

	@Override
	public void onApplicationEvent(MessageEvent event) {
		Message message = ConvertUtil.sourceToTarget(event, Message.class);
		if (CollectionUtil.isEmpty(message.getReceiver())) {
			Result<AssigneeCO> result = tasksFeignClient.assignee(event.getInstanceId());
			message.setReceiver(Collections.singleton(result.getData().getAssignee()));
		}
		messageGateway.insert(message, ConvertUtil.sourceToTarget(UserUtil.user(), User.class));
	}

}
