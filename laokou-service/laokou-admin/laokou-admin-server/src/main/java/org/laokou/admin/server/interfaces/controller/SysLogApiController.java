/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.SysLogApplicationService;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.log.qo.SysLoginLogQo;
import org.laokou.common.log.qo.SysOperateLogQo;
import org.laokou.common.log.vo.SysLoginLogVO;
import org.laokou.common.log.vo.SysOperateLogVO;
import org.laokou.common.i18n.core.HttpResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

/**
 * 系统日志控制器
 *
 * @author laokou
 */
@RestController
@Tag(name = "Sys Log API", description = "系统日志API")
@RequestMapping("/sys/log/api")
@RequiredArgsConstructor
public class SysLogApiController {

	private final SysLogApplicationService sysLogApplicationService;

	@PostMapping(value = "/operate/query")
	@Operation(summary = "系统日志>操作日志>查询", description = "系统日志>操作日志>查询")
	@PreAuthorize("hasAuthority('sys:log:operate:query')")
	public HttpResult<IPage<SysOperateLogVO>> queryOperateLog(@RequestBody SysOperateLogQo qo) {
		return new HttpResult<IPage<SysOperateLogVO>>().ok(sysLogApplicationService.queryOperateLogPage(qo));
	}

	@PostMapping(value = "/operate/export")
	@Operation(summary = "系统日志>操作日志>导出", description = "系统日志>操作日志>导出")
	@OperateLog(module = "操作日志", name = "日志导出")
	@PreAuthorize("hasAuthority('sys:log:operate:export')")
	public void exportOperateLog(@RequestBody SysOperateLogQo qo, HttpServletResponse response) throws IOException {
		sysLogApplicationService.exportOperateLog(qo, response);
	}

	@PostMapping(value = "/login/query")
	@Operation(summary = "系统日志>登录日志>查询", description = "系统日志>登录日志>查询")
	@PreAuthorize("hasAuthority('sys:log:login:query')")
	public HttpResult<IPage<SysLoginLogVO>> queryLoginLog(@RequestBody SysLoginLogQo qo) {
		return new HttpResult<IPage<SysLoginLogVO>>().ok(sysLogApplicationService.queryLoginLogPage(qo));
	}

	@PostMapping(value = "/login/export")
	@Operation(summary = "系统日志>登录日志>导出", description = "系统日志>登录日志>导出")
	@OperateLog(module = "登录日志", name = "日志导出")
	@PreAuthorize("hasAuthority('sys:log:login:export')")
	public void exportLoginLog(@RequestBody SysLoginLogQo qo, HttpServletResponse response) throws IOException {
		sysLogApplicationService.exportLoginLog(qo, response);
	}

}
