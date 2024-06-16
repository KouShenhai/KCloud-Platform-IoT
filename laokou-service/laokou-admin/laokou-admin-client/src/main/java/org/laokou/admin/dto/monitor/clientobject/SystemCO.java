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

package org.laokou.admin.dto.monitor.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@Schema(name = "系统", description = "系统")
class SystemCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = -2249049152299436233L;

	@Schema(name = "服务器名称", description = "服务器名称")
	private String computerName;

	@Schema(name = "服务器IP", description = "服务器IP")
	private String computerIp;

	@Schema(name = "项目路径", description = "项目路径")
	private String userDir;

	@Schema(name = "操作系统", description = "操作系统")
	private String osName;

	@Schema(name = "系统架构", description = "系统架构")
	private String osArch;

}
