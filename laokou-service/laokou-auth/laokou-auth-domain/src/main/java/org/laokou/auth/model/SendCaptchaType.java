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

package org.laokou.auth.model;

import lombok.Getter;
import org.laokou.common.i18n.utils.EnumParser;
import org.laokou.common.i18n.utils.RedisKeyUtil;

/**
 * @author laokou
 */
@Getter
public enum SendCaptchaType {

	// @formatter:off
	SEND_MAIL_CAPTCHA("sendMailCaptcha", "发送邮箱验证码") {
		@Override
		public String getCaptchaCacheKey(String uuid) {
			return RedisKeyUtil.getMailAuthCaptchaKey(uuid);
		}
	},

	SEND_MOBILE_CAPTCHA("sendMobileCaptcha", "发送手机号验证码") {
		@Override
		public String getCaptchaCacheKey(String uuid) {
			return RedisKeyUtil.getMobileAuthCaptchaKey(uuid);
		}
	};

	private final String code;

	private final String desc;

	SendCaptchaType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static SendCaptchaType getByCode(String code) {
		return EnumParser.parse(SendCaptchaType.class, SendCaptchaType::getCode, code);
	}

	public abstract String getCaptchaCacheKey(String uuid);
	// @formatter:on

}
