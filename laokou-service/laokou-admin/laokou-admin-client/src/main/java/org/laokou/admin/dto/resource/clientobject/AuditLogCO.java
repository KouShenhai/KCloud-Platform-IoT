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

package org.laokou.admin.dto.resource.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Data
@Schema(name = "AuditLogCO", description = "审批日志")
public class AuditLogCO extends ClientObject {

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "status", description = "审批状态 0驳回审批 1通过审批")
	private Integer status;

	@Schema(name = "approver", description = "审批人")
	private String approver;

	@Schema(name = "comment", description = "审批意见")
	private String comment;

	@Schema(name = "createDate", description = "创建时间")
	private LocalDateTime createDate;

}
