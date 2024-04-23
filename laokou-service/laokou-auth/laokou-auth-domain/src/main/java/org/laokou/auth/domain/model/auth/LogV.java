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

package org.laokou.auth.domain.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "LogV", description = "日志值对象")
public record LogV(@Schema(name = "loginIp", description = "登录IP") String loginIp,
		@Schema(name = "address", description = "登录的归属地") String address,
		@Schema(name = "browser", description = "登录的浏览器") String browser,
		@Schema(name = "os", description = "登录的操作系统") String os,
		@Schema(name = "loginDate", description = "登录时间") LocalDateTime loginDate) {

	public LogV(String loginIp, String address, String browser, String os, LocalDateTime loginDate) {
		this.loginIp = loginIp;
		this.address = address;
		this.browser = browser;
		this.os = os;
		this.loginDate = loginDate;
	}

}
