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

package org.laokou.common.secret.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author laokou
 */
@Component
public class SecretUtil {

	@Schema(name = "APP_KEY", description = "应用Key")
	public static final String APP_KEY = "laokou2023";

	@Schema(name = "APP_SECRET", description = "应用密钥")
	public static final String APP_SECRET = "vb05f6c45d67340zaz95v7fa6d49v99zx";

	private static final long TIMEOUT_MILLIS = 60 * 1000L;

	public static void verification(String appKey, String appSecret, String sign, String nonce, String timestamp,
			Map<String, String> map) {
		if (StringUtil.isEmpty(appKey)) {
			throw new RuntimeException("appKey不为空");
		}
		if (StringUtil.isEmpty(appSecret)) {
			throw new RuntimeException("appSecret不为空");
		}
		if (StringUtil.isEmpty(nonce)) {
			throw new RuntimeException("nonce不为空");
		}
		if (StringUtil.isEmpty(timestamp)) {
			throw new RuntimeException("timestamp不为空");
		}
		if (!APP_KEY.equals(appKey)) {
			throw new RuntimeException("appKey不存在");
		}
		if (!APP_SECRET.equals(appSecret)) {
			throw new RuntimeException("appSecret不存在");
		}
		long ts = Long.parseLong(timestamp);
		// 判断时间戳
		long nowTimestamp = IdGenerator.SystemClock.now();
		long maxTimestamp = ts + TIMEOUT_MILLIS;
		long minTimestamp = ts - TIMEOUT_MILLIS;
		if (nowTimestamp > maxTimestamp || nowTimestamp < minTimestamp) {
			throw new RuntimeException("timestamp已过期");
		}
		if (StringUtil.isEmpty(sign)) {
			throw new RuntimeException("sign不能为空");
		}
		String params = MapUtil.parseParams(map, false);
		String newSing = sign(appKey, appSecret, nonce, ts, params);
		if (!sign.equals(newSing)) {
			throw new RuntimeException("Api验签失败，请检查配置");
		}
	}

	/**
	 * MD5(appKey+appSecret+nonce+timestamp+params).
	 * @param appKey aap标识
	 * @param appSecret app密钥
	 * @param nonce 随机字符
	 * @param params 参数
	 * @param timestamp 时间戳
	 */
	private static String sign(String appKey, String appSecret, String nonce, long timestamp, String params) {
		String str = appKey.concat(appSecret).concat(nonce).concat(String.valueOf(timestamp)).concat(params);
		return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
	}

}
