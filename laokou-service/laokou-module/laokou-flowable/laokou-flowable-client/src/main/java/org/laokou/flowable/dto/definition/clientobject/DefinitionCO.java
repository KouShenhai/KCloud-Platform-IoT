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

package org.laokou.flowable.dto.definition.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * @author laokou
 */
@Data
@Schema(name = "DefinitionCO", description = "流程定义")
public class DefinitionCO extends ClientObject {

	@Schema(name = "definitionId", description = "定义ID")
	private String definitionId;

	@Schema(name = "processName", description = "流程名称")
	private String processName;

	@Schema(name = "processKey", description = "流程Key")
	private String processKey;

	@Schema(name = "deploymentId", description = "部署ID")
	private String deploymentId;

	@Schema(name = "isSuspended", description = "流程状态 0激活 1挂起")
	private Boolean isSuspended;

}
