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

package org.laokou.admin.user.command.query;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.ossLog.gatewayimpl.database.OssLogMapper;
import org.laokou.admin.ossLog.gatewayimpl.database.dataobject.OssLogDO;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.dto.UserGetQry;
import org.laokou.admin.user.dto.clientobject.UserCO;
import org.laokou.admin.user.gatewayimpl.database.UserDeptMapper;
import org.laokou.admin.user.gatewayimpl.database.UserMapper;
import org.laokou.admin.user.gatewayimpl.database.UserRoleMapper;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.stereotype.Component;
import static org.laokou.common.tenant.constant.DSConstants.DOMAIN;

/**
 * 查看用户请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserGetQryExe {

	private final UserMapper userMapper;

	private final UserRoleMapper userRoleMapper;

	private final UserDeptMapper userDeptMapper;

	private final OssLogMapper ossLogMapper;

	public Result<UserCO> execute(UserGetQry qry) throws Exception {
		try {
			UserCO userCO = UserConvertor.toClientObject(userMapper.selectById(qry.getId()));
			userCO.setRoleIds(userRoleMapper.selectRoleIdsByUserId(qry.getId()));
			userCO.setDeptIds(userDeptMapper.selectDeptIdsByUserId(qry.getId()));
			DynamicDataSourceContextHolder.push(DOMAIN);
			OssLogDO ossLogDO = ossLogMapper.selectById(userCO.getAvatar());
			if (ObjectUtils.isNotNull(ossLogDO)) {
				userCO.setAvatarUrl(ossLogDO.getUrl());
	 	    }
			return Result.ok(userCO);
		} finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
