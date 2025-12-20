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
import ${packageName}.${moduleName}.api.${className}ServiceI;
import ${packageName}.${moduleName}.dto.${className}ExportCmd;
import ${packageName}.${moduleName}.dto.${className}GetQry;
import ${packageName}.${moduleName}.dto.${className}ImportCmd;
import ${packageName}.${moduleName}.dto.${className}ModifyCmd;
import ${packageName}.${moduleName}.dto.${className}PageQry;
import ${packageName}.${moduleName}.dto.${className}RemoveCmd;
import ${packageName}.${moduleName}.dto.${className}SaveCmd;
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
import ${packageName}.${moduleName}.dto.clientobject.${className}CO;

/**
 *
 * ${comment}管理控制器.
 *
 * @author ${author}
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "${comment}管理", description = "${comment}管理")
public class ${className}sController {

	private final ${className}sServiceI ${moduleName}sServiceI;

	@Idempotent
	@PostMapping("/v1/${moduleName}s")
	@PreAuthorize("hasAuthority(${dsName}:${moduleName}:save')")
	@OperateLog(module = "${comment}管理", operation = "保存${comment}")
	@Operation(summary = "保存${comment}", description = "保存${comment}")
	public void save${className}(@RequestBody ${className}SaveCmd cmd) {
		${moduleName}sServiceI.save(cmd);
	}

	@PutMapping("/v1/${moduleName}s")
	@PreAuthorize("hasAuthority(${dsName}:${moduleName}:modify')")
	@OperateLog(module = "${comment}管理", operation = "修改${comment}")
	@Operation(summary = "修改${comment}", description = "修改${comment}")
	@DistributedCache(name = ${(className)?upper_case}S, key = "#cmd.co.id", operateType = DEL)
	public void modify${className}(@RequestBody ${className}ModifyCmd cmd) {
		${moduleName}sServiceI.modify(cmd);
	}

	@DeleteMapping("/v1/${moduleName}s")
	@PreAuthorize("hasAuthority(${dsName}:${moduleName}:remove')")
	@OperateLog(module = "${comment}管理", operation = "删除${comment}")
	@Operation(summary = "删除${comment}", description = "删除${comment}")
	public void remove${className}(@RequestBody Long[] ids) {
		${moduleName}sServiceI.remove(new ${className}RemoveCmd(ids));
	}

	@PostMapping(value = "/v1/${moduleName}s/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority(${dsName}:${moduleName}:import')")
	@OperateLog(module = "${comment}管理", operation = "导入${comment}")
	@Operation(summary = "导入${comment}", description = "导入${comment}")
	public void import${className}(@RequestPart("files") MultipartFile[] files) {
		${moduleName}sServiceI.importI(new ${className}ImportCmd(files));
	}

	@PostMapping("/v1/${moduleName}s/export")
	@PreAuthorize("hasAuthority(${dsName}:${moduleName}:export')")
	@OperateLog(module = "${comment}管理", operation = "导出${comment}")
	@Operation(summary = "导出${comment}", description = "导出${comment}")
	public void export${className}(@RequestBody ${className}ExportCmd cmd) {
		${moduleName}sServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("/v1/${moduleName}s/page")
	@PreAuthorize("hasAuthority(${dsName}:${moduleName}:page')")
	@Operation(summary = "分页查询${comment}列表", description = "分页查询${comment}列表")
	public Result<Page<${className}CO>> page${className}(@RequestBody ${className}PageQry qry) {
		return ${moduleName}sServiceI.page${className}(qry);
	}

	@TraceLog
	@GetMapping("/v1/${moduleName}s/{id}")
	@DistributedCache(name = ${(className)?upper_case}S, key = "#id")
	@Operation(summary = "查看${comment}详情", description = "查看${comment}详情")
	public Result<${className}CO> get${className}ById(@PathVariable("id") Long id) {
		return ${moduleName}sServiceI.get${className}ById(new ${className}GetQry(id));
	}

}
// @formatter:on
