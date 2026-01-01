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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dict.api.DictsServiceI;
import org.laokou.admin.dict.dto.DictExportCmd;
import org.laokou.admin.dict.dto.DictGetQry;
import org.laokou.admin.dict.dto.DictImportCmd;
import org.laokou.admin.dict.dto.DictModifyCmd;
import org.laokou.admin.dict.dto.DictPageQry;
import org.laokou.admin.dict.dto.DictRemoveCmd;
import org.laokou.admin.dict.dto.DictSaveCmd;
import org.laokou.admin.dict.dto.clientobject.DictCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
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
 * 字典管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "字典管理", description = "字典管理")
public class DictsController {

	private final DictsServiceI dictsServiceI;

	@Idempotent
	@PostMapping("/v1/dicts")
	@PreAuthorize("hasAuthority('sys:dict:save')")
	@OperateLog(module = "字典管理", operation = "保存字典")
	@Operation(summary = "保存字典", description = "保存字典")
	public void saveDict(@RequestBody DictSaveCmd cmd) {
		dictsServiceI.saveDict(cmd);
	}

	@PutMapping("/v1/dicts")
	@PreAuthorize("hasAuthority('sys:dict:modify')")
	@OperateLog(module = "字典管理", operation = "修改字典")
	@Operation(summary = "修改字典", description = "修改字典")
	public void modifyDict(@RequestBody DictModifyCmd cmd) {
		dictsServiceI.modifyDict(cmd);
	}

	@DeleteMapping("/v1/dicts")
	@PreAuthorize("hasAuthority('sys:dict:remove')")
	@OperateLog(module = "字典管理", operation = "删除字典")
	@Operation(summary = "删除字典", description = "删除字典")
	public void removeDict(@RequestBody Long[] ids) {
		dictsServiceI.removeDict(new DictRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/dicts/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:dict:import')")
	@OperateLog(module = "字典管理", operation = "导入字典")
	@Operation(summary = "导入字典", description = "导入字典")
	public void importDict(@RequestPart("files") MultipartFile[] files) {
		dictsServiceI.importDict(new DictImportCmd(files));
	}

	@PostMapping("/v1/dicts/export")
	@PreAuthorize("hasAuthority('sys:dict:export')")
	@OperateLog(module = "字典管理", operation = "导出字典")
	@Operation(summary = "导出字典", description = "导出字典")
	public void exportDict(@RequestBody DictExportCmd cmd) {
		dictsServiceI.exportDict(cmd);
	}

	@TraceLog
	@PostMapping("/v1/dicts/page")
	@PreAuthorize("hasAuthority('sys:dict:page')")
	@Operation(summary = "分页查询字典列表", description = "分页查询字典列表")
	public Result<Page<DictCO>> pageDict(@Validated @RequestBody DictPageQry qry) {
		return dictsServiceI.pageDict(qry);
	}

	@TraceLog
	@GetMapping("/v1/dicts/{id}")
	@PreAuthorize("hasAuthority('sys:dict:detail')")
	@Operation(summary = "查看字典详情", description = "查看字典详情")
	public Result<DictCO> getDictById(@PathVariable("id") Long id) {
		return dictsServiceI.getDictById(new DictGetQry(id));
	}

}
