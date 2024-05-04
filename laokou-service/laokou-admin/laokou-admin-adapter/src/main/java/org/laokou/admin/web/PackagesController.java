/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.api.PackagesServiceI;
import org.laokou.common.i18n.dto.Option;
import org.laokou.admin.dto.packages.*;
import org.laokou.admin.dto.packages.clientobject.PackageCO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.constant.TypeEnum;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.laokou.common.data.cache.constant.NameConstant.PACKAGES;

/**
 * @author laokou
 */
@RestController
@Tag(name = "PackagesController", description = "套餐管理")
@RequiredArgsConstructor
@RequestMapping("v1/packages")
public class PackagesController {

	private final PackagesServiceI packagesServiceI;

	@TraceLog
	@PostMapping("list")
	@Operation(summary = "套餐管理", description = "查询套餐列表")
	@PreAuthorize("hasAuthority('packages:list')")
	public Result<Datas<PackageCO>> findList(@RequestBody PackageListQry qry) {
		return packagesServiceI.findList(qry);
	}

	@Idempotent
	@PostMapping
	@Operation(summary = "套餐管理", description = "新增套餐")
	@OperateLog(module = "套餐管理", operation = "新增套餐")
	@PreAuthorize("hasAuthority('packages:create')")
	public void create(@RequestBody PackageCreateCmd cmd) {
		packagesServiceI.create(cmd);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "套餐管理", description = "查看套餐")
	@DataCache(name = PACKAGES, key = "#id")
	public Result<PackageCO> findById(@PathVariable("id") Long id) {
		return packagesServiceI.findById(new PackageGetQry(id));
	}

	@PutMapping
	@Operation(summary = "套餐管理", description = "修改套餐")
	@OperateLog(module = "套餐管理", operation = "修改套餐")
	@PreAuthorize("hasAuthority('packages:modify')")
	@DataCache(name = PACKAGES, key = "#cmd.packageCO.id", type = TypeEnum.DEL)
	public void modify(@RequestBody PackageModifyCmd cmd) {
		packagesServiceI.modify(cmd);
	}

	@DeleteMapping
	@Operation(summary = "套餐管理", description = "删除套餐")
	@OperateLog(module = "套餐管理", operation = "删除套餐")
	@PreAuthorize("hasAuthority('packages:remove')")
	public void remove(@RequestBody Long[] ids) {
		packagesServiceI.remove(new PackageRemoveCmd(ids));
	}

	@TraceLog
	@GetMapping("option-list")
	@Operation(summary = "套餐管理", description = "下拉列表")
	public Result<List<Option>> findOptionList() {
		return packagesServiceI.findOptionList();
	}

}
