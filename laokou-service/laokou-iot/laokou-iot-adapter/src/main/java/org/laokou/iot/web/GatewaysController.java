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
import org.laokou.iot.gateway.api.GatewaysServiceI;
import org.laokou.iot.gateway.dto.GatewayExportCmd;
import org.laokou.iot.gateway.dto.GatewayGetQry;
import org.laokou.iot.gateway.dto.GatewayImportCmd;
import org.laokou.iot.gateway.dto.GatewayModifyCmd;
import org.laokou.iot.gateway.dto.GatewayPageQry;
import org.laokou.iot.gateway.dto.GatewayRemoveCmd;
import org.laokou.iot.gateway.dto.GatewaySaveCmd;
import org.laokou.iot.gateway.dto.clientobject.GatewayCO;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 网关管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "网关管理", description = "网关管理")
public class GatewaysController {

	private final GatewaysServiceI gatewaysServiceI;

	@Idempotent
	@PostMapping("/v1/gateways")
	@PreAuthorize("hasAuthority('write') and hasAuthority('iot:gateway:save')")
	@OperateLog(module = "网关管理", operation = "保存网关")
	@Operation(summary = "保存网关", description = "保存网关")
	public void saveGateway(@RequestBody GatewaySaveCmd cmd) {
		gatewaysServiceI.saveGateway(cmd);
	}

	@PutMapping("/v1/gateways")
	@PreAuthorize("hasAuthority('write') and hasAuthority('iot:gateway:modify')")
	@OperateLog(module = "网关管理", operation = "修改网关")
	@Operation(summary = "修改网关", description = "修改网关")
	public void modifyGateway(@RequestBody GatewayModifyCmd cmd) {
		gatewaysServiceI.modifyGateway(cmd);
	}

	@DeleteMapping("/v1/gateways")
	@PreAuthorize("hasAuthority('write') and hasAuthority('iot:gateway:remove')")
	@OperateLog(module = "网关管理", operation = "删除网关")
	@Operation(summary = "删除网关", description = "删除网关")
	public void removeGateway(@RequestBody Long[] ids) {
		gatewaysServiceI.removeGateway(new GatewayRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/gateways/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('write') and hasAuthority('iot:gateway:import')")
	@OperateLog(module = "网关管理", operation = "导入网关")
	@Operation(summary = "导入网关", description = "导入网关")
	public void importGateway(@RequestPart("files") MultipartFile[] files) {
		gatewaysServiceI.importGateway(new GatewayImportCmd(files));
	}

	@PostMapping("/v1/gateways/export")
	@PreAuthorize("hasAuthority('write') and hasAuthority('iot:gateway:export')")
	@OperateLog(module = "网关管理", operation = "导出网关")
	@Operation(summary = "导出网关", description = "导出网关")
	public void exportGateway(@RequestBody GatewayExportCmd cmd) {
		gatewaysServiceI.exportGateway(cmd);
	}

	@TraceLog
	@PostMapping("/v1/gateways/page")
	@PreAuthorize("hasAuthority('read') and hasAuthority('iot:gateway:page')")
	@Operation(summary = "分页查询网关列表", description = "分页查询网关列表")
	public Result<Page<GatewayCO>> pageGateway(@Validated @RequestBody GatewayPageQry qry) {
		return gatewaysServiceI.pageGateway(qry);
	}

	@TraceLog
	@GetMapping("/v1/gateways/{id}")
	@PreAuthorize("hasAuthority('read') and hasAuthority('iot:gateway:detail')")
	@Operation(summary = "查看网关详情", description = "查看网关详情")
	public Result<GatewayCO> getGatewayById(@PathVariable("id") Long id) {
		return gatewaysServiceI.getGatewayById(new GatewayGetQry(id));
	}

}
