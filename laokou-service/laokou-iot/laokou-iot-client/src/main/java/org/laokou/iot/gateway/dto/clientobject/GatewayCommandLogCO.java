/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.gateway.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;
import java.time.Instant;

/**
 *
 * 网关指令日志客户端对象.
 *
 * @author laokou
 */
@Data
@Schema(name = "网关指令日志客户端对象", description = "网关指令日志客户端对象")
public class GatewayCommandLogCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "指令ID", description = "指令ID")
	private Long commandId;

	@Schema(name = "网关ID", description = "网关ID")
	private Long gatewayId;

	@Schema(name = "网关标识", description = "网关标识")
	private String gatewayKey;

	@Schema(name = "指令类型 1重启网关 2读取设备属性 3写入设备属性", description = "指令类型 1重启网关 2读取设备属性 3写入设备属性")
	private Integer type;

	@Schema(name = "设备标识", description = "设备标识")
	private String deviceKey;

	@Schema(name = "指令内容", description = "指令内容")
	private String payload;

	@Schema(name = "指令状态 0待处理 1成功 2失败", description = "指令状态 0待处理 1成功 2失败")
	private Integer status;

	@Schema(name = "指令回执", description = "指令回执")
	private String result;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createTime;

}
