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
@Schema(name = "系统文件", description = "系统文件")
class SystemFileCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = 2307419364818519046L;

	@Schema(name = "盘符路径", description = "盘符路径")
	private String dirName;

	@Schema(name = "盘符类型", description = "盘符类型")
	private String sysTypeName;

	@Schema(name = "文件类型", description = "文件类型")
	private String typeName;

	@Schema(name = "总大小", description = "总大小")
	private String total;

	@Schema(name = "剩余大小", description = "剩余大小")
	private String free;

	@Schema(name = "已经使用量", description = "已经使用量")
	private String used;

	@Schema(name = "资源的使用率", description = "资源的使用率")
	private double usage;

}
