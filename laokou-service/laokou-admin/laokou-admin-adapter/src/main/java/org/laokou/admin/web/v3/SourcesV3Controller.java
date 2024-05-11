/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.web.v3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.source.*;
import org.laokou.admin.dto.source.clientobject.SourceCO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.constant.TypeEnum;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Option;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.laokou.common.data.cache.constant.NameConstant.SOURCES;
import static org.laokou.common.data.cache.constant.TypeEnum.DEL;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/sources")
@Tag(name = "SourcesV3Controller", description = "数据源管理")
public class SourcesV3Controller {

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('source:page')")
	@Operation(summary = "数据源管理", description = "查询数据源列表")
	public Result<Datas<SourceCO>> pageV3() {
		return null;
	}

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('source:save')")
	@OperateLog(module = "数据源管理", operation = "数据源新增")
	@Operation(summary = "数据源管理", description = "新增数据源")
	public void saveV3() {
	}

	@TraceLog
	@GetMapping("{id}")
	@DataCache(name = SOURCES, key = "#id")
	@Operation(summary = "数据源管理", description = "查看数据源详情")
	public Result<?> getByIdV3(@PathVariable("id") Long id) {
		return null;
	}

	@PutMapping
	@PreAuthorize("hasAuthority('source:modify')")
	@OperateLog(module = "数据源管理", operation = "修改数据源")
	@Operation(summary = "数据源管理", description = "修改数据源")
	@DataCache(name = SOURCES, key = "#cmd.sourceCO.id", type = DEL)
	public void modifyV3() {

	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('source:remove')")
	@OperateLog(module = "数据源管理", operation = "删除数据源")
	@Operation(summary = "数据源管理", description = "删除数据源")
	public void removeV3(@RequestBody Long[] ids) {
	}

	@TraceLog
	@GetMapping("options")
	@Operation(summary = "数据源管理", description = "查询数据源下拉选择项列表")
	public Result<List<Option>> listOptionV3() {
		return null;
	}

}
