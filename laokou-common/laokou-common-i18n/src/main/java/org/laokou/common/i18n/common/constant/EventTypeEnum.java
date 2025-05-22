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

package org.laokou.common.i18n.common.constant;

import lombok.Getter;

/**
 * 事件类型枚举.
 *
 * @author laokou
 */
@Getter
public enum EventTypeEnum {

	SEND_CAPTCHA_EVENT("send_captcha_event", "发送验证码事件"),

	LOGIN_EVENT("login_event", "登录事件"),

	OPERATE_EVENT("operate_event", "操作事件"),

	REMOVE_CACHE_EVENT("remove_cache_event", "移除缓存事件");

	private final String code;

	private final String desc;

	EventTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
