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
import org.laokou.iot.communicationProtocol.dto.*;
import org.springframework.http.MediaType;
import org.laokou.iot.communicationProtocol.dto.clientobject.CommunicationProtocolCO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.laokou.iot.communicationProtocol.api.CommunicationProtocolsServiceI;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 通讯协议管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/communication-protocols")
@Tag(name = "通讯协议管理", description = "通讯协议管理")
public class CommunicationProtocolsControllerV3 {

	private final CommunicationProtocolsServiceI communicationProtocolsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('iot:communication-protocol:save')")
	@OperateLog(module = "通讯协议管理", operation = "保存通讯协议")
	@Operation(summary = "保存通讯协议", description = "保存通讯协议")
	public void saveCommunicationProtocol(@RequestBody CommunicationProtocolSaveCmd cmd) {
		communicationProtocolsServiceI.saveCommunicationProtocol(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('iot:communication-protocol:modify')")
	@OperateLog(module = "通讯协议管理", operation = "修改通讯协议")
	@Operation(summary = "修改通讯协议", description = "修改通讯协议")
	public void modifyCommunicationProtocol(@RequestBody CommunicationProtocolModifyCmd cmd) {
		communicationProtocolsServiceI.modifyCommunicationProtocol(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('iot:communication-protocol:remove')")
	@OperateLog(module = "通讯协议管理", operation = "删除通讯协议")
	@Operation(summary = "删除通讯协议", description = "删除通讯协议")
	public void removeCommunicationProtocol(@RequestBody Long[] ids) {
		communicationProtocolsServiceI.removeCommunicationProtocol(new CommunicationProtocolRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('iot:communication-protocol:import')")
	@OperateLog(module = "通讯协议管理", operation = "导入通讯协议")
	@Operation(summary = "导入通讯协议", description = "导入通讯协议")
	public void importCommunicationProtocol(@RequestPart("files") MultipartFile[] files) {
		communicationProtocolsServiceI.importCommunicationProtocol(new CommunicationProtocolImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('iot:communication-protocol:export')")
	@OperateLog(module = "通讯协议管理", operation = "导出通讯协议")
	@Operation(summary = "导出通讯协议", description = "导出通讯协议")
	public void exportCommunicationProtocol(@RequestBody CommunicationProtocolExportCmd cmd) {
		communicationProtocolsServiceI.exportCommunicationProtocol(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('iot:communication-protocol:page')")
	@Operation(summary = "分页查询通讯协议列表", description = "分页查询通讯协议列表")
	public Result<Page<CommunicationProtocolCO>> pageCommunicationProtocol(
			@Validated @RequestBody CommunicationProtocolPageQry qry) {
		return communicationProtocolsServiceI.pageCommunicationProtocol(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('iot:communication-protocol:detail')")
	@Operation(summary = "查看通讯协议详情", description = "查看通讯协议详情")
	public Result<CommunicationProtocolCO> getCommunicationProtocolById(@PathVariable("id") Long id) {
		return communicationProtocolsServiceI.getCommunicationProtocolById(new CommunicationProtocolGetQry(id));
	}

}
