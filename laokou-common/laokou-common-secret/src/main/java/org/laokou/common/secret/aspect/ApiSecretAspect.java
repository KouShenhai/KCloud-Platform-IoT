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

import static org.laokou.common.secret.utils.SecretUtil.*;

/**
 * @author laokou
 */
@Component
@Aspect
@Slf4j
public class ApiSecretAspect {

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
