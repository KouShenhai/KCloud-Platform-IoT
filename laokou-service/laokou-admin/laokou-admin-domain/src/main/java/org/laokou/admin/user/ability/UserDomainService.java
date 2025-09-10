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

package org.laokou.admin.user.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.user.gateway.UserDeptGateway;
import org.laokou.admin.user.gateway.UserGateway;
import org.laokou.admin.user.gateway.UserRoleGateway;
import org.laokou.admin.user.model.UserE;
import org.laokou.common.core.util.ThreadUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * 用户领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserDomainService {

	private final UserGateway userGateway;

	private final UserRoleGateway userRoleGateway;

	private final UserDeptGateway userDeptGateway;

	public void createUser(UserE userE) throws Exception {
		// 校验用户参数
		userE.checkUserParam();
		// 用户名加密
		userE.encryptUsername();
		// 邮箱加密
		userE.encryptMail();
		// 手机号加密
		userE.encryptMobile();
		userGateway.createUser(userE);
	}

	public void updateUser(UserE userE) throws Exception {
		// 校验用户参数
		userE.checkUserParam();
		// 邮箱加密
		userE.encryptMail();
		// 手机号加密
		userE.encryptMobile();
		userGateway.updateUser(userE);
	}

	public void updateAuthorityUser(UserE userE) throws Exception {
		// 校验用户参数
		userE.checkUserParam();
		List<Callable<Boolean>> futures = new ArrayList<>(2);
		futures.add(() -> {
			userRoleGateway.updateUserRole(userE);
			return true;
		});
		futures.add(() -> {
			userDeptGateway.updateUserDept(userE);
			return true;
		});
		try (ExecutorService virtualTaskExecutor = ThreadUtils.newVirtualTaskExecutor()) {
			virtualTaskExecutor.invokeAll(futures);
		}
	}

	public void deleteUser(Long[] ids) throws InterruptedException {
		List<Callable<Boolean>> futures = new ArrayList<>(3);
		futures.add(() -> {
			userGateway.deleteUser(ids);
			return true;
		});
		futures.add(() -> {
			userRoleGateway.deleteUserRole(ids);
			return true;
		});
		futures.add(() -> {
			userDeptGateway.deleteUserDept(ids);
			return true;
		});
		try (ExecutorService virtualTaskExecutor = ThreadUtils.newVirtualTaskExecutor()) {
			virtualTaskExecutor.invokeAll(futures);
		}
	}

}
