/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth;

import org.laokou.auth.model.validator.AuthParamValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 认证参数校验器测试.
 *
 * @author laokou
 */
@SpringBootTest
class AuthParamValidatorTest {

	private final AuthParamValidator authorizationCodeAuthParamValidator;

	private final AuthParamValidator mailAuthParamValidator;

	private final AuthParamValidator mobileAuthParamValidator;

	private final AuthParamValidator testAuthParamValidator;

	private final AuthParamValidator usernamePasswordAuthParamValidator;

	AuthParamValidatorTest(
			@Qualifier("authorizationCodeAuthParamValidator") AuthParamValidator authorizationCodeAuthParamValidator,
			@Qualifier("mailAuthParamValidator") AuthParamValidator mailAuthParamValidator,
			@Qualifier("mobileAuthParamValidator") AuthParamValidator mobileAuthParamValidator,
			@Qualifier("testAuthParamValidator") AuthParamValidator testAuthParamValidator,
			@Qualifier("usernamePasswordAuthParamValidator") AuthParamValidator usernamePasswordAuthParamValidator) {
		this.authorizationCodeAuthParamValidator = authorizationCodeAuthParamValidator;
		this.mailAuthParamValidator = mailAuthParamValidator;
		this.mobileAuthParamValidator = mobileAuthParamValidator;
		this.testAuthParamValidator = testAuthParamValidator;
		this.usernamePasswordAuthParamValidator = usernamePasswordAuthParamValidator;
	}

}
