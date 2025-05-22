// @formatter:off
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

package ${packageName}.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Result;
import org.springframework.http.MediaType;
import ${packageName}.${instanceName}.dto.clientobject.${className}CO;
import org.springframework.web.bind.annotation.*;
import ${packageName}.${instanceName}.api.${className}sServiceI;
import ${packageName}.${instanceName}.dto.*;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import static org.laokou.common.data.cache.constant.NameConstant.${(className)?upper_case}S;
import static org.laokou.common.data.cache.constant.OperateTypeEnum.DEL;

/**
 *
 * ${comment}管理控制器.
 *
 * @author ${author}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${version}/${instanceName}s")
@Tag(name = "${comment}管理", description = "${comment}管理")
public class ${className}sController${(version)?upper_case} {

	private final ${className}sServiceI ${instanceName}sServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('${app?lower_case}:${instanceName}:save')")
	@OperateLog(module = "${comment}管理", operation = "保存${comment}")
	@Operation(summary = "保存${comment}", description = "保存${comment}")
	public void save${(version)?upper_case}(@RequestBody ${className}SaveCmd cmd) {
		${instanceName}sServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('${app?lower_case}:${instanceName}:modify')")
	@OperateLog(module = "${comment}管理", operation = "修改${comment}")
	@Operation(summary = "修改${comment}", description = "修改${comment}")
	@DataCache(name = ${(className)?upper_case}S, key = "#cmd.co.id", operateType = DEL)
	public void modify${(version)?upper_case}(@RequestBody ${className}ModifyCmd cmd) {
		${instanceName}sServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('${app?lower_case}:${instanceName}:remove')")
	@OperateLog(module = "${comment}管理", operation = "删除${comment}")
	@Operation(summary = "删除${comment}", description = "删除${comment}")
	public void remove${(version)?upper_case}(@RequestBody Long[] ids) {
		${instanceName}sServiceI.remove(new ${className}RemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('${app?lower_case}:${instanceName}:import')")
	@OperateLog(module = "${comment}管理", operation = "导入${comment}")
	@Operation(summary = "导入${comment}", description = "导入${comment}")
	public void import${(version)?upper_case}(@RequestPart("files") MultipartFile[] files) {
		${instanceName}sServiceI.importI(new ${className}ImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('${app?lower_case}:${instanceName}:export')")
	@OperateLog(module = "${comment}管理", operation = "导出${comment}")
	@Operation(summary = "导出${comment}", description = "导出${comment}")
	public void export${(version)?upper_case}(@RequestBody ${className}ExportCmd cmd) {
		${instanceName}sServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('${app?lower_case}:${instanceName}:page')")
	@Operation(summary = "分页查询${comment}列表", description = "分页查询${comment}列表")
	public Result<Page<${className}CO>> page${(version)?upper_case}(@RequestBody ${className}PageQry qry) {
		return ${instanceName}sServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@DataCache(name = ${(className)?upper_case}S, key = "#id")
	@Operation(summary = "查看${comment}详情", description = "查看${comment}详情")
	public Result<${className}CO> getById${(version)?upper_case}(@PathVariable("id") Long id) {
		return ${instanceName}sServiceI.getById(new ${className}GetQry(id));
	}

}
// @formatter:on
