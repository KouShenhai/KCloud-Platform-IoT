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

package org.laokou.auth.domain.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.domain.gateway.*;
import org.laokou.auth.domain.model.auth.AuthA;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class AuthDomainService {

	private final UserGateway userGateway;

	private final MenuGateway menuGateway;

	private final DeptGateway deptGateway;

	private final SourceGateway sourceGateway;

	private final Environment environment;

	private final CaptchaGateway captchaGateway;

	private final PasswordEncoder passwordEncoder;

	public void auth(AuthA auth) {
		auth.updateAppName(environment.getProperty("spring.application.name"));
		auth.updateSource(sourceGateway.getSourceName(auth.getUser()));
		// 校验验证码
		checkCaptcha(auth);
		auth.updateUser(userGateway.getProfile(auth.getUser()));
		// 校验密码
		auth.checkUserPassword(passwordEncoder);
		// 校验用户状态
		auth.checkUserStatus();
		auth.updateMenu(menuGateway.getPermissions(auth.getUser()));
		// 校验权限
		auth.checkMenuPermissions();
		auth.updateDept(deptGateway.getDeptPaths(auth.getUser()));
		// 成功
		auth.ok();
	}

	private void checkCaptcha(AuthA auth) {
		if (auth.isUseCaptcha()) {
			Boolean result = captchaGateway.checkValue(auth.getCaptcha().uuid(), auth.getCaptcha().captcha());
			auth.checkCaptcha(result);
		}
	}

}
