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
import org.laokou.iot.device.api.DevicesServiceI;
import org.laokou.iot.device.dto.DeviceExportCmd;
import org.laokou.iot.device.dto.DeviceGetQry;
import org.laokou.iot.device.dto.DeviceImportCmd;
import org.laokou.iot.device.dto.DeviceModifyCmd;
import org.laokou.iot.device.dto.DevicePageQry;
import org.laokou.iot.device.dto.DeviceRemoveCmd;
import org.laokou.iot.device.dto.DeviceSaveCmd;
import org.laokou.iot.device.dto.clientobject.DeviceCO;
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
 * 设备管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "设备管理", description = "设备管理")
public class DevicesController {

	private final DevicesServiceI devicesServiceI;

	@Idempotent
	@PostMapping("/v1/devices")
	@PreAuthorize("hasAuthority('iot:device:save')")
	@OperateLog(module = "设备管理", operation = "保存设备")
	@Operation(summary = "保存设备", description = "保存设备")
	public void saveDevice(@RequestBody DeviceSaveCmd cmd) {
		devicesServiceI.saveDevice(cmd);
	}

	@PutMapping("/v1/devices")
	@PreAuthorize("hasAuthority('iot:device:modify')")
	@OperateLog(module = "设备管理", operation = "修改设备")
	@Operation(summary = "修改设备", description = "修改设备")
	public void modifyDevice(@RequestBody DeviceModifyCmd cmd) {
		devicesServiceI.modifyDevice(cmd);
	}

	@DeleteMapping("/v1/devices")
	@PreAuthorize("hasAuthority('iot:device:remove')")
	@OperateLog(module = "设备管理", operation = "删除设备")
	@Operation(summary = "删除设备", description = "删除设备")
	public void removeDevice(@RequestBody Long[] ids) {
		devicesServiceI.removeDevice(new DeviceRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/devices/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('iot:device:import')")
	@OperateLog(module = "设备管理", operation = "导入设备")
	@Operation(summary = "导入设备", description = "导入设备")
	public void importDevice(@RequestPart("files") MultipartFile[] files) {
		devicesServiceI.importDevice(new DeviceImportCmd(files));
	}

	@PostMapping("/v1/devices/export")
	@PreAuthorize("hasAuthority('iot:device:export')")
	@OperateLog(module = "设备管理", operation = "导出设备")
	@Operation(summary = "导出设备", description = "导出设备")
	public void exportDevice(@RequestBody DeviceExportCmd cmd) {
		devicesServiceI.exportDevice(cmd);
	}

	@TraceLog
	@PostMapping("/v1/devices/page")
	@PreAuthorize("hasAuthority('iot:device:page')")
	@Operation(summary = "分页查询设备列表", description = "分页查询设备列表")
	public Result<Page<DeviceCO>> pageDevice(@Validated @RequestBody DevicePageQry qry) {
		return devicesServiceI.pageDevice(qry);
	}

	@TraceLog
	@GetMapping("/v1/devices/{id}")
	@PreAuthorize("hasAuthority('iot:device:detail')")
	@Operation(summary = "查看设备详情", description = "查看设备详情")
	public Result<DeviceCO> getDeviceById(@PathVariable("id") Long id) {
		return devicesServiceI.getDeviceById(new DeviceGetQry(id));
	}

}
