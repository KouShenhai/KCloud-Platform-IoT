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

package org.laokou.iot.session.dto.clientobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.common.constant.DateConstants;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.Instant;

/**
 * 会话客户端对象.
 *
 * @author laokou
 */
@Data
@Schema(name = "SessionCO", description = "会话客户端对象")
public class SessionCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "会话名称", description = "会话名称")
	private String name;

	@Schema(name = "会话主机", description = "会话主机")
	private String host;

	@Schema(name = "会话端口", description = "会话端口")
	private Integer port;

	@Schema(name = "会话用户名", description = "会话用户名")
	private String username;

	@Schema(name = "会话密码", description = "会话密码")
	private String password;

	@Schema(name = "开关状态，0关闭，1打开", description = "开关状态，0关闭，1打开")
	private Integer state;

	@Schema(name = "创建时间", description = "创建时间")
	@JsonFormat(pattern = DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS, timezone = DateConstants.DEFAULT_TIMEZONE)
	private Instant createTime;

}
