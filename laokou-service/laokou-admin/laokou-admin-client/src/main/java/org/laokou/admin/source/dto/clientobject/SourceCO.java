/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.source.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * 数据源客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "数据源客户端对象", description = "数据源客户端对象")
public class SourceCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "数据源名称", description = "数据源名称")
	private String name;

	@Schema(name = "数据源驱动名称", description = "数据源驱动名称")
	private String driverClassName;

	@Schema(name = "数据源连接信息", description = "数据源连接信息")
	private String url;

	@Schema(name = "数据源用户名", description = "数据源用户名")
	private String username;

	@Schema(name = "数据源的密码", description = "数据源的密码")
	private String password;

}
