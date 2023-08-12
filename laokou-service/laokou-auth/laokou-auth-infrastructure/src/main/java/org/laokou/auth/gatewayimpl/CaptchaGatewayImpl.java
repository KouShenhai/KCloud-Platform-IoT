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
package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.domain.gateway.CaptchaGateway;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class CaptchaGatewayImpl implements CaptchaGateway {

	private final RedisUtil redisUtil;

	@Override
	public void set(String uuid, String code) {
		setValue(uuid, code);
	}

	@Override
	public Boolean validate(String uuid, String code) {
		// 获取验证码
		String captcha = getCaptcha(uuid);
		if (StringUtil.isEmpty(captcha)) {
			return null;
		}
		return code.equalsIgnoreCase(captcha);
	}

	private String getCaptcha(String uuid) {
		String key = RedisKeyUtil.getUserCaptchaKey(uuid);
		Object captcha = redisUtil.get(key);
		if (captcha != null) {
			redisUtil.delete(key);
		}
		return captcha != null ? captcha.toString() : "";
	}

	private void setValue(String uuid, String code) {
		String key = RedisKeyUtil.getUserCaptchaKey(uuid);
		// 保存五分钟
		redisUtil.set(key, code, 60 * 5);
	}

}
