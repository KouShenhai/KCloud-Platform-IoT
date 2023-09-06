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
import org.laokou.admin.client.api.SourcesServiceI;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.enums.Cache;
import org.laokou.common.i18n.dto.Result;
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
public class SourcesController {

	private final SourcesServiceI sourcesServiceI;

	@TraceLog
	@PostMapping("v1/sources/list")
	@Operation(summary = "数据源管理", description = "查询数据源列表")
	@PreAuthorize("hasAuthority('sources:list')")
	public Result<?> list() {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping("v1/sources")
	@Operation(summary = "数据源管理", description = "新增数据源")
	@OperateLog(module = "数据源管理", operation = "数据源新增")
	@PreAuthorize("hasAuthority('sources:insert')")
	public Result<Boolean> insert() {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/sources/{id}")
	@Operation(summary = "数据源管理", description = "查看数据源")
	@DataCache(name = "sources", key = "#id")
	public Result<?> get(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@TraceLog
	@PutMapping("v1/sources")
	@Operation(summary = "数据源管理", description = "修改数据源")
	@OperateLog(module = "数据源管理", operation = "修改数据源")
	@PreAuthorize("hasAuthority('sources:update')")
	@DataCache(name = "sources", key = "#dto.id", type = Cache.DEL)
	public Result<Boolean> update() {
		return Result.of(null);
	}

	@TraceLog
	@DeleteMapping("v1/sources/{id}")
	@Operation(summary = "数据源管理", description = "删除数据源")
	@OperateLog(module = "数据源管理", operation = "删除数据源")
	@PreAuthorize("hasAuthority('sources:delete')")
	@DataCache(name = "sources", key = "#id", type = Cache.DEL)
	public Result<Boolean> delete(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/sources/option-list")
	@Operation(summary = "数据源管理", description = "下拉列表")
	public Result<List<OptionVO>> optionList() {
		return Result.of(null);
	}

}
