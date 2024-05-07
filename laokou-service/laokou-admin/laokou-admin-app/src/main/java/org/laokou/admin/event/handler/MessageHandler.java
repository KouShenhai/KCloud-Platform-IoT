/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.command.message.MessageCreateCmdExe;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * 消息处理.
 *
 * @author laokou
 */
@Slf4j
// @Component
@NonNullApi
@RequiredArgsConstructor
public class MessageHandler implements ApplicationListener {

	private final MessageCreateCmdExe messageCreateCmdExe;

	// private final TasksFeignClient tasksFeignClient;

	// @Override
	// public void onApplicationEvent(MessageEvent event) {
	// MessageCO co = ConvertUtil.sourceToTarget(event, MessageCO.class);
	// Assert.isTrue(ObjectUtil.isNotNull(co), "MessageCO is null");
	// if (CollectionUtil.isEmpty(co.getReceiver())) {
	// // 远程调用获取当前任务执行人
	// AssigneeCO result =
	// FeignUtil.result(tasksFeignClient.assignee(event.getInstanceId()));
	// co.setReceiver(Collections.singleton(result.getAssignee()));
	// }
	// messageInsertCmdExe.execute(new MessageInsertCmd(co));
	// }

	@Override
	public void onApplicationEvent(ApplicationEvent event) {

	}

}
