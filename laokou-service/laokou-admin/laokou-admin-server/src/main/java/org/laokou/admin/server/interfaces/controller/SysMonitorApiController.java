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
package org.laokou.admin.server.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.SysMonitorApplicationService;
import org.laokou.admin.client.vo.CacheVO;
import org.laokou.admin.server.infrastructure.server.Server;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laokou
 */
@RestController
@Tag(name = "Sys Monitor API", description = "系统监控API")
@RequestMapping("/sys/monitor/api")
@RequiredArgsConstructor
public class SysMonitorApiController {

	private final SysMonitorApplicationService sysMonitorApplicationService;

	@TraceLog
	@GetMapping("/cache")
	@Operation(summary = "系统监控>缓存", description = "系统监控>缓存")
	public Result<CacheVO> redis() {
		return Result.of(sysMonitorApplicationService.getCacheInfo());
	}

	@TraceLog
	@GetMapping("/server")
	@Operation(summary = "系统监控>主机", description = "系统监控>主机")
	public Result<Server> server() throws Exception {
		return Result.of(sysMonitorApplicationService.getServerInfo());
	}

}
