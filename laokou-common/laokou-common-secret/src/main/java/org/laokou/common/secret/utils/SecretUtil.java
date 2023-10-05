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
package org.laokou.common.secret.utils;

import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.common.GlobalException;
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

	private static final String APP_KEY_VALUE = "laokou2023";

	private static final String APP_SECRET_VALUE = "vb05f6c45d67340zaz95v7fa6d49v99zx";

	private static final long TIMEOUT_MILLIS = 60 * 1000L;

	public static final String NONCE = "nonce";

	public static final String SIGN = "sign";

	public static final String TIMESTAMP = "timestamp";

	public static final String APP_KEY = "app-key";

	public static final String APP_SECRET = "app-secret";

	public static void verification(String appKey, String appSecret, String sign, String nonce, String timestamp,
			Map<String, String> map) {
		if (StringUtil.isEmpty(appKey)) {
			throw new GlobalException("appKey不为空");
		}
		if (StringUtil.isEmpty(appSecret)) {
			throw new GlobalException("appSecret不为空");
		}
		if (StringUtil.isEmpty(nonce)) {
			throw new GlobalException("nonce不为空");
		}
		if (StringUtil.isEmpty(timestamp)) {
			throw new GlobalException("timestamp不为空");
		}
		if (!APP_KEY_VALUE.equals(appKey)) {
			throw new GlobalException("appKey不存在");
		}
		if (!APP_SECRET_VALUE.equals(appSecret)) {
			throw new GlobalException("appSecret不存在");
		}
		long ts = Long.parseLong(timestamp);
		// 判断时间戳
		long nowTimestamp = IdGenerator.SystemClock.now();
		long maxTimestamp = ts + TIMEOUT_MILLIS;
		long minTimestamp = ts - TIMEOUT_MILLIS;
		if (nowTimestamp > maxTimestamp || nowTimestamp < minTimestamp) {
			throw new GlobalException("timestamp已过期");
		}
		if (StringUtil.isEmpty(sign)) {
			throw new GlobalException("sign不能为空");
		}
		String params = MapUtil.parseParams(map, false);
		String newSing = sign(appKey, appSecret, nonce, ts, params);
		if (!sign.equals(newSing)) {
			throw new GlobalException("Api验签失败，请检查配置");
		}
	}

	/**
	 * MD5(appKey+appSecret+nonce+timestamp+params)
	 */
	private static String sign(String appKey, String appSecret, String nonce, long timestamp, String params) {
		String str = appKey.concat(appSecret).concat(nonce).concat(String.valueOf(timestamp)).concat(params);
		return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
	}

}
