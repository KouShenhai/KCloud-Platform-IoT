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

package org.laokou.auth.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.ability.validator.PasswordValidator;
import org.laokou.auth.gateway.*;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.CaptchaE;
import org.laokou.auth.model.InfoV;
import org.laokou.auth.model.LoginLogE;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DomainService {

	private final UserGateway userGateway;

	private final MenuGateway menuGateway;

	private final DeptGateway deptGateway;

	private final TenantGateway tenantGateway;

	private final SourceGateway sourceGateway;

	private final CaptchaGateway captchaGateway;

	private final LoginLogGateway loginLogGateway;

	private final PasswordValidator passwordValidator;

	@Async("ttl-task-executor")
	public void createLoginLog(LoginLogE loginLog) {
		loginLogGateway.create(loginLog);
	}

	public void createCaptcha(Long eventId, AuthA auth, CaptchaE captcha) {
		// 获取验证码信息
		auth.getCaptcha(captcha);
		// 校验租户
		checkTenant(auth);
		// 创建验证码
		auth.createCaptcha(eventId);
	}

	public void auth(AuthA auth, InfoV info) {
		// 获取扩展信息
		auth.getExtInfo(info);
		// 校验验证码
		auth.checkCaptcha(captchaGateway::validate);
		// 校验租户
		checkTenant(auth);
		// 获取用户信息
		auth.getUserInfo(userGateway.getProfile(auth.getUser(), auth.getTenantCode()));
		// 校验用户名
		auth.checkUsername();
		// 校验密码
		auth.checkPassword(passwordValidator);
		// 校验用户状态
		auth.checkUserStatus();
		// 获取菜单标识集合
		auth.getMenuPermissions(menuGateway.getPermissions(auth.getUser()));
		// 校验菜单权限集合
		auth.checkMenuPermissions();
		// 获取部门路径集合
		auth.getDeptPaths(deptGateway.getPaths(auth.getUser()));
		// 校验部门路径集合
		auth.checkDeptPaths();
	}

	private void checkTenant(AuthA auth) {
		// 获取租户ID
		auth.getTenantId(tenantGateway.getId(auth.getTenantCode()));
		// 校验租户ID
		auth.checkTenantId();
		// 获取数据源前缀
		auth.getSourcePrefix(sourceGateway.getPrefix(auth.getTenantCode()));
		// 校验数据源前缀
		auth.checkSourcePrefix();
	}

}
