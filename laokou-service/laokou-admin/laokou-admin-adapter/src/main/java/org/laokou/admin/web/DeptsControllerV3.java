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
import org.laokou.admin.dept.api.DeptsServiceI;
import org.laokou.admin.dept.dto.*;
import org.laokou.admin.dept.dto.clientobject.DeptCO;
import org.laokou.admin.dept.dto.clientobject.DeptTreeCO;
import org.laokou.common.data.cache.annotation.DataCache;
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

import java.util.List;

import static org.laokou.common.data.cache.constant.NameConstants.DEPTS;
import static org.laokou.common.data.cache.model.OperateTypeEnum.DEL;

/**
 * 部门管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/depts")
@Tag(name = "部门管理", description = "部门管理")
public class DeptsControllerV3 {

	private final DeptsServiceI deptsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:dept:save')")
	@OperateLog(module = "部门管理", operation = "保存部门")
	@Operation(summary = "保存部门", description = "保存部门")
	public void saveDept(@RequestBody DeptSaveCmd cmd) {
		deptsServiceI.saveDept(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:dept:modify')")
	@OperateLog(module = "部门管理", operation = "修改部门")
	@Operation(summary = "修改部门", description = "修改部门")
	@DataCache(name = DEPTS, key = "#cmd.co.id", operateType = DEL)
	public void modifyDept(@RequestBody DeptModifyCmd cmd) {
		deptsServiceI.modifyDept(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:dept:remove')")
	@OperateLog(module = "部门管理", operation = "删除部门")
	@Operation(summary = "删除部门", description = "删除部门")
	public void removeDept(@RequestBody Long[] ids) {
		deptsServiceI.removeDept(new DeptRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:dept:import')")
	@OperateLog(module = "部门管理", operation = "导入部门")
	@Operation(summary = "导入部门", description = "导入部门")
	public void importDept(@RequestPart("files") MultipartFile[] files) {
		deptsServiceI.importDept(new DeptImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:dept:export')")
	@OperateLog(module = "部门管理", operation = "导出部门")
	@Operation(summary = "导出部门", description = "导出部门")
	public void exportDept(@RequestBody DeptExportCmd cmd) {
		deptsServiceI.exportDept(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:dept:page')")
	@Operation(summary = "分页查询部门列表", description = "分页查询部门列表")
	public Result<Page<DeptCO>> pageDept(@Validated @RequestBody DeptPageQry qry) {
		return deptsServiceI.pageDept(qry);
	}

	@TraceLog
	@PostMapping("list-tree")
	@PreAuthorize("hasAuthority('sys:dept:list-tree')")
	@Operation(summary = "查询部门树列表", description = "查询部门树列表")
	public Result<List<DeptTreeCO>> listTreeDept(@RequestBody DeptTreeListQry qry) {
		return deptsServiceI.listTreeDept(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@DataCache(name = DEPTS, key = "#id")
	@PreAuthorize("hasAuthority('sys:dept:detail')")
	@Operation(summary = "查看部门详情", description = "查看部门详情")
	public Result<DeptCO> getByIdDept(@PathVariable("id") Long id) {
		return deptsServiceI.getByIdDept(new DeptGetQry(id));
	}

}
