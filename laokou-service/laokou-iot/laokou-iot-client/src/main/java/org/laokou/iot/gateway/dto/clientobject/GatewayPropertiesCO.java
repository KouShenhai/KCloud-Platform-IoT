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

import java.util.Map;

/**
 * 网关属性客户端对象.
 *
 * @author laokou
 */
@Data
@Schema(name = "网关属性客户端对象", description = "网关属性客户端对象")
public class GatewayPropertiesCO extends ClientObject {

	@Schema(name = "网关序列号", description = "网关序列号")
	private String sn;

	@Schema(name = "网关名称", description = "网关名称")
	private String name;

	@Schema(name = "时间戳", description = "时间戳")
	private Long timestamp;

	@Schema(name = "网关属性", description = "网关属性")
	private Map<String, Object> properties;

}
