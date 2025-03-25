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

package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.gateway.CaptchaGateway;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;
import static org.laokou.common.redis.util.RedisUtils.FIVE_MINUTE_EXPIRE;

/**
 * 验证码.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CaptchaGatewayImpl implements CaptchaGateway {

	private final RedisUtils redisUtils;

	/**
	 * 写入Redis.
	 * @param key 键
	 * @param captcha 验证码
	 */
	@Override
	public void set(String key, String captcha) {
		set(key, captcha, FIVE_MINUTE_EXPIRE);
	}

	/**
	 * 写入Redis.
	 * @param key 键
	 * @param captcha 验证码
	 */
	@Override
	public void set(String key, String captcha, long expireTime) {
		redisUtils.del(key);
		redisUtils.set(key, captcha, expireTime);
	}

	/**
	 * 检查验证码.
	 * @param key 键
	 * @param code 验证码
	 * @return 校验结果
	 */
	@Override
	public Boolean validate(String key, String code) {
		// 获取验证码
		String captcha = getValue(key);
		if (StringUtils.isEmpty(captcha)) {
			return null;
		}
		return code.equalsIgnoreCase(captcha);
	}

	/**
	 * 从Redis根据UUID查看验证码.
	 * @param key 键
	 * @return 验证码
	 */
	private String getValue(String key) {
		Object captcha = redisUtils.get(key);
		if (ObjectUtils.isNotNull(captcha)) {
			redisUtils.del(key);
		}
		return ObjectUtils.isNotNull(captcha) ? captcha.toString() : EMPTY;
	}

}
