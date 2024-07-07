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

package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.gateway.CaptchaGateway;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.redis.utils.RedisUtil.MINUTE_FIVE_EXPIRE;

/**
 * 验证码.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CaptchaGatewayImpl implements CaptchaGateway {

	private final RedisUtil redisUtil;

	/**
	 * 写入Redis.
	 * @param uuid UUID
	 * @param code 验证码
	 */
	@Override
	public void setValue(String uuid, String code) {
		String key = getKey(uuid);
		// 保存五分钟
		redisUtil.del(key);
		redisUtil.set(key, code, MINUTE_FIVE_EXPIRE);
	}

	/**
	 * 检查验证码.
	 * @param uuid UUID
	 * @param code 验证码
	 * @return 校验结果
	 */
	@Override
	public Boolean checkValue(String uuid, String code) {
		// 获取验证码
		String captcha = getValue(uuid);
		if (StringUtil.isEmpty(captcha)) {
			return null;
		}
		return code.equalsIgnoreCase(captcha);
	}

	/**
	 * 获取key（MD5加密）.
	 * @param uuid UUID
	 * @return key
	 */
	@Override
	public String getKey(String uuid) {
		String key = RedisKeyUtil.getUserCaptchaKey(uuid);
		return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 从Redis根据UUID查看验证码.
	 * @param uuid UUID
	 * @return 验证码
	 */
	private String getValue(String uuid) {
		String key = getKey(uuid);
		Object captcha = redisUtil.get(key);
		if (ObjectUtil.isNotNull(captcha)) {
			redisUtil.del(key);
		}
		return ObjectUtil.isNotNull(captcha) ? captcha.toString() : EMPTY;
	}

}
