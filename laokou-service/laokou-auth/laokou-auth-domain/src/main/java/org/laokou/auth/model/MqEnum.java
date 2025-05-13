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

/**
 * mq枚举.
 *
 * @author laokou
 */
@Getter
public enum MqEnum {

	LOGIN_LOG("loginLog", "登录日志") {
		@Override
		public String getTopic() {
			return "laokou_log_topic";
		}

		@Override
		public String getTag() {
			return "loginLog";
		}

	},
	MAIL_CAPTCHA("mailCaptcha", "邮箱验证码") {
		@Override
		public String getTopic() {
			return "laokou_captcha_topic";
		}

		@Override
		public String getTag() {
			return "mailCaptcha";
		}

	},
	MOBILE_CAPTCHA("mobileCaptcha", "手机验证码") {
		@Override
		public String getTopic() {
			return "laokou_captcha_topic";
		}

		@Override
		public String getTag() {
			return "mobileCaptcha";
		}
	};

	private final String code;

	private final String desc;

	MqEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract String getTopic();

	public abstract String getTag();

}
