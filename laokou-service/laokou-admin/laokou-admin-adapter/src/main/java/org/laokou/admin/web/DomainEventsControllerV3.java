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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.domainEvent.api.DomainEventsServiceI;
import org.laokou.admin.domainEvent.dto.*;
import org.laokou.admin.domainEvent.dto.clientobject.DomainEventCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.secret.annotation.ApiSecret;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 领域事件管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/domain-events")
@Tag(name = "领域事件管理", description = "领域事件管理")
public class DomainEventsControllerV3 {

	private final DomainEventsServiceI domainEventsServiceI;

	@ApiSecret
	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('domain:domain-event:save')")
	@OperateLog(module = "领域事件管理", operation = "保存领域事件")
	@Operation(summary = "保存领域事件", description = "保存领域事件")
	public void saveV3(@RequestBody DomainEventSaveCmd cmd) {
		domainEventsServiceI.save(cmd);
	}

	@ApiSecret
	@PutMapping
	@PreAuthorize("hasAuthority('domain:domain-event:modify')")
	@OperateLog(module = "领域事件管理", operation = "修改领域事件")
	@Operation(summary = "修改领域事件", description = "修改领域事件")
	public void modifyV3(@RequestBody DomainEventModifyCmd cmd) {
		domainEventsServiceI.modify(cmd);
	}

	@ApiSecret
	@DeleteMapping
	@PreAuthorize("hasAuthority('domain:domain-event:remove')")
	@OperateLog(module = "领域事件管理", operation = "删除领域事件")
	@Operation(summary = "删除领域事件", description = "删除领域事件")
	public void removeV3(@RequestBody Long[] ids) {
		domainEventsServiceI.remove(new DomainEventRemoveCmd(ids));
	}

	@ApiSecret
	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('domain:domain-event:import')")
	@OperateLog(module = "领域事件管理", operation = "导入领域事件")
	@Operation(summary = "导入领域事件", description = "导入领域事件")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		domainEventsServiceI.importI(new DomainEventImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('domain:domain-event:export')")
	@OperateLog(module = "领域事件管理", operation = "导出领域事件")
	@Operation(summary = "导出领域事件", description = "导出领域事件")
	public void exportV3(@RequestBody DomainEventExportCmd cmd) {
		domainEventsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('domain:domain-event:page')")
	@Operation(summary = "分页查询领域事件列表", description = "分页查询领域事件列表")
	public Result<Page<DomainEventCO>> pageV3(@RequestBody DomainEventPageQry qry) {
		return domainEventsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "查看领域事件详情", description = "查看领域事件详情")
	public Result<DomainEventCO> getByIdV3(@PathVariable("id") Long id) {
		return domainEventsServiceI.getById(new DomainEventGetQry(id));
	}

}
