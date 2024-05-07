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

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Data
@Schema(name = "TaskCO", description = "任务流程")
public class TaskCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = -8942395421885937298L;

	@Schema(name = "taskId", description = "任务ID")
	private String taskId;

	@Schema(name = "taskName", description = "任务名称")
	private String taskName;

	@Schema(name = "definitionId", description = "定义ID")
	private String definitionId;

	@Schema(name = "instanceId", description = "实例ID")
	private String instanceId;

	@Schema(name = "createDate", description = "创建时间")
	private LocalDateTime createDate;

	@Schema(name = "definitionKey", description = "定义Key")
	private String definitionKey;

	@Schema(name = "name", description = "流程名称")
	private String name;

	@Schema(name = "username", description = "流程执行人")
	private String username;

	@Schema(name = "instanceName", description = "实例名称")
	private String instanceName;

	@Schema(name = "", description = "")
	private String businessKey;

}
