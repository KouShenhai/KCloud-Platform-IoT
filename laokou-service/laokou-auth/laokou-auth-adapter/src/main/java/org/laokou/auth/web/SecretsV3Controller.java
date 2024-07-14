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
import lombok.RequiredArgsConstructor;
import org.laokou.auth.api.SecretsServiceI;
import org.laokou.auth.dto.clientobject.SecretCO;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.ratelimiter.annotation.RateLimiter;
import org.laokou.common.trace.annotation.TraceLog;
import org.redisson.api.RateIntervalUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.laokou.common.ratelimiter.driver.spi.TypeEnum.IP;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "安全配置", description = "安全配置")
@RequestMapping("v3/secrets")
public class SecretsV3Controller {

	private final SecretsServiceI secretsServiceI;

	@TraceLog
	@GetMapping
	@Operation(summary = "获取密钥", description = "获取密钥")
	@RateLimiter(id = "AUTH_SECRET", type = IP, unit = RateIntervalUnit.MINUTES, interval = 30, rate = 100)
	public Result<SecretCO> getInfoV3() {
		return secretsServiceI.getInfo();
	}

}
