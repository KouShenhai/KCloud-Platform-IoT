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
package org.laokou.auth.server.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.client.vo.IdempotentToken;
import org.laokou.auth.client.vo.SecretInfoVO;
import org.laokou.auth.server.application.service.SysAuthApplicationService;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author laokou
 */
@RestController
@Tag(name = "OAuth2 API", description = "认证API")
@RequiredArgsConstructor
@RequestMapping("oauth2")
public class OAuth2ApiController {

	private final SysAuthApplicationService sysAuthApplicationService;

	@TraceLog
	@GetMapping("v1/captcha")
	@Operation(summary = "验证码", description = "验证码")
	public HttpResult<String> captcha(HttpServletRequest request) {
		return new HttpResult<String>().ok(sysAuthApplicationService.captcha(request));
	}

	@TraceLog
	@GetMapping("v1/logout")
	@Operation(summary = "注销", description = "注销")
	public HttpResult<Boolean> logout(HttpServletRequest request) {
		return new HttpResult<Boolean>().ok(sysAuthApplicationService.logout(request));
	}

	@TraceLog
	@GetMapping("v1/tenant_list")
	@Operation(summary = "列表", description = "列表")
	public HttpResult<List<OptionVO>> tenantList() {
		return new HttpResult<List<OptionVO>>().ok(sysAuthApplicationService.getOptionList());
	}

	@TraceLog
	@GetMapping("v1/secret_info")
	@Operation(summary = "密钥", description = "密钥")
	public HttpResult<SecretInfoVO> secretInfo() throws IOException {
		return new HttpResult<SecretInfoVO>().ok(sysAuthApplicationService.getSecretInfo());
	}

	@TraceLog
	@GetMapping("v1/idempotent_token")
	@Operation(summary = "令牌", description = "令牌")
	public HttpResult<IdempotentToken> idempotentToken() {
		return new HttpResult<IdempotentToken>().ok(sysAuthApplicationService.idempotentToken());
	}

}
