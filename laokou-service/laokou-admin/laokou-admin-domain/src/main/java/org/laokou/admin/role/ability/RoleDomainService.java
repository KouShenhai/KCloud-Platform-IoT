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

package org.laokou.admin.role.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.role.gateway.RoleDeptGateway;
import org.laokou.admin.role.gateway.RoleGateway;
import org.laokou.admin.role.gateway.RoleMenuGateway;
import org.laokou.admin.role.model.RoleE;
import org.laokou.common.core.util.ThreadUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * 角色领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleDomainService {

	private final RoleGateway roleGateway;

	private final RoleMenuGateway roleMenuGateway;

	private final RoleDeptGateway roleDeptGateway;

	public void createRole(RoleE roleE) {
		roleGateway.createRole(roleE);
	}

	public void updateRole(RoleE roleE) {
		roleGateway.updateRole(roleE);
	}

	public void updateAuthorityRole(RoleE roleE) throws InterruptedException {
		roleE.checkRoleParam();
		List<Callable<Boolean>> futures = new ArrayList<>(3);
		futures.add(() -> {
			roleGateway.updateRole(roleE);
			return true;
		});
		futures.add(() -> {
			roleMenuGateway.updateRoleMenu(roleE);
			return true;
		});
		futures.add(() -> {
			roleDeptGateway.updateRoleDept(roleE);
			return true;
		});
		try (ExecutorService virtualTaskExecutor = ThreadUtils.newVirtualTaskExecutor()) {
			virtualTaskExecutor.invokeAll(futures);
		}
	}

	public void deleteRole(Long[] ids) throws InterruptedException {
		List<Callable<Boolean>> futures = new ArrayList<>(3);
		futures.add(() -> {
			roleGateway.deleteRole(ids);
			return true;
		});
		futures.add(() -> {
			roleMenuGateway.deleteRoleMenu(ids);
			return true;
		});
		futures.add(() -> {
			roleDeptGateway.deleteRoleDept(ids);
			return true;
		});
		try (ExecutorService virtualTaskExecutor = ThreadUtils.newVirtualTaskExecutor()) {
			virtualTaskExecutor.invokeAll(futures);
		}
	}

}
