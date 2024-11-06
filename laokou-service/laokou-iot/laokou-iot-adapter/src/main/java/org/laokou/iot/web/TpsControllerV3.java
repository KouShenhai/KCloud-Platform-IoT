/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.springframework.http.MediaType;
import org.laokou.iot.tp.dto.clientobject.TpCO;
import org.springframework.web.bind.annotation.*;
import org.laokou.iot.tp.api.TpsServiceI;
import org.laokou.iot.tp.dto.*;
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
@RequestMapping("v3/tps")
@Tag(name = "传输协议管理", description = "传输协议管理")
public class TpsControllerV3 {

	private final TpsServiceI tpsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('iot:tp:save')")
	@OperateLog(module = "传输协议管理", operation = "保存传输协议")
	@Operation(summary = "保存传输协议", description = "保存传输协议")
	public void saveV3(@RequestBody TpSaveCmd cmd) {
		tpsServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('iot:tp:modify')")
	@OperateLog(module = "传输协议管理", operation = "修改传输协议")
	@Operation(summary = "修改传输协议", description = "修改传输协议")
	public void modifyV3(@RequestBody TpModifyCmd cmd) {
		tpsServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('iot:tp:remove')")
	@OperateLog(module = "传输协议管理", operation = "删除传输协议")
	@Operation(summary = "删除传输协议", description = "删除传输协议")
	public void removeV3(@RequestBody Long[] ids) {
		tpsServiceI.remove(new TpRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('iot:tp:import')")
	@OperateLog(module = "传输协议管理", operation = "导入传输协议")
	@Operation(summary = "导入传输协议", description = "导入传输协议")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		tpsServiceI.importI(new TpImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('iot:tp:export')")
	@OperateLog(module = "传输协议管理", operation = "导出传输协议")
	@Operation(summary = "导出传输协议", description = "导出传输协议")
	public void exportV3(@RequestBody TpExportCmd cmd) {
		tpsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('iot:tp:page')")
	@Operation(summary = "分页查询传输协议列表", description = "分页查询传输协议列表")
	public Result<Page<TpCO>> pageV3(@RequestBody TpPageQry qry) {
		return tpsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "查看传输协议详情", description = "查看传输协议详情")
	public Result<TpCO> getByIdV3(@PathVariable("id") Long id) {
		return tpsServiceI.getById(new TpGetQry(id));
	}

}
