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

package org.laokou.flowable.gatewayimpl.database.dataobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Data
@Schema(name = "TaskDO", description = "任务")
public class TaskDO {

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

	@Schema(name = "instanceName", description = "实例名称")
	private String instanceName;

	@Schema(name = "businessKey", description = "业务Key")
	private String businessKey;

}
