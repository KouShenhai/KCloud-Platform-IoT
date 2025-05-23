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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.loginLog.api.LoginLogsServiceI;
import org.laokou.admin.loginLog.dto.*;
import org.laokou.admin.loginLog.dto.clientobject.LoginLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.secret.annotation.ApiSecret;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 登录日志管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/login-logs")
@Tag(name = "登录日志管理", description = "登录日志管理")
public class LoginLogsControllerV3 {

	private final LoginLogsServiceI loginLogsServiceI;

	@ApiSecret
	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:login-log:save')")
	@OperateLog(module = "登录日志管理", operation = "保存登录日志")
	@Operation(summary = "保存登录日志", description = "保存登录日志")
	public void saveLoginLog(@RequestBody LoginLogSaveCmd cmd) {
		loginLogsServiceI.saveLoginLog(cmd);
	}

	@ApiSecret
	@PutMapping
	@PreAuthorize("hasAuthority('sys:login-log:modify')")
	@OperateLog(module = "登录日志管理", operation = "修改登录日志")
	@Operation(summary = "修改登录日志", description = "修改登录日志")
	public void modifyLoginLog(@RequestBody LoginLogModifyCmd cmd) {
		loginLogsServiceI.modifyLoginLog(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:login-log:remove')")
	@OperateLog(module = "登录日志管理", operation = "删除登录日志")
	@Operation(summary = "删除登录日志", description = "删除登录日志")
	public void removeLoginLog(@RequestBody Long[] ids) {
		loginLogsServiceI.removeLoginLog(new LoginLogRemoveCmd(ids));
	}

	@ApiSecret
	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:login-log:import')")
	@OperateLog(module = "登录日志管理", operation = "导入登录日志")
	@Operation(summary = "导入登录日志", description = "导入登录日志")
	public void importLoginLog(@RequestPart("files") MultipartFile[] files) {
		loginLogsServiceI.importLoginLog(new LoginLogImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:login-log:export')")
	@OperateLog(module = "登录日志管理", operation = "导出登录日志")
	@Operation(summary = "导出登录日志", description = "导出登录日志")
	public void exportLoginLog(@RequestBody LoginLogExportCmd cmd) {
		loginLogsServiceI.exportLoginLog(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:login-log:page')")
	@Operation(summary = "分页查询登录日志列表", description = "分页查询登录日志列表")
	public Result<Page<LoginLogCO>> pageLoginLog(@Validated @RequestBody LoginLogPageQry qry) {
		return loginLogsServiceI.pageLoginLog(qry);
	}

	@TraceLog
	@ApiSecret
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('sys:login-log:detail')")
	@Operation(summary = "查看登录日志详情", description = "查看登录日志详情")
	public Result<LoginLogCO> getByIdLoginLog(@PathVariable("id") Long id) {
		return loginLogsServiceI.getByIdLoginLog(new LoginLogGetQry(id));
	}

}
