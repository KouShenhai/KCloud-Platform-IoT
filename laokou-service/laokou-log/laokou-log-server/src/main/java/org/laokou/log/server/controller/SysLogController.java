/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.log.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.log.client.dto.AuditLogDTO;
import org.laokou.log.client.dto.LoginLogDTO;
import org.laokou.log.client.dto.OperateLogDTO;
import org.laokou.log.server.service.SysLoginLogService;
import org.laokou.log.server.service.SysOperateLogService;
import org.laokou.log.server.service.SysAuditLogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/log/api")
@Tag(name = "Sys Log API",description = "系统日志API")
public class SysLogController {

    private final SysOperateLogService sysOperateLogService;

    private final SysLoginLogService sysLoginLogService;

    private final SysAuditLogService sysAuditLogService;

    @PostMapping("/login")
    @Operation(summary = "系统日志>登录日志>新增",description = "系统日志>登录日志>新增")
    public HttpResult<Boolean> login(@RequestBody LoginLogDTO dto) {
        return new HttpResult<Boolean>().ok(sysLoginLogService.insertLoginLog(dto));
    }

    @PostMapping("/operate")
    @Operation(summary = "系统日志>操作日志>新增",description = "系统日志>操作日志>新增")
    public HttpResult<Boolean> operate(@RequestBody OperateLogDTO dto) {
        return new HttpResult<Boolean>().ok(sysOperateLogService.insertOperateLog(dto));
    }

    @PostMapping("/audit")
    @Operation(summary = "系统日志>审批日志>新增",description = "系统日志>审批日志>新增")
    public HttpResult<Boolean> audit(@RequestBody AuditLogDTO dto) {
        return new HttpResult<Boolean>().ok(sysAuditLogService.insertAuditLog(dto));
    }

}
