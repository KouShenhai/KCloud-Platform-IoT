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

package org.laokou.admin.command.resource;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.common.event.DomainEventPublisher;
import org.laokou.admin.domain.message.Type;
import org.laokou.admin.dto.log.domainevent.AuditLogEvent;
import org.laokou.admin.dto.message.domainevent.MessageEvent;
import org.laokou.admin.dto.resource.ResourceAuditTaskCmd;
import org.laokou.admin.dto.resource.TaskAuditCmd;
import org.laokou.admin.dto.resource.clientobject.AuditCO;
import org.laokou.admin.gatewayimpl.database.ResourceAuditMapper;
import org.laokou.admin.gatewayimpl.database.ResourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceAuditDO;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceDO;
import org.laokou.admin.gatewayimpl.feign.TasksFeignClient;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.laokou.admin.domain.resource.Status.*;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceAuditTaskCmdExe {

	private static final String AUDIT_STATUS = "auditStatus";

	private final TasksFeignClient tasksFeignClient;

	private final ResourceAuditMapper resourceAuditMapper;

	private final ResourceMapper resourceMapper;

	private final DomainEventPublisher domainEventPublisher;

	@GlobalTransactional(rollbackFor = Exception.class)
	public Result<Boolean> execute(ResourceAuditTaskCmd cmd) {
		log.info("审批任务分布式事务 XID:{}", RootContext.getXID());
		TaskAuditCmd taskAuditCmd = ConvertUtil.sourceToTarget(cmd, TaskAuditCmd.class);
		Result<AuditCO> result = tasksFeignClient.audit(taskAuditCmd);
		if (result.fail()) {
			throw new GlobalException(result.getMsg());
		}
		// 下一个审批人
		String assignee = result.getData().getAssignee();
		// 审批状态
		int auditStatus = Integer.parseInt(cmd.getValues().get(AUDIT_STATUS).toString());
		// 还有审批人，就是审批中，没有审批人就结束审批
		int status = StringUtil.isNotEmpty(assignee) ? IN_APPROVAL
				// 通过审批 或 驳回审批
				: auditStatus == PASS ? APPROVED : REJECT_APPROVAL;
		// 修改审批状态，审批通过需要将审批通过内容更新至资源表
		Long id = cmd.getBusinessKey();
		int version = resourceMapper.getVersion(id, ResourceDO.class);
		ResourceAuditDO resourceAuditDO = null;
		if (status == APPROVED) {
			resourceAuditDO = resourceAuditMapper.getResourceAuditById(id);
		}
		boolean flag = updateResource(id, version, status, resourceAuditDO);
		// 审批日志
		domainEventPublisher.publish(toAuditLogEvent(cmd, auditStatus));
		// 审批中，则发送审批通知消息
		if (status == IN_APPROVAL) {
			domainEventPublisher.publish(toMessageEvent(assignee, cmd));
		}
		return Result.of(flag);
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean updateResource(Long id, int version, int status, ResourceAuditDO resourceAuditDO) {
		ResourceDO resourceDO;
		if (resourceAuditDO != null) {
			resourceDO = ConvertUtil.sourceToTarget(resourceAuditDO, ResourceDO.class);
		}
		else {
			resourceDO = new ResourceDO();
		}
		resourceDO.setId(id);
		resourceDO.setStatus(status);
		resourceDO.setVersion(version);
		return resourceMapper.updateById(resourceDO) > 0;
	}

	private MessageEvent toMessageEvent(String assignee, ResourceAuditTaskCmd cmd) {
		String title = "资源待审批任务提醒";
		String content = String.format("编号为%s，名称为%s的资源需要审批，请及时查看并审批", cmd.getBusinessKey(), cmd.getInstanceName());
		MessageEvent event = new MessageEvent(this);
		event.setContent(content);
		event.setTitle(title);
		event.setType(Type.REMIND.ordinal());
		event.setReceiver(Collections.singleton(assignee));
		return event;
	}

	private AuditLogEvent toAuditLogEvent(ResourceAuditTaskCmd cmd, int auditStatus) {
		AuditLogEvent event = new AuditLogEvent(this);
		event.setApprover(UserUtil.getUserName());
		event.setComment(cmd.getComment());
		event.setBusinessId(cmd.getBusinessKey());
		event.setStatus(auditStatus);
		return event;
	}

}
