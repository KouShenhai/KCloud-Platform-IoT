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
import org.laokou.admin.dept.api.DeptsServiceI;
import org.laokou.admin.dept.dto.DeptExportCmd;
import org.laokou.admin.dept.dto.DeptGetQry;
import org.laokou.admin.dept.dto.DeptImportCmd;
import org.laokou.admin.dept.dto.DeptModifyCmd;
import org.laokou.admin.dept.dto.DeptPageQry;
import org.laokou.admin.dept.dto.DeptRemoveCmd;
import org.laokou.admin.dept.dto.DeptSaveCmd;
import org.laokou.admin.dept.dto.DeptTreeListQry;
import org.laokou.admin.dept.dto.clientobject.DeptCO;
import org.laokou.admin.dept.dto.clientobject.DeptTreeCO;
import org.laokou.common.data.cache.annotation.DistributedCache;
import org.laokou.common.data.cache.constant.NameConstants;
import org.laokou.common.data.cache.model.OperateType;
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

import java.util.List;

/**
 * 部门管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "部门管理", description = "部门管理")
public class DeptsController {

	private final DeptsServiceI deptsServiceI;

	@Idempotent
	@PostMapping("/v1/depts")
	@PreAuthorize("hasAuthority('sys:dept:save')")
	@OperateLog(module = "部门管理", operation = "保存部门")
	@Operation(summary = "保存部门", description = "保存部门")
	public void saveDept(@RequestBody DeptSaveCmd cmd) {
		deptsServiceI.saveDept(cmd);
	}

	@PutMapping("/v1/depts")
	@PreAuthorize("hasAuthority('sys:dept:modify')")
	@OperateLog(module = "部门管理", operation = "修改部门")
	@Operation(summary = "修改部门", description = "修改部门")
	@DistributedCache(name = NameConstants.DEPTS, key = "#cmd.co.id", operateType = OperateType.DEL)
	public void modifyDept(@RequestBody DeptModifyCmd cmd) {
		deptsServiceI.modifyDept(cmd);
	}

	@DeleteMapping("/v1/depts")
	@PreAuthorize("hasAuthority('sys:dept:remove')")
	@OperateLog(module = "部门管理", operation = "删除部门")
	@Operation(summary = "删除部门", description = "删除部门")
	public void removeDept(@RequestBody Long[] ids) {
		deptsServiceI.removeDept(new DeptRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/depts/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:dept:import')")
	@OperateLog(module = "部门管理", operation = "导入部门")
	@Operation(summary = "导入部门", description = "导入部门")
	public void importDept(@RequestPart("files") MultipartFile[] files) {
		deptsServiceI.importDept(new DeptImportCmd(files));
	}

	@PostMapping("/v1/depts/export")
	@PreAuthorize("hasAuthority('sys:dept:export')")
	@OperateLog(module = "部门管理", operation = "导出部门")
	@Operation(summary = "导出部门", description = "导出部门")
	public void exportDept(@RequestBody DeptExportCmd cmd) {
		deptsServiceI.exportDept(cmd);
	}

	@TraceLog
	@PostMapping("/v1/depts/page")
	@PreAuthorize("hasAuthority('sys:dept:page')")
	@Operation(summary = "分页查询部门列表", description = "分页查询部门列表")
	public Result<Page<DeptCO>> pageDept(@Validated @RequestBody DeptPageQry qry) {
		return deptsServiceI.pageDept(qry);
	}

	@TraceLog
	@PostMapping("/v1/depts/list-tree")
	@PreAuthorize("hasAuthority('sys:dept:list-tree')")
	@Operation(summary = "查询部门树列表", description = "查询部门树列表")
	public Result<List<DeptTreeCO>> listTreeDept(@RequestBody DeptTreeListQry qry) {
		return deptsServiceI.listTreeDept(qry);
	}

	@TraceLog
	@GetMapping("/v1/depts/{id}")
	@DistributedCache(name = NameConstants.DEPTS, key = "#id")
	@PreAuthorize("hasAuthority('sys:dept:detail')")
	@Operation(summary = "查看部门详情", description = "查看部门详情")
	public Result<DeptCO> getDeptById(@PathVariable("id") Long id) {
		return deptsServiceI.getDeptById(new DeptGetQry(id));
	}

}
