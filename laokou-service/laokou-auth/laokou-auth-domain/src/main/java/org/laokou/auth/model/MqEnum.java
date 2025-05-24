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

	LOGIN_LOG("login_log", "登录日志") {
		@Override
		public String getTopic() {
			return LOG_TOPIC;
		}

		@Override
		public String getTag() {
			return LOGIN_LOGIN_TAG;
		}

	},
	MAIL_CAPTCHA("mail_captcha", "邮箱验证码") {
		@Override
		public String getTopic() {
			return CAPTCHA_TOPIC;
		}

		@Override
		public String getTag() {
			return MAIL_CAPTCHA_TAG;
		}

	},
	MOBILE_CAPTCHA("mobile_captcha", "手机验证码") {
		@Override
		public String getTopic() {
			return CAPTCHA_TOPIC;
		}

		@Override
		public String getTag() {
			return MOBILE_CAPTCHA_TAG;
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

	public static final String MAIL_CAPTCHA_TAG = "mail_captcha";

	public static final String MOBILE_CAPTCHA_TAG = "mobile_captcha";

	public static final String CAPTCHA_TOPIC = "laokou_captcha_topic";

	public static final String LOGIN_LOGIN_TAG = "login_log";

	public static final String LOG_TOPIC = "laokou_log_topic";

	public static final String MAIL_CAPTCHA_CONSUMER_GROUP = "laokou_mail_captcha_consumer_group";

	public static final String MOBILE_CAPTCHA_CONSUMER_GROUP = "laokou_mobile_captcha_consumer_group";

	public static final String LOGIN_LOG_CONSUMER_GROUP = "laokou_login_log_consumer_group";

}
