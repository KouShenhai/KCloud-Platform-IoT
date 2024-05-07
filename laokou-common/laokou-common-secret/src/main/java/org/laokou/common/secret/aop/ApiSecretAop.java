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

package org.laokou.common.secret.aop;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.secret.utils.SecretUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

/**
 * @author laokou
 */
@Component
@Aspect
@Slf4j
public class ApiSecretAop {

	@Schema(name = "NONCE", description = "随机字符")
	public static final String NONCE = "nonce";

	@Schema(name = "SIGN", description = "签名（MD5）")
	public static final String SIGN = "sign";

	@Schema(name = "TIMESTAMP", description = "时间戳")
	public static final String TIMESTAMP = "timestamp";

	@Schema(name = "APP_KEY", description = "应用标识")
	public static final String APP_KEY = "app-key";

	@Schema(name = "APP_SECRET", description = "应用密钥")
	public static final String APP_SECRET = "app-secret";

	@Before("@annotation(org.laokou.common.secret.annotation.ApiSecret)")
	public void doBefore() {
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		String nonce = request.getHeader(NONCE);
		String timestamp = request.getHeader(TIMESTAMP);
		String sign = request.getHeader(SIGN);
		String appKey = request.getHeader(APP_KEY);
		String appSecret = request.getHeader(APP_SECRET);
		MultiValueMap<String, String> multiValueMap = MapUtil.getParameters(request);
		SecretUtil.verification(appKey, appSecret, sign, nonce, timestamp, multiValueMap.toSingleValueMap());
	}

}
