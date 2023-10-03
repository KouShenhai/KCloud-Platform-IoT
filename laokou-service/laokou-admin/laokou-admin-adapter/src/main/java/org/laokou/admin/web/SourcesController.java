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
import org.laokou.admin.api.SourcesServiceI;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.source.*;
import org.laokou.admin.dto.source.clientobject.SourceCO;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.aspect.Type;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author laokou
 */
@RestController
@Tag(name = "SourcesController", description = "数据源管理")
@RequiredArgsConstructor
@RequestMapping("v1/sources")
public class SourcesController {

	private final SourcesServiceI sourcesServiceI;

	@TraceLog
	@PostMapping("list")
	@Operation(summary = "数据源管理", description = "查询数据源列表")
	@PreAuthorize("hasAuthority('sources:list')")
	public Result<Datas<SourceCO>> list(@RequestBody SourceListQry qry) {
		return sourcesServiceI.list(qry);
	}

	@Idempotent
	@TraceLog
	@PostMapping
	@Operation(summary = "数据源管理", description = "新增数据源")
	@OperateLog(module = "数据源管理", operation = "数据源新增")
	@PreAuthorize("hasAuthority('sources:insert')")
	public Result<Boolean> insert(@RequestBody SourceInsertCmd cmd) {
		return sourcesServiceI.insert(cmd);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "数据源管理", description = "查看数据源")
	@DataCache(name = "sources", key = "#id")
	public Result<SourceCO> getById(@PathVariable("id") Long id) {
		return sourcesServiceI.getById(new SourceGetQry(id));
	}

	@TraceLog
	@PutMapping
	@Operation(summary = "数据源管理", description = "修改数据源")
	@OperateLog(module = "数据源管理", operation = "修改数据源")
	@PreAuthorize("hasAuthority('sources:update')")
	@DataCache(name = "sources", key = "#cmd.sourceCO.id", type = Type.DEL)
	public Result<Boolean> update(@RequestBody SourceUpdateCmd cmd) {
		return sourcesServiceI.update(cmd);
	}

	@TraceLog
	@DeleteMapping("{id}")
	@Operation(summary = "数据源管理", description = "删除数据源")
	@OperateLog(module = "数据源管理", operation = "删除数据源")
	@PreAuthorize("hasAuthority('sources:delete')")
	@DataCache(name = "sources", key = "#id", type = Type.DEL)
	public Result<Boolean> deleteById(@PathVariable("id") Long id) {
		return sourcesServiceI.deleteById(new SourceDeleteCmd(id));
	}

	@TraceLog
	@GetMapping("option-list")
	@Operation(summary = "数据源管理", description = "下拉列表")
	public Result<List<OptionCO>> optionList() {
		return sourcesServiceI.optionList(new SourceOptionListQry());
	}

}
