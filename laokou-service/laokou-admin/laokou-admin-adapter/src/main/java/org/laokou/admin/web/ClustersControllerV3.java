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
import org.laokou.common.i18n.dto.Result;
import org.springframework.http.MediaType;
import org.laokou.admin.cluster.dto.clientobject.ClusterCO;
import org.springframework.web.bind.annotation.*;
import org.laokou.admin.cluster.api.ClustersServiceI;
import org.laokou.admin.cluster.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 集群管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/clusters")
@Tag(name = "集群管理", description = "集群管理")
public class ClustersControllerV3 {

	private final ClustersServiceI clustersServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:cluster:save')")
	@OperateLog(module = "集群管理", operation = "保存集群")
	@Operation(summary = "保存集群", description = "保存集群")
	public void saveV3(@RequestBody ClusterSaveCmd cmd) {
		clustersServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:cluster:modify')")
	@OperateLog(module = "集群管理", operation = "修改集群")
	@Operation(summary = "修改集群", description = "修改集群")
	public void modifyV3(@RequestBody ClusterModifyCmd cmd) {
		clustersServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:cluster:remove')")
	@OperateLog(module = "集群管理", operation = "删除集群")
	@Operation(summary = "删除集群", description = "删除集群")
	public void removeV3(@RequestBody Long[] ids) {
		clustersServiceI.remove(new ClusterRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:cluster:import')")
	@OperateLog(module = "集群管理", operation = "导入集群")
	@Operation(summary = "导入集群", description = "导入集群")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		clustersServiceI.importI(new ClusterImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:cluster:export')")
	@OperateLog(module = "集群管理", operation = "导出集群")
	@Operation(summary = "导出集群", description = "导出集群")
	public void exportV3(@RequestBody ClusterExportCmd cmd) {
		clustersServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:cluster:page')")
	@Operation(summary = "分页查询集群列表", description = "分页查询集群列表")
	public Result<Page<ClusterCO>> pageV3(@RequestBody ClusterPageQry qry) {
		return clustersServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('sys:cluster:detail')")
	@Operation(summary = "查看集群详情", description = "查看集群详情")
	public Result<ClusterCO> getByIdV3(@PathVariable("id") Long id) {
		return clustersServiceI.getById(new ClusterGetQry(id));
	}

}
