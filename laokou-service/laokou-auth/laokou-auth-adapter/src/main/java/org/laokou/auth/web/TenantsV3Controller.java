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
import org.laokou.auth.api.TenantsServiceI;
import org.laokou.auth.dto.TenantIDGetQry;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.i18n.dto.Option;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "租户管理", description = "租户管理")
@RequestMapping("v3/tenants")
public class TenantsV3Controller {

	private final TenantsServiceI tenantsServiceI;

	@TraceLog
	@GetMapping("options")
	@Operation(summary = "查询租户下拉选择项列表", description = "查询租户下拉选择项列表")
	public Result<List<Option>> listOptionV3() {
		return tenantsServiceI.listOption();
	}

	@TraceLog
	@GetMapping("id")
	@Operation(summary = "根据域名查看ID", description = "根据域名查看ID")
	public Result<Long> getIdByDomainNameV3(HttpServletRequest request) {
		return tenantsServiceI.getIdByDomainName(new TenantIDGetQry(RequestUtil.getDomainName(request)));
	}

}
