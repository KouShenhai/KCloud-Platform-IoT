/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import com.baomidou.dynamic.datasource.annotation.DS;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.common.utils.EventUtil;
import org.laokou.admin.dto.log.domainevent.AuditLogEvent;
import org.laokou.admin.dto.resource.ResourceAuditTaskCmd;
import org.laokou.admin.gatewayimpl.database.ResourceAuditMapper;
import org.laokou.admin.gatewayimpl.database.ResourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceAuditDO;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 审批资源任务流程执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceAuditTaskCmdExe {

	// private final TasksFeignClient tasksFeignClient;

	private final ResourceAuditMapper resourceAuditMapper;

	private final ResourceMapper resourceMapper;

	private final EventUtil eventUtil;

	/**
	 * 执行审批资源任务流程.
	 * @param cmd 审批资源任务流程参数
	 * @return 执行审批结果
	 */
	@DS(TENANT)
	@GlobalTransactional(rollbackFor = Exception.class)
	public Result<Boolean> execute(ResourceAuditTaskCmd cmd) {
		/*
		 * log.info("资源审批任务分布式事务 XID：{}", RootContext.getXID()); TaskAuditCmd taskAuditCmd
		 * = ConvertUtil.sourceToTarget(cmd, TaskAuditCmd.class); AuditCO co =
		 * FeignUtil.result(tasksFeignClient.audit(taskAuditCmd)); // 下一个审批人 String
		 * assignee = co.getAssignee(); // 审批状态 int auditStatus =
		 * Integer.parseInt(cmd.getValues().get(STATUS).toString()); //
		 * 还有审批人，就是审批中，没有审批人就结束审批 int status = StringUtil.isNotEmpty(assignee) ?
		 * IN_APPROVAL.getValue() // 通过审批 或 驳回审批 : auditStatus == PASS.getValue() ?
		 * APPROVED.getValue() : REJECT_APPROVAL.getValue(); // 修改审批状态，审批通过需要将审批通过内容更新至资源表
		 * Long id = cmd.getBusinessKey(); int version = resourceMapper.getVersion(id,
		 * ResourceDO.class); ResourceAuditDO resourceAuditDO = null; if (status ==
		 * APPROVED.getValue()) { resourceAuditDO =
		 * resourceAuditMapper.getResourceAuditByResourceId(id); } boolean flag =
		 * updateResource(id, version, status, resourceAuditDO); // 审批日志 //
		 * domainEventPublisher.publish(toAuditLogEvent(cmd, auditStatus)); //
		 * 审批中，则发送审批通知消息 if (status == IN_APPROVAL.getValue()) { publishMessage(assignee,
		 * cmd); } return Result.ok(flag);
		 */
		return null;
	}

	/**
	 * 推送审批消息.
	 * @param assignee 执行人
	 * @param cmd 审批资源任务流程参数
	 */
	public void publishMessage(String assignee, ResourceAuditTaskCmd cmd) {
		// domainEventPublisher
		// .publish(eventUtil.toAuditMessageEvent(assignee, cmd.getBusinessKey(),
		// cmd.getInstanceName(), null));
	}

	/**
	 * 修改资源.
	 * @param id ID
	 * @param version 版本
	 * @param status 状态
	 * @param resourceAuditDO 资源审批对象
	 * @return 修改结果
	 */
	private Boolean updateResource(Long id, int version, int status, ResourceAuditDO resourceAuditDO) {
		ResourceDO resourceDO;
		if (ObjectUtil.isNotNull(resourceAuditDO)) {
			resourceDO = ConvertUtil.sourceToTarget(resourceAuditDO, ResourceDO.class);
		}
		else {
			resourceDO = new ResourceDO();
		}
		Assert.isTrue(ObjectUtil.isNotNull(resourceDO), "resourceDO is null");
		resourceDO.setId(id);
		resourceDO.setStatus(status);
		resourceDO.setVersion(version);
		return resourceMapper.updateById(resourceDO) > 0;
	}

	/**
	 * 转换为审批日志事件.
	 * @param cmd 审批资源任务流程参数
	 * @param auditStatus 审批状态
	 * @return 审批日志事件
	 */
	private AuditLogEvent toAuditLogEvent(ResourceAuditTaskCmd cmd, int auditStatus) {
		AuditLogEvent event = null;
		event.setApprover(UserUtil.getUserName());
		event.setComment(cmd.getComment());
		event.setBusinessId(cmd.getBusinessKey());
		event.setStatus(auditStatus);
		return event;
	}

}
