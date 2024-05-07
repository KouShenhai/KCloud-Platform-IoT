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

package org.laokou.admin.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 * @author laokou
 */
@Data
@Schema(name = "ResourceDelegateTaskCmd", description = "委派资源任务流程命令请求")
public class ResourceDelegateTaskCmd extends CommonCommand {

	@Schema(name = "businessKey", description = "业务Key")
	private Long businessKey;

	@Schema(name = "instanceName", description = "实例名称")
	private String instanceName;

	@Schema(name = "taskId", description = "任务ID")
	private String taskId;

	@Schema(name = "userId", description = "用户ID")
	private Long userId;

}
