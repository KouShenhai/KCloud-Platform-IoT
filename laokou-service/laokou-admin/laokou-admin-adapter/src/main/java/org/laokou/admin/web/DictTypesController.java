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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.DictsServiceI;
import org.laokou.admin.dto.dict.DictListQry;
import org.laokou.admin.dto.dict.clientobject.DictTypeCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laokou
 */
@RestController
@Tag(name = "DictTypesController", description = "字典类型管理")
@RequiredArgsConstructor
public class DictTypesController {

	private final DictsServiceI dictsServiceI;

	@TraceLog
	@PostMapping("v1/dict-types/page")
	// @PreAuthorize("hasAuthority('dict-type:page')")
	@Operation(summary = "字典类型管理", description = "分页查询字典列表")
	public Result<Datas<DictTypeCO>> pageV1(@Validated @RequestBody DictListQry qry) {
		return dictsServiceI.page(qry);
	}
	/*
	 * @TraceLog
	 *
	 * @GetMapping("{type}/option-list")
	 *
	 * @Operation(summary = "字典管理", description = "下拉列表") public Result<List<Option>>
	 * findOptionList(@PathVariable("type") String type) { return
	 * dictsServiceI.optionList(new DictOptionListQry(type)); }
	 *
	 * @TraceLog
	 *
	 * @GetMapping("{id}")
	 *
	 * @Operation(summary = "字典管理", description = "查看字典")
	 *
	 * @DataCache(name = DICTS, key = "#id") public Result<DictCO>
	 * findById(@PathVariable("id") Long id) { return dictsServiceI.findById(new
	 * DictGetQry(id)); }
	 *
	 * @Idempotent
	 *
	 * @PostMapping
	 *
	 * @Operation(summary = "字典管理", description = "新增字典")
	 *
	 * @OperateLog(module = "字典管理", operation = "新增字典")
	 *
	 * @PreAuthorize("hasAuthority('dicts:create')") public void create(@RequestBody
	 * DictCreateCmd cmd) { dictsServiceI.create(cmd); }
	 *
	 * @PutMapping
	 *
	 * @Operation(summary = "字典管理", description = "修改字典")
	 *
	 * @OperateLog(module = "字典管理", operation = "修改字典")
	 *
	 * @PreAuthorize("hasAuthority('dicts:modify')")
	 *
	 * @DataCache(name = DICTS, key = "#cmd.dictCO.id", type = TypeEnum.DEL) public void
	 * modify(@RequestBody DictModifyCmd cmd) { dictsServiceI.modify(cmd); }
	 *
	 * @DeleteMapping
	 *
	 * @Operation(summary = "字典管理", description = "删除字典")
	 *
	 * @OperateLog(module = "字典管理", operation = "删除字典")
	 *
	 * @PreAuthorize("hasAuthority('dicts:remove')") public void remove(@RequestBody
	 * Long[] ids) { dictsServiceI.remove(new DictRemoveCmd(ids)); }
	 */

}
