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
import org.laokou.auth.gateway.*;
import org.laokou.auth.model.AuthA;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static org.laokou.common.core.config.SpringTaskExecutorConfig.THREAD_POOL_TASK_EXECUTOR_NAME;

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

	private final CaptchaGateway captchaGateway;

	private final LoginLogGateway loginLogGateway;

	private final NoticeLogGateway noticeLogGateway;

	private final PasswordValidator passwordValidator;

	@Async(THREAD_POOL_TASK_EXECUTOR_NAME)
	public void recordLoginLog(DefaultDomainEvent domainEvent) {
		loginLogGateway.create(domainEvent);
	}

	@Async(THREAD_POOL_TASK_EXECUTOR_NAME)
	public void recordNoticeLog(DefaultDomainEvent domainEvent) {
		noticeLogGateway.create(domainEvent);
	}

	public void auth(AuthA auth) {
		// 校验验证码
		checkCaptcha(auth);
		// 修改数据源前缀
		auth.updateSourcePrefix(sourceGateway.getPrefix(auth.getTenantCode()));
		// 修改用户信息
		auth.updateUserInfo(userGateway.getProfile(auth.getUser(), auth.getTenantCode()));
		// 校验密码
		auth.checkUserPassword(passwordValidator);
		// 校验用户状态
		auth.checkUserStatus();
		// 修改菜单权限
		auth.updateMenuPermissions(menuGateway.getPermissions(auth.getUser()));
		// 修改部门路径
		auth.updateDeptPaths(deptGateway.getPaths(auth.getUser()));
		// 认证成功
		auth.success();
	}

	private void checkCaptcha(AuthA auth) {
		if (auth.isUseCaptcha()) {
			auth.checkCaptcha(captchaGateway.checkValue(auth.getCaptcha().uuid(), auth.getCaptcha().captcha()));
		}
	}

}
