/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.auth.dto.CaptchaSendCmd;
import org.laokou.auth.model.MqEnum;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.ratelimiter.annotation.RateLimiter;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.web.bind.annotation.*;

import static org.laokou.common.ratelimiter.aop.Type.IP;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/captchas")
@Tag(name = "验证码", description = "验证码")
public class CaptchasV3Controller {

	private final CaptchasServiceI captchasServiceI;

	@TraceLog
	@GetMapping("{uuid}")
	@RateLimiter(key = "GET_CAPTCHA", type = IP)
	@Operation(summary = "根据UUID获取验证码", description = "根据UUID获取验证码")
	public Result<String> getByUuidCaptcha(@PathVariable("uuid") String uuid) {
		return captchasServiceI.getByUuidCaptcha(new CaptchaGetQry(uuid));
	}

	@Idempotent
	@PostMapping("send/mobile")
	@RateLimiter(key = "SEND_MOBILE_CAPTCHA", type = IP)
	@Operation(summary = "根据UUID发送手机验证码", description = "根据UUID发送手机验证码")
	public void sendMobileByUuidCaptcha(@RequestBody CaptchaSendCmd cmd) {
		cmd.getCo().setTag(MqEnum.MOBILE_CAPTCHA.getTag());
		captchasServiceI.sendByUuidCaptcha(cmd);
	}

	@Idempotent
	@PostMapping("send/mail")
	@RateLimiter(key = "SEND_MAIL_CAPTCHA", type = IP)
	@Operation(summary = "根据UUID发送邮箱验证码", description = "根据UUID发送邮箱验证码")
	public void sendMailByUuidCaptcha(@RequestBody CaptchaSendCmd cmd) {
		cmd.getCo().setTag(MqEnum.MAIL_CAPTCHA.getTag());
		captchasServiceI.sendByUuidCaptcha(cmd);
	}

}
