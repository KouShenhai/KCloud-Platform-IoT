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
@Schema(name = "", description = "")
public class TaskCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = -8942395421885937298L;

	@Schema(name = "", description = "")
	private String taskId;

	@Schema(name = "", description = "")
	private String taskName;

	@Schema(name = "", description = "")
	private String definitionId;

	@Schema(name = "", description = "")
	private String instanceId;

	@Schema(name = "", description = "")
	private LocalDateTime createDate;

	@Schema(name = "", description = "")
	private String definitionKey;

	@Schema(name = "", description = "")
	private String name;

	@Schema(name = "", description = "")
	private String username;

	@Schema(name = "", description = "")
	private String instanceName;

	@Schema(name = "", description = "")
	private String businessKey;

}
