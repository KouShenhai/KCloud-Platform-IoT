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
import org.laokou.admin.client.dto.common.clientobject.OptionCO;
import org.laokou.admin.domain.annotation.OperateLog;
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
@Tag(name = "DictsController", description = "字典")
@RequiredArgsConstructor
public class DictsController {

	@PostMapping(value = "v1/dicts/list")
	@TraceLog
	@Operation(summary = "查询", description = "查询")
	@PreAuthorize("hasAuthority('dicts:list')")
	public Result<?> list() {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/dicts/option-list/{type}")
	@Operation(summary = "下拉列表", description = "下拉列表")
	public Result<List<OptionCO>> optionList(@PathVariable("type") String type) {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping(value = "v1/dicts/{id}")
	@Operation(summary = "查看", description = "查看")
	@DataCache(name = "dicts", key = "#id")
	public Result<?> detail(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping(value = "v1/dicts")
	@Operation(summary = "新增", description = "新增")
	@OperateLog(module = "字典管理", operation = "新增")
	@PreAuthorize("hasAuthority('dicts:insert')")
	public Result<Boolean> insert() {
		return Result.of(null);
	}

	@TraceLog
	@PutMapping(value = "v1/dicts")
	@Operation(summary = "修改", description = "修改")
	@OperateLog(module = "字典管理", operation = "修改")
	@PreAuthorize("hasAuthority('dicts:update')")
	@DataCache(name = "dicts", key = "#dto.id", type = Cache.DEL)
	public Result<Boolean> update() {
		return Result.of(null);
	}

	@TraceLog
	@DeleteMapping(value = "v1/dicts/{id}")
	@Operation(summary = "删除", description = "删除")
	@OperateLog(module = "字典管理", operation = "删除")
	@PreAuthorize("hasAuthority('dicts:delete')")
	@DataCache(name = "dicts", key = "#id", type = Cache.DEL)
	public Result<Boolean> delete(@PathVariable("id") Long id) {
		return Result.of(null);
	}

}
