/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.secret.aspect;

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
public class ApiSecretAspect {

	private static final String NONCE = "nonce";

	private static final String TIMESTAMP = "timestamp";

	private static final String SIGN = "sign";

	private static final String APP_KEY = "app-key";

	private static final String APP_SECRET = "app-secret";

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
