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

package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.laokou.common.i18n.common.EventStatusEnum;
import org.laokou.common.i18n.common.EventTypeEnum;

import java.io.Serial;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

/**
 * @author laokou
 */
@Data
@SuperBuilder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Schema(name = "DomainEvent", description = "领域事件")
public abstract class DomainEvent<ID> implements Event {

	@Serial
	private static final long serialVersionUID = 1532877866226749304L;

	@Schema(name = "id", description = "ID")
	protected ID id;

	@Schema(name = "aggregateId", description = "聚合根ID")
	protected ID aggregateId;

	@Schema(name = "eventType", description = "事件类型")
	protected EventTypeEnum eventType;

	@Schema(name = "eventStatus", description = "事件状态")
	protected EventStatusEnum eventStatus;

	@Schema(name = "topic", description = "MQ主题")
	protected String topic;

	@Schema(name = "sourceName", description = "数据源名称")
	private String sourceName;

	@Schema(name = "appName", description = "应用名称")
	private String appName;

	@Schema(name = "creator", description = "创建人")
	protected ID creator;

	@Schema(name = "editor;", description = "编辑人")
	protected ID editor;

	@Schema(name = "deptId", description = "部门ID")
	protected ID deptId;

	@Schema(name = "deptPath", description = "部门PATH")
	protected String deptPath;

	@Schema(name = "tenantId", description = "租户ID")
	protected ID tenantId;

	@Schema(name = "createDate", description = "创建时间")
	protected LocalDateTime createDate;

	@Schema(name = "updateDate", description = "修改时间")
	protected LocalDateTime updateDate;

	public DomainEvent(ID id, EventStatusEnum eventStatus, String sourceName) {
		this.id = id;
		this.eventStatus = eventStatus;
		this.sourceName = sourceName;
	}

}
