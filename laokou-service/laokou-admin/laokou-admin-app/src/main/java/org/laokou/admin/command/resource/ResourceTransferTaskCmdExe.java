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

package org.laokou.admin.command.resource;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.common.event.DomainEventPublisher;
import org.laokou.admin.common.utils.EventUtil;
import org.laokou.admin.dto.resource.ResourceTransferTaskCmd;
import org.laokou.admin.dto.resource.TaskTransferCmd;
import org.laokou.admin.gatewayimpl.rpc.TasksFeignClient;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceTransferTaskCmdExe {

	private final TasksFeignClient tasksFeignClient;

	private final DomainEventPublisher domainEventPublisher;

	private final EventUtil eventUtil;

	/**
	 *
	 * @param cmd
	 * @return
	 */
	@GlobalTransactional(rollbackFor = Exception.class)
	public Result<Boolean> execute(ResourceTransferTaskCmd cmd) {
		log.info("资源转办任务分布式事务 XID：{}", RootContext.getXID());
		Result<Boolean> result = tasksFeignClient.transfer(toCmd(cmd));
		// 发送消息
		if (result.success()) {
			publishMessage(cmd);
		}
		return result;
	}

	@Async
	public void publishMessage(ResourceTransferTaskCmd cmd) {
		domainEventPublisher.publish(eventUtil.toAuditMessageEvent(cmd.getUserId().toString(), cmd.getBusinessKey(),
				cmd.getInstanceName(), null));
	}

	private TaskTransferCmd toCmd(ResourceTransferTaskCmd cmd) {
		TaskTransferCmd c = new TaskTransferCmd();
		c.setTaskId(cmd.getTaskId());
		c.setToUserId(cmd.getUserId());
		c.setUserId(UserUtil.getUserId());
		return c;
	}

}
