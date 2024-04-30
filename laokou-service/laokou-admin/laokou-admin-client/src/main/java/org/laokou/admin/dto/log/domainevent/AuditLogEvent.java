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

package org.laokou.admin.dto.log.domainevent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.laokou.common.i18n.common.EventStatusEnum;
import org.laokou.common.i18n.common.EventTypeEnum;
import org.laokou.common.i18n.dto.DomainEvent;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Setter
@Getter
@Schema(name = "AuditLogEvent", description = "审批日志事件")
public class AuditLogEvent extends DomainEvent<Long> {

	@Serial
	private static final long serialVersionUID = 1532877866226749304L;

	@Schema(name = "businessId", description = "业务ID")
	private Long businessId;

	@Schema(name = "status", description = "审批状态 0驳回审批 1通过审批")
	private Integer status;

	@Schema(name = "approver", description = "审批人")
	private String approver;

	@Schema(name = "comment", description = "审批意见")
	private String comment;

	protected AuditLogEvent(Long aLong, Long aggregateId, EventTypeEnum eventType, EventStatusEnum eventStatus,
			String topic, String sourceName, String appName, Long creator, Long editor, Long deptId, String deptPath,
			Long tenantId, LocalDateTime createDate, LocalDateTime updateDate) {
		super(aLong, aggregateId, eventType, eventStatus, topic, sourceName, appName, creator, editor, deptId, deptPath,
				tenantId, createDate, updateDate);
	}

}
