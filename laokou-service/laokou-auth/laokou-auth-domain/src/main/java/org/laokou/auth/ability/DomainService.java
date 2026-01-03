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

package org.laokou.auth.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.gateway.CaptchaGateway;
import org.laokou.auth.gateway.DeptGateway;
import org.laokou.auth.gateway.MenuGateway;
import org.laokou.auth.gateway.OssLogGateway;
import org.laokou.auth.gateway.TenantGateway;
import org.laokou.auth.gateway.UserGateway;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.enums.DataScopeEnum;
import org.springframework.stereotype.Component;

import java.util.Set;

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

	private final OssLogGateway ossLogGateway;

	private final CaptchaGateway captchaGateway;

	public void createCaptcha(AuthA authA) {
		// 校验验证码参数
		authA.checkCaptchaParam();
		// 获取租户ID
		authA.getTenantId(() -> tenantGateway.getTenantId(authA.getUserV().tenantCode()));
		// 校验租户ID
		authA.checkTenantId();
		// 保存验证码
		captchaGateway.createCaptcha(authA.getCaptchaCacheKeyBySend(), authA.getCaptchaBySend());
	}

	public void auth(AuthA authA) {
		// 校验认证参数
		authA.checkAuthParam();
		// 获取租户ID
		authA.getTenantId(() -> tenantGateway.getTenantId(authA.getUserV().tenantCode()));
		// 校验租户ID
		authA.checkTenantId();
		// 校验验证码
		authA.checkCaptcha();
		// 获取用户信息
		authA.getUserInfo(userGateway.getUserProfile(authA.getUserV()));
		// 校验用户名
		authA.checkUsername();
		// 校验密码
		authA.checkPassword();
		// 校验用户状态
		authA.checkUserStatus();
		// 校验部门ID
		authA.checkDeptId();
		// 获取菜单权限标识集合
		authA.getMenuPermissions(menuGateway.getMenuPermissions(authA.getUserE()));
		// 校验菜单权限标识集合
		authA.checkMenuPermissions();
		// 获取数据权限
		authA.getDataFilter(Set.of(DataScopeEnum.ALL.getCode()), () -> Set.of(1L));
		// 校验数据权限
		authA.checkDataFilter();
		// 获取用户头像
		authA.getUserAvatar(ossLogGateway.getOssUrl(authA.getUserE().getAvatar()));
	}

}
