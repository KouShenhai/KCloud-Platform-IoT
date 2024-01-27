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
import org.laokou.common.i18n.common.EventStatusEnums;
import org.laokou.common.i18n.common.EventTypeEnums;

import java.io.Serial;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;
import static org.laokou.common.i18n.common.MybatisPlusConstants.*;

/**
 * @author laokou
 */
@Data
@AllArgsConstructor(access = PROTECTED)
@Schema(name = "DomainEvent", description = "领域事件")
public abstract class DomainEvent<ID> implements Event {

	@Serial
	private static final long serialVersionUID = 1532877866226749304L;

	@Schema(name = "id", description = "ID")
	protected ID id;

	@Schema(name = "aggregateId", description = "聚合根ID")
	protected ID aggregateId;

	@Schema(name = "eventType", description = "类型")
	protected EventTypeEnums eventType;

	@Schema(name = "eventStatus", description = "状态")
	protected EventStatusEnums eventStatus;

	@Schema(name = "topic", description = "主题")
	protected String topic;

	@Schema(name = CREATOR, description = "创建人")
	protected ID creator;

	@Schema(name = EDITOR, description = "编辑人")
	protected ID editor;

	@Schema(name = DEPT_ID, description = "部门ID")
	protected ID deptId;

	@Schema(name = DEPT_PATH, description = "部门PATH")
	protected String deptPath;

	@Schema(name = TENANT_ID, description = "租户ID")
	protected ID tenantId;

	@Schema(name = CREATE_DATE, description = "创建时间")
	protected LocalDateTime createDate;

	@Schema(name = UPDATE_DATE, description = "修改时间")
	protected LocalDateTime updateDate;

}
