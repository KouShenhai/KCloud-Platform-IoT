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

package org.laokou.admin.dto.log.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.Instant;

/**
 * @author laokou
 */
@Data
@Schema(name = "LoginLogCO", description = "登录日志")
public class LoginLogCO extends ClientObject {

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "登录的用户名", description = "登录的用户名")
	private String username;

	@Schema(name = "登录的IP地址", description = "登录的IP地址")
	private String ip;

	@Schema(name = "登录的归属地", description = "登录的归属地")
	private String address;

	@Schema(name = "登录的浏览器", description = "登录的浏览器")
	private String browser;

	@Schema(name = "登录的操作系统", description = "登录的操作系统")
	private String os;

	@Schema(name = "登录状态 0登录成功 1登录失败", description = "登录状态 0登录成功 1登录失败")
	private Integer status;

	@Schema(name = "错误信息", description = "错误信息")
	private String errorMessage;

	@Schema(name = "登录类型", description = "登录类型")
	private String type;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createDate;

}
