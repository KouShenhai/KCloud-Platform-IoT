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

package org.laokou.auth.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.dto.tenant.TenantGetIDQry;
import org.laokou.auth.api.TenantsServiceI;
import org.laokou.common.i18n.dto.Option;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "TenantsController", description = "租户管理")
public class TenantsController {

	private final TenantsServiceI tenantsServiceI;

	@TraceLog
	@GetMapping("v1/tenants/option-list")
	@Operation(summary = "租户管理", description = "租户下拉列表")
	public Result<List<Option>> listOptionV1() {
		return tenantsServiceI.listOption();
	}

	@TraceLog
	@GetMapping("v1/tenants/id")
	@Operation(summary = "租户管理", description = "根据域名查看ID")
	public Result<Long> getIdByDomainNameV1(HttpServletRequest request) {
		return tenantsServiceI.getIdByDomainName(new TenantGetIDQry(request));
	}

}
