/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.model.enums;

import lombok.Getter;

/**
 * mq枚举.
 *
 * @author laokou
 */
@Getter
public enum MqTopic {

	LOGIN_LOG("login_log", "登录日志") {
		@Override
		public String getTopic() {
			return LOGIN_LOG_TOPIC;
		}

	},
	MAIL_CAPTCHA("mail_captcha", "邮箱验证码") {
		@Override
		public String getTopic() {
			return MAIL_CAPTCHA_TOPIC;
		}

	},
	MOBILE_CAPTCHA("mobile_captcha", "手机验证码") {
		@Override
		public String getTopic() {
			return MOBILE_CAPTCHA_TOPIC;
		}
	};

	private final String code;

	private final String desc;

	MqTopic(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract String getTopic();

	public static final String MAIL_CAPTCHA_TOPIC = "mail-captcha";

	public static final String MOBILE_CAPTCHA_TOPIC = "mobile-captcha";

	public static final String LOGIN_LOG_TOPIC = "login-log";

	public static final String MAIL_CAPTCHA_CONSUMER_GROUP = "mail-captcha-consumer-group";

	public static final String MOBILE_CAPTCHA_CONSUMER_GROUP = "mobile-captcha-consumer-group";

	public static final String LOGIN_LOG_CONSUMER_GROUP = "login-log-consumer-group";

}
