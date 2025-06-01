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

package org.laokou.auth.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.gateway.*;
import org.laokou.auth.model.*;
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

	private final LoginLogGateway loginLogGateway;

	private final NoticeLogGateway noticeLogGateway;

	public void createLoginLog(LoginLogE loginLog) {
		// 保存登录日志
		loginLogGateway.createLoginLog(loginLog);
	}

	public void createSendCaptchaInfo(CaptchaE captchaE) {
		// 校验验证码参数
		captchaE.checkCaptchaParam();
		// 获取租户ID
		captchaE.getTenantId(tenantGateway.getIdTenant(captchaE.getTenantCode()));
		// 校验租户ID
		captchaE.checkTenantId();
	}

	public void createNoticeLog(NoticeLogE noticeLog) {
		// 保存通知日志
		noticeLogGateway.createNoticeLog(noticeLog);
	}

	public void auth(AuthA auth) {
		// 校验认证参数
		auth.checkAuthParam();
		// 获取租户ID
		auth.getTenantId(tenantGateway.getIdTenant(auth.getTenantCode()));
		// 校验租户ID
		auth.checkTenantId();
		// 校验验证码
		auth.checkCaptcha();
		// 获取用户信息
		auth.getUserInfo(userGateway.getProfileUser(auth.getUser()));
		// 校验用户名
		auth.checkUsername();
		// 校验密码
		auth.checkPassword();
		// 校验用户状态
		auth.checkUserStatus();
		// 获取菜单权限标识集合
		auth.getMenuPermissions(menuGateway.getPermissionsMenu(auth.getUser()));
		// 校验菜单权限标识集合
		auth.checkMenuPermissions();
		// 获取部门路径集合
		auth.getDeptPaths(deptGateway.getPathsDept(auth.getUser()));
		// 校验部门路径集合
		auth.checkDeptPaths();
	}

}
