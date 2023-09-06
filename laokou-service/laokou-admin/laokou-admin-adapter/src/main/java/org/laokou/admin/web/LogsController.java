/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
import org.laokou.admin.client.api.LogsServiceI;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laokou
 */
@RestController
@Tag(name = "LogsController", description = "日志")
@RequiredArgsConstructor
public class LogsController {

	private final LogsServiceI logsServiceI;

	@TraceLog
	@PostMapping(value = "v1/logs/operate-list")
	@Operation(summary = "查询", description = "查询")
	@PreAuthorize("hasAuthority('logs:operate-list')")
	public Result<?> operateList() {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping(value = "v1/logs/operate-export")
	@Operation(summary = "导出", description = "导出")
	@OperateLog(module = "操作日志", operation = "导出")
	@PreAuthorize("hasAuthority('logs:operate-export')")
	public void operateExport() {

	}

	@TraceLog
	@PostMapping(value = "v1/logs/login-list")
	@Operation(summary = "查询", description = "查询")
	@PreAuthorize("hasAuthority('logs:login-list')")
	public Result<?> loginList() {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping(value = "v1/logs/login-export")
	@Operation(summary = "导出", description = "导出")
	@OperateLog(module = "登录日志", operation = "导出")
	@PreAuthorize("hasAuthority('logs:login-export')")
	public void loginExport() {
	}

}
