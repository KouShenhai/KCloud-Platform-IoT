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

package org.laokou.admin.command.role;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.RoleConvertor;
import org.laokou.admin.domain.gateway.RoleGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.role.RoleInsertCmd;
import org.laokou.admin.dto.role.clientobject.RoleCO;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleInsertCmdExe {

	private final RoleGateway roleGateway;

	private final RoleMapper roleMapper;

	public Result<Boolean> execute(RoleInsertCmd cmd) {
		RoleCO roleCO = cmd.getRoleCO();
		Long count = roleMapper.selectCount(Wrappers.lambdaQuery(RoleDO.class).eq(RoleDO::getName, roleCO.getName()));
		if (count > 0) {
			throw new GlobalException("角色已存在，请重新填写");
		}
		return Result.of(roleGateway.insert(RoleConvertor.toEntity(roleCO), toUser()));
	}

	private User toUser() {
		return ConvertUtil.sourceToTarget(UserUtil.user(), User.class);
	}

}
