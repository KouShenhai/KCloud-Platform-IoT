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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.i18nMessage.api.I18nMessagesServiceI;
import org.laokou.admin.i18nMessage.dto.*;
import org.laokou.admin.i18nMessage.dto.clientobject.I18nMessageCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 国际化消息管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/i18n-messages")
@Tag(name = "国际化消息管理", description = "国际化消息管理")
public class I18nMessagesControllerV3 {

	private final I18nMessagesServiceI i18nMessagesServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:i18n-message:save')")
	@OperateLog(module = "国际化消息管理", operation = "保存国际化消息")
	@Operation(summary = "保存国际化消息", description = "保存国际化消息")
	public void saveI18nMessage(@RequestBody I18nMessageSaveCmd cmd) {
		i18nMessagesServiceI.saveI18nMessage(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:i18n-message:modify')")
	@OperateLog(module = "国际化消息管理", operation = "修改国际化消息")
	@Operation(summary = "修改国际化消息", description = "修改国际化消息")
	public void modifyI18nMessage(@RequestBody I18nMessageModifyCmd cmd) {
		i18nMessagesServiceI.modifyI18nMessage(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:i18n-message:remove')")
	@OperateLog(module = "国际化消息管理", operation = "删除国际化消息")
	@Operation(summary = "删除国际化消息", description = "删除国际化消息")
	public void removeI18nMessage(@RequestBody Long[] ids) {
		i18nMessagesServiceI.removeI18nMessage(new I18nMessageRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:i18n-message:import')")
	@OperateLog(module = "国际化消息管理", operation = "导入国际化消息")
	@Operation(summary = "导入国际化消息", description = "导入国际化消息")
	public void importI18nMessage(@RequestPart("files") MultipartFile[] files) {
		i18nMessagesServiceI.importI18nMessage(new I18nMessageImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:i18n-message:export')")
	@OperateLog(module = "国际化消息管理", operation = "导出国际化消息")
	@Operation(summary = "导出国际化消息", description = "导出国际化消息")
	public void exportI18nMessage(@RequestBody I18nMessageExportCmd cmd) {
		i18nMessagesServiceI.exportI18nMessage(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:i18n-message:page')")
	@Operation(summary = "分页查询国际化消息列表", description = "分页查询国际化消息列表")
	public Result<Page<I18nMessageCO>> pageI18nMessage(@Validated @RequestBody I18nMessagePageQry qry) {
		return i18nMessagesServiceI.pageI18nMessage(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('sys:i18n-message:detail')")
	@Operation(summary = "查看国际化消息详情", description = "查看国际化消息详情")
	public Result<I18nMessageCO> getByIdI18nMessage(@PathVariable("id") Long id) {
		return i18nMessagesServiceI.getByIdI18nMessage(new I18nMessageGetQry(id));
	}

}
