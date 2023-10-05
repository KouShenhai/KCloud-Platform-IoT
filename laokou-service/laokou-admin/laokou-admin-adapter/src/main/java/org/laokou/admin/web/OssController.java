/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
import org.laokou.admin.api.OssServiceI;
import org.laokou.admin.dto.oss.*;
import org.laokou.admin.dto.oss.clientobject.FileCO;
import org.laokou.admin.dto.oss.clientobject.OssCO;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.aspect.Type;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author laokou
 */
@RestController
@Tag(name = "OssController", description = "存储管理")
@RequiredArgsConstructor
@RequestMapping("v1/oss")
public class OssController {

	private final OssServiceI ossServiceI;

	@TraceLog
	@PostMapping("list")
	@Operation(summary = "存储管理", description = "查询存储列表")
	@PreAuthorize("hasAuthority('oss:list')")
	public Result<Datas<OssCO>> list(@RequestBody OssListQry qry) {
		return ossServiceI.list(qry);
	}

	@TraceLog
	@PostMapping("upload")
	@Operation(summary = "存储管理", description = "上传文件")
	@OperateLog(module = "存储管理", operation = "上传文件")
	public Result<FileCO> upload(@RequestPart("file") MultipartFile file) {
		return ossServiceI.upload(new OssUploadCmd(file));
	}

	@Idempotent
	@TraceLog
	@PostMapping
	@Operation(summary = "存储管理", description = "新增存储")
	@OperateLog(module = "存储管理", operation = "新增存储")
	@PreAuthorize("hasAuthority('oss:insert')")
	public Result<Boolean> insert(@RequestBody OssInsertCmd cmd) {
		return ossServiceI.insert(cmd);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "存储管理", description = "查看存储")
	@DataCache(name = "oss", key = "#id")
	public Result<OssCO> getById(@PathVariable("id") Long id) {
		return ossServiceI.getById(new OssGetQry(id));
	}

	@TraceLog
	@PutMapping
	@Operation(summary = "存储管理", description = "修改存储")
	@OperateLog(module = "存储管理", operation = "修改存储")
	@PreAuthorize("hasAuthority('oss:update')")
	@DataCache(name = "oss", key = "#cmd.ossCO.id", type = Type.DEL)
	public Result<Boolean> update(@RequestBody OssUpdateCmd cmd) {
		return ossServiceI.update(cmd);
	}

	@TraceLog
	@DeleteMapping("{id}")
	@Operation(summary = "存储管理", description = "删除存储")
	@OperateLog(module = "存储管理", operation = "删除存储")
	@PreAuthorize("hasAuthority('oss:delete')")
	@DataCache(name = "oss", key = "#id", type = Type.DEL)
	public Result<Boolean> deleteById(@PathVariable("id") Long id) {
		return ossServiceI.deleteById(new OssDeleteCmd(id));
	}

}
