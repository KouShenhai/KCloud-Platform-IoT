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

package org.laokou.iot.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.transportProtocol.dto.*;
import org.springframework.http.MediaType;
import org.laokou.iot.transportProtocol.dto.clientobject.TransportProtocolCO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.laokou.iot.transportProtocol.api.TransportProtocolsServiceI;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 传输协议管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/transport-protocols")
@Tag(name = "传输协议管理", description = "传输协议管理")
public class TransportProtocolsControllerV3 {

	private final TransportProtocolsServiceI transportProtocolsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('iot:transport-protocol:save')")
	@OperateLog(module = "传输协议管理", operation = "保存传输协议")
	@Operation(summary = "保存传输协议", description = "保存传输协议")
	public void saveTransportProtocol(@RequestBody TransportProtocolSaveCmd cmd) {
		transportProtocolsServiceI.saveTransportProtocol(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('iot:transport-protocol:modify')")
	@OperateLog(module = "传输协议管理", operation = "修改传输协议")
	@Operation(summary = "修改传输协议", description = "修改传输协议")
	public void modifyTransportProtocol(@RequestBody TransportProtocolModifyCmd cmd) {
		transportProtocolsServiceI.modifyTransportProtocol(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('iot:transport-protocol:remove')")
	@OperateLog(module = "传输协议管理", operation = "删除传输协议")
	@Operation(summary = "删除传输协议", description = "删除传输协议")
	public void removeTransportProtocol(@RequestBody Long[] ids) {
		transportProtocolsServiceI.removeTransportProtocol(new TransportProtocolRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('iot:transport-protocol:import')")
	@OperateLog(module = "传输协议管理", operation = "导入传输协议")
	@Operation(summary = "导入传输协议", description = "导入传输协议")
	public void importTransportProtocol(@RequestPart("files") MultipartFile[] files) {
		transportProtocolsServiceI.importTransportProtocol(new TransportProtocolImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('iot:transport-protocol:export')")
	@OperateLog(module = "传输协议管理", operation = "导出传输协议")
	@Operation(summary = "导出传输协议", description = "导出传输协议")
	public void exportTransportProtocol(@RequestBody TransportProtocolExportCmd cmd) {
		transportProtocolsServiceI.exportTransportProtocol(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('iot:transport-protocol:page')")
	@Operation(summary = "分页查询传输协议列表", description = "分页查询传输协议列表")
	public Result<Page<TransportProtocolCO>> pageTransportProtocol(
			@Validated @RequestBody TransportProtocolPageQry qry) {
		return transportProtocolsServiceI.pageTransportProtocol(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('iot:transport-protocol:detail')")
	@Operation(summary = "查看传输协议详情", description = "查看传输协议详情")
	public Result<TransportProtocolCO> getByIdTransportProtocol(@PathVariable("id") Long id) {
		return transportProtocolsServiceI.getByIdTransportProtocol(new TransportProtocolGetQry(id));
	}

}
