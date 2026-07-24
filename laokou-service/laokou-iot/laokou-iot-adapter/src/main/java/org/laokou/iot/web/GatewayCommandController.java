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

package org.laokou.iot.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.laokou.iot.gateway.api.GatewayCommandServiceI;
import org.laokou.iot.gateway.dto.GatewayCommandLogPageQry;
import org.laokou.iot.gateway.dto.GatewayReadPropertyCmd;
import org.laokou.iot.gateway.dto.GatewayRebootCmd;
import org.laokou.iot.gateway.dto.GatewayWritePropertyCmd;
import org.laokou.iot.gateway.dto.clientobject.GatewayCommandLogCO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 网关指令控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "网关指令", description = "网关指令")
public class GatewayCommandController {

	private final GatewayCommandServiceI gatewayCommandServiceI;

	@Idempotent
	@PostMapping("/v1/gateways/command/reboot")
	@PreAuthorize("hasAuthority('write') and hasAuthority('iot:gateway:command')")
	@OperateLog(module = "网关指令", operation = "重启网关")
	@Operation(summary = "重启网关", description = "重启网关")
	public Result<Long> reboot(@RequestBody GatewayRebootCmd cmd) {
		return gatewayCommandServiceI.reboot(cmd);
	}

	@Idempotent
	@PostMapping("/v1/gateways/command/read-property")
	@PreAuthorize("hasAuthority('write') and hasAuthority('iot:gateway:command')")
	@OperateLog(module = "网关指令", operation = "读取设备属性")
	@Operation(summary = "读取设备属性", description = "读取设备属性")
	public Result<Long> readProperty(@RequestBody GatewayReadPropertyCmd cmd) {
		return gatewayCommandServiceI.readProperty(cmd);
	}

	@Idempotent
	@PostMapping("/v1/gateways/command/write-property")
	@PreAuthorize("hasAuthority('write') and hasAuthority('iot:gateway:command')")
	@OperateLog(module = "网关指令", operation = "写入设备属性")
	@Operation(summary = "写入设备属性", description = "写入设备属性")
	public Result<Long> writeProperty(@RequestBody GatewayWritePropertyCmd cmd) {
		return gatewayCommandServiceI.writeProperty(cmd);
	}

	@TraceLog
	@PostMapping("/v1/gateways/command/log/page")
	@PreAuthorize("hasAuthority('read') and hasAuthority('iot:gateway:command')")
	@Operation(summary = "分页查询网关指令日志", description = "分页查询网关指令日志")
	public Result<Page<GatewayCommandLogCO>> pageCommandLog(@Validated @RequestBody GatewayCommandLogPageQry qry) {
		return gatewayCommandServiceI.pageCommandLog(qry);
	}

}
