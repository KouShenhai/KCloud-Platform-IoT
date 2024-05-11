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
import org.laokou.admin.dto.packages.*;
import org.laokou.admin.dto.packages.clientobject.PackageCO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Option;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.laokou.common.data.cache.constant.NameConstant.PACKAGES;
import static org.laokou.common.data.cache.constant.TypeEnum.DEL;

/**
 * @author laokou
 */
@RestController
@Tag(name = "PackagesV3Controller", description = "套餐管理")
@RequiredArgsConstructor
@RequestMapping("v3/packages")
public class PackagesV3Controller {

	@TraceLog
	@PostMapping("page")
	@Operation(summary = "套餐管理", description = "分页查询套餐列表")
	@PreAuthorize("hasAuthority('package:page')")
	public Result<Datas<PackageCO>> pageV3(@RequestBody PackageListQry qry) {
		return null;
	}

	@Idempotent
	@PostMapping
	@Operation(summary = "套餐管理", description = "新增套餐")
	@OperateLog(module = "套餐管理", operation = "新增套餐")
	@PreAuthorize("hasAuthority('package:save')")
	public void saveV3(@RequestBody PackageCreateCmd cmd) {
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "套餐管理", description = "查看套餐")
	@DataCache(name = PACKAGES, key = "#id")
	public Result<PackageCO> getByIdV3(@PathVariable("id") Long id) {
		return null;
	}

	@PutMapping
	@Operation(summary = "套餐管理", description = "修改套餐")
	@OperateLog(module = "套餐管理", operation = "修改套餐")
	@PreAuthorize("hasAuthority('package:modify')")
	@DataCache(name = PACKAGES, key = "#cmd.co.id", type = DEL)
	public void modifyV3(@RequestBody PackageModifyCmd cmd) {
	}

	@DeleteMapping
	@Operation(summary = "套餐管理", description = "删除套餐")
	@OperateLog(module = "套餐管理", operation = "删除套餐")
	@PreAuthorize("hasAuthority('package:remove')")
	public void removeV3(@RequestBody Long[] ids) {
	}

	@TraceLog
	@GetMapping("options")
	@Operation(summary = "套餐管理", description = "查询套餐下拉选择项列表")
	public Result<List<Option>> listOptionV3() {
		return null;
	}

}
