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
import org.laokou.auth.api.CaptchasServiceI;
import org.laokou.auth.dto.CaptchaGetQry;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.ratelimiter.annotation.RateLimiter;
import org.laokou.common.trace.annotation.TraceLog;
import org.redisson.api.RateIntervalUnit;
import org.springframework.web.bind.annotation.*;

import static org.laokou.common.ratelimiter.driver.spi.TypeEnum.IP;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/captchas")
@Tag(name = "CaptchasV3Controller", description = "验证码")
public class CaptchasV3Controller {

	private final CaptchasServiceI captchasServiceI;

	@TraceLog
	@GetMapping("{uuid}")
	@RateLimiter(id = "GET_CAPTCHA", type = IP, unit = RateIntervalUnit.MINUTES, interval = 30, rate = 100)
	@Operation(summary = "验证码-根据UUID获取验证码", description = "验证码-根据UUID获取验证码")
	public Result<String> getByUuidV3(@PathVariable("uuid") String uuid) {
		return captchasServiceI.getByUuid(new CaptchaGetQry(uuid));
	}
	
	@PostMapping("{type}/{uuid}")
	@RateLimiter(id = "SEND_CAPTCHA", type = IP, unit = RateIntervalUnit.MINUTES)
	@Operation(summary = "验证码-根据UUID发送验证码", description = "验证码-根据UUID发送验证码")
	public void sendByUuidV3(@PathVariable String type, @PathVariable String uuid) {

	}

}
