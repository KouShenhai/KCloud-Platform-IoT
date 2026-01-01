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

package org.laokou.mcp.client.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.dto.Result;
import org.laokou.mcp.client.api.DeviceServiceI;
import org.laokou.mcp.client.dto.DevicePropertyGetQry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理控制器.
 *
 * @author laokou
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "MCP管理", description = "MCP管理")
public class McpController {

	private final DeviceServiceI deviceServiceI;

	@GetMapping("/v1/mcps/device/property/{sn}")
	@PreAuthorize("hasAuthority('mcp:device:property')")
	@Operation(summary = "根据设备序列号查看设备属性", description = "根据设备序列号查看设备属性")
	public Result<String> getDevicePropertyBySn(@PathVariable("sn") String sn) {
		return deviceServiceI.getDevicePropertyBySn(new DevicePropertyGetQry(sn));
	}

}
