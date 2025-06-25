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

package org.laokou.iot.transportProtocol.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;
import java.time.Instant;

/**
 *
 * 传输协议客户端对象.
 *
 * @author laokou
 */
@Data
@Schema(name = "传输协议客户端对象", description = "传输协议客户端对象")
public class TransportProtocolCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "协议名称", description = "协议名称")
	private String name;

	@Schema(name = "协议类型", description = "协议类型")
	private String type;

	@Schema(name = "主机", description = "主机")
	private String host;

	@Schema(name = "端口", description = "端口")
	private String port;

	@Schema(name = "客户端ID", description = "客户端ID")
	private String clientId;

	@Schema(name = "用户名", description = "用户名")
	private String username;

	@Schema(name = "密码", description = "密码")
	private String password;

	@Schema(name = "备注", description = "备注")
	private String remark;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createTime;

}
