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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.LogsServiceI;
import org.laokou.admin.dto.log.LoginLogPageQry;
import org.laokou.admin.dto.log.clientobject.LoginLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/logs")
@Tag(name = "日志管理", description = "日志管理")
public class LogsV3Controller {

	private final LogsServiceI logsServiceI;

	@PostMapping("login/page")
	@Operation(summary = "分页查询登录日志列表", description = "分页查询登录日志列表")
	public Result<Page<LoginLogCO>> pageLogin() {
		return logsServiceI.pageLogin(new LoginLogPageQry());
	}

}
