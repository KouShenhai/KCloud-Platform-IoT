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

package org.laokou.admin.command.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.UserConvertor;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.user.UserInsertCmd;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;
import static org.laokou.common.i18n.common.StringConstants.SINGLE_QUOT;

/**
 * 新增用户执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserInsertCmdExe {

	private final UserGateway userGateway;

	private final UserMapper userMapper;

	private final UserConvertor userConvertor;

	/**
	 * 执行新增用户.
	 * @param cmd 新增用户参数
	 * @return 执行新增结果
	 */
	@DS(TENANT)
	public Result<Boolean> execute(UserInsertCmd cmd) {
		UserCO co = cmd.getUserCO();
		int count = userMapper.getUserCount(toUserDO(co), AesUtil.getKey());
		if (count > 0) {
			throw new SystemException("用户名已存在，请重新输入");
		}
		return Result.of(userGateway.insert(toUser(co)));
	}

	/**
	 * 转换为用户数据模型.
	 * @param co 用户对象
	 * @return 用户数据模型
	 */
	private UserDO toUserDO(UserCO co) {
		return userConvertor.toDataObj(co);
	}

	/**
	 * 转换为用户领域.
	 * @param co 用户对象
	 * @return 用户领域
	 */
	private User toUser(UserCO co) {
		User user = userConvertor.toEntity(co);
		user.setTenantId(UserUtil.getTenantId());
		user.setCreator(UserUtil.getUserId());
		user.setDeptId(co.getDeptId());
		user.setDeptPath(co.getDeptPath());
		user.setUsername(SINGLE_QUOT.concat(user.getUsername()).concat(SINGLE_QUOT));
		return user;
	}

}
