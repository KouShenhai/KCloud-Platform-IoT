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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.tenant.clientobject.TenantCO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.ratelimiter.annotation.RateLimiter;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.laokou.common.data.cache.constant.NameConstant.TENANTS;
import static org.laokou.common.data.cache.constant.Type.DEL;
import static org.laokou.common.ratelimiter.aop.Type.IP;
import static org.redisson.api.RateIntervalUnit.MINUTES;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/tenants")
@Tag(name = "TenantsV3Controller", description = "租户管理")
public class TenantsV3Controller {

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('tenant:page')")
	@Operation(summary = "租户管理", description = "分页查询租户列表")
	public Result<Page<TenantCO>> pageV3() {
		return null;
	}

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('tenant:save')")
	@OperateLog(module = "租户管理", operation = "新增租户")
	@Operation(summary = "租户管理", description = "新增租户")
	public void saveV3() {
	}

	@TraceLog
	@GetMapping("{id}")
	@DataCache(name = TENANTS, key = "#id")
	@Operation(summary = "租户管理", description = "查看租户详情")
	public Result<TenantCO> getByIdV3(@PathVariable("id") Long id) {
		return null;
	}

	@PutMapping
	@PreAuthorize("hasAuthority('tenant:modify')")
	@OperateLog(module = "租户管理", operation = "修改租户")
	@Operation(summary = "租户管理", description = "修改租户")
	@DataCache(name = TENANTS, key = "#cmd.co.id", type = DEL)
	public void modifyV3() {

	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('tenant:remove')")
	@OperateLog(module = "租户管理", operation = "删除租户")
	@Operation(summary = "租户管理", description = "删除租户")
	public void removeV3(@RequestBody Long[] ids) {

	}

	@GetMapping("download-ds/{id}")
	@PreAuthorize("hasAuthority('tenant:download-ds')")
	@OperateLog(module = "租户管理", operation = "下载数据库")
	@Operation(summary = "租户管理", description = "下载数据库")
	@RateLimiter(key = "DOWNLOAD_TENANT_DS", rate = 5, interval = 10, unit = MINUTES, type = IP)
	public void downloadDSV3(@PathVariable("id") Long id, HttpServletResponse response) {

	}

}
