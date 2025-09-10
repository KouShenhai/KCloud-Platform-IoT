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

package org.laokou.common.secret.util;

import org.laokou.common.core.util.MapUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringUtils;

import java.util.Map;

/**
 * @author laokou
 */
public final class ApiSecretParamValidator {

	private static final long TIMEOUT_MILLIS = 60 * 1000L;

	private ApiSecretParamValidator() {

	}

	public static ParamValidator.Validate validateAppKey(String appKey) {
		if (StringUtils.isEmpty(appKey)) {
			return ParamValidator.invalidate("appKey不为空");
		}
		return ObjectUtils.equals(SecretUtils.APP_KEY, appKey) ? ParamValidator.validate() : ParamValidator.invalidate("appKey不存在");
	}

	public static ParamValidator.Validate validateAppSecret(String appSecret) {
		if (StringUtils.isEmpty(appSecret)) {
			return ParamValidator.invalidate("appSecret不为空");
		}
		return ObjectUtils.equals(SecretUtils.APP_SECRET, appSecret) ? ParamValidator.validate()
				: ParamValidator.invalidate("appSecret不存在");
	}

	public static ParamValidator.Validate validateNonce(String nonce) {
		return StringUtils.isEmpty(nonce) ? ParamValidator.invalidate("nonce不为空") : ParamValidator.validate();
	}

	public static ParamValidator.Validate validateTimestamp(String timestamp) {
		if (StringUtils.isEmpty(timestamp)) {
			return ParamValidator.invalidate("timestamp不为空");
		}
		long ts = Long.parseLong(timestamp);
		// 判断时间戳
		long nowTimestamp = System.currentTimeMillis();
		long maxTimestamp = ts + TIMEOUT_MILLIS;
		long minTimestamp = ts - TIMEOUT_MILLIS;
		if (nowTimestamp > maxTimestamp || nowTimestamp < minTimestamp) {
			return ParamValidator.invalidate("timestamp已过期");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateSign(String appKey, String appSecret, String sign, String nonce,
			String timestamp, Map<String, String> map) {
		if (StringUtils.isEmpty(sign)) {
			return ParamValidator.invalidate("sign不能为空");
		}
		String params = MapUtils.parseParamterString(map, false);
		String newSign = SecretUtils.sign(appKey, appSecret, nonce, timestamp, params);
		if (!ObjectUtils.equals(sign, newSign)) {
			return ParamValidator.invalidate("Api验签失败");
		}
		return ParamValidator.validate();
	}

}
