/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.RoleConvertor;
import org.laokou.admin.domain.gateway.RoleGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.role.RoleModifyCmd;
import org.laokou.admin.dto.role.clientobject.RoleCO;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.ValCodes.SYSTEM_ID_REQUIRE;
import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * 修改角色执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleModifyCmdExe {

	private final RoleGateway roleGateway;

	private final RoleMapper roleMapper;

	private final RoleConvertor roleConvertor;

	/**
	 * 执行修改角色.
	 * @param cmd 修改角色参数
	 * @return 执行修改结果
	 */
	@DS(TENANT)
	public Result<Boolean> execute(RoleModifyCmd cmd) {
		RoleCO co = cmd.getRoleCO();
		Long id = co.getId();
		if (ObjectUtil.isNull(id)) {
			throw new SystemException(ValidatorUtil.getMessage(SYSTEM_ID_REQUIRE));
		}
		Long count = roleMapper
			.selectCount(Wrappers.lambdaQuery(RoleDO.class).eq(RoleDO::getName, co.getName()).ne(RoleDO::getId, id));
		if (count > 0) {
			throw new SystemException("角色已存在，请重新填写");
		}
		return Result.of(roleGateway.update(roleConvertor.toEntity(co), toUser()));
	}

	/**
	 * 转换为用户领域.
	 * @return 用户领域
	 */
	private User toUser() {
		return ConvertUtil.sourceToTarget(UserUtil.user(), User.class);
	}

}
