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
import org.laokou.admin.api.TenantsServiceI;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.tenant.*;
import org.laokou.admin.dto.tenant.clientobject.TenantCO;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.enums.Cache;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author laokou
 */
@RestController
@Tag(name = "TenantsController", description = "租户管理")
@RequiredArgsConstructor
public class TenantsController {

	private final TenantsServiceI tenantsServiceI;

	@TraceLog
	@PostMapping("v1/tenants/list")
	@Operation(summary = "租户管理", description = "查询租户列表")
	@PreAuthorize("hasAuthority('tenants:list')")
	public Result<Datas<TenantCO>> list(@RequestBody TenantListQry qry) {
		return tenantsServiceI.list(qry);
	}

	@TraceLog
	@PostMapping("v1/tenants")
	@Operation(summary = "租户管理", description = "新增租户")
	@OperateLog(module = "租户管理", operation = "新增租户")
	@PreAuthorize("hasAuthority('tenants:insert')")
	public Result<Boolean> insert(@RequestBody TenantInsertCmd cmd) {
		return tenantsServiceI.insert(cmd);
	}

	@TraceLog
	@GetMapping("v1/tenants/{id}")
	@Operation(summary = "租户管理", description = "查看租户")
	@DataCache(name = "tenants", key = "#id")
	public Result<TenantCO> get(@PathVariable("id") Long id) {
		return tenantsServiceI.get(new TenantGetQry());
	}

	@TraceLog
	@PutMapping("v1/tenants")
	@Operation(summary = "租户管理", description = "修改租户")
	@OperateLog(module = "租户管理", operation = "修改租户")
	@PreAuthorize("hasAuthority('tenants:update')")
	@DataCache(name = "tenants", key = "#dto.id", type = Cache.DEL)
	public Result<Boolean> update(@RequestBody TenantUpdateCmd cmd) {
		return tenantsServiceI.update(cmd);
	}

	@TraceLog
	@DeleteMapping("v1/tenants/{id}")
	@Operation(summary = "租户管理", description = "删除租户")
	@OperateLog(module = "租户管理", operation = "删除租户")
	@PreAuthorize("hasAuthority('tenants:delete')")
	@DataCache(name = "tenants", key = "#id", type = Cache.DEL)
	public Result<Boolean> delete(@PathVariable("id") Long id) {
		return tenantsServiceI.delete(new TenantDeleteCmd(id));
	}

	@TraceLog
	@GetMapping("v1/tenants/option-list")
	@Operation(summary = "租户管理", description = "下拉列表")
	public Result<List<OptionCO>> optionList() {
		return tenantsServiceI.optionList(new TenantOptionListQry());
	}

}
