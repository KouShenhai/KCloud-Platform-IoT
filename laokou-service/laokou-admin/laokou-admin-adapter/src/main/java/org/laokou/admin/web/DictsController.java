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
import org.laokou.admin.api.DictsServiceI;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.dict.*;
import org.laokou.admin.dto.dict.clientobject.DictCO;
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
@Tag(name = "DictsController", description = "字典管理")
@RequiredArgsConstructor
@RequestMapping("v1/dicts")
public class DictsController {

	private final DictsServiceI dictsServiceI;

	@PostMapping(value = "list")
	@TraceLog
	@Operation(summary = "字典管理", description = "查询字典列表")
	@PreAuthorize("hasAuthority('dicts:list')")
	public Result<Datas<DictCO>> list(@RequestBody DictListQry qry) {
		return dictsServiceI.list(qry);
	}

	@TraceLog
	@GetMapping("{type}/option-list")
	@Operation(summary = "字典管理", description = "下拉列表")
	public Result<List<OptionCO>> optionList(@PathVariable("type") String type) {
		return dictsServiceI.optionList(new DictOptionListQry(type));
	}

	@TraceLog
	@GetMapping(value = "{id}")
	@Operation(summary = "字典管理", description = "查看字典")
	@DataCache(name = "dicts", key = "#id")
	public Result<DictCO> getById(@PathVariable("id") Long id) {
		return dictsServiceI.getById(new DictGetQry(id));
	}

	@Idempotent
	@TraceLog
	@PostMapping
	@Operation(summary = "字典管理", description = "新增字典")
	@OperateLog(module = "字典管理", operation = "新增字典")
	@PreAuthorize("hasAuthority('dicts:insert')")
	public Result<Boolean> insert(@RequestBody DictInsertCmd cmd) {
		return dictsServiceI.insert(cmd);
	}

	@TraceLog
	@PutMapping
	@Operation(summary = "字典管理", description = "修改字典")
	@OperateLog(module = "字典管理", operation = "修改字典")
	@PreAuthorize("hasAuthority('dicts:update')")
	@DataCache(name = "dicts", key = "#cmd.dictCO.id", type = Type.DEL)
	public Result<Boolean> update(@RequestBody DictUpdateCmd cmd) {
		return dictsServiceI.update(cmd);
	}

	@TraceLog
	@DeleteMapping(value = "{id}")
	@Operation(summary = "字典管理", description = "删除字典")
	@OperateLog(module = "字典管理", operation = "删除字典")
	@PreAuthorize("hasAuthority('dicts:delete')")
	@DataCache(name = "dicts", key = "#id", type = Type.DEL)
	public Result<Boolean> deleteById(@PathVariable("id") Long id) {
		return dictsServiceI.deleteById(new DictDeleteCmd(id));
	}

}
