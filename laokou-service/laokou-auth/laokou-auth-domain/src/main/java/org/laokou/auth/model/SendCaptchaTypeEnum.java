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
import org.laokou.common.i18n.util.EnumParser;
import org.laokou.common.i18n.util.RedisKeyUtils;

import static org.laokou.auth.model.MqEnum.MAIL_CAPTCHA;
import static org.laokou.auth.model.MqEnum.MOBILE_CAPTCHA;

/**
 * @author laokou
 */
@Getter
public enum SendCaptchaTypeEnum {

	// @formatter:off
	SEND_MAIL_CAPTCHA("send_mail_captcha", "发送邮箱验证码") {
		@Override
		public String getCaptchaCacheKey(String uuid) {
			return RedisKeyUtils.getMailAuthCaptchaKey(uuid);
		}

		@Override public String getMqTopic() {
    		return MAIL_CAPTCHA.getTopic();
    	}
	},

	SEND_MOBILE_CAPTCHA("send_mobile_captcha", "发送手机号验证码") {
		@Override
		public String getCaptchaCacheKey(String uuid) {
			return RedisKeyUtils.getMobileAuthCaptchaKey(uuid);
		}

		@Override public String getMqTopic() {
    		return MOBILE_CAPTCHA.getTopic();
    	}
	};

	private final String code;

	private final String desc;

	SendCaptchaTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static SendCaptchaTypeEnum getByCode(String code) {
		return EnumParser.parse(SendCaptchaTypeEnum.class, SendCaptchaTypeEnum::getCode, code);
	}

	public abstract String getCaptchaCacheKey(String uuid);

	public abstract String getMqTopic();

	// @formatter:on

}
