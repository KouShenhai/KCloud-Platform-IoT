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

package org.laokou.admin.role.command;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.role.ability.RoleDomainService;
import org.laokou.admin.role.convertor.RoleConvertor;
import org.laokou.admin.role.dto.RoleModifyCmd;
import org.laokou.admin.role.model.RoleE;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.springframework.stereotype.Component;

/**
 * 修改角色命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleModifyCmdExe {

	private final RoleDomainService roleDomainService;

	private final TransactionalUtils transactionalUtils;

	@CommandLog
	public void executeVoid(RoleModifyCmd cmd) {
		RoleE roleE = RoleConvertor.toEntity(cmd.getCo(), false);
		roleE.checkRoleParam();
		transactionalUtils.executeInTransaction(() -> roleDomainService.updateRole(roleE));
	}

}
