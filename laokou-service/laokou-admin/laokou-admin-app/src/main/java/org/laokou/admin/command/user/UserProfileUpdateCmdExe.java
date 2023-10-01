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

package org.laokou.admin.command.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.user.UserProfileUpdateCmd;
import org.laokou.admin.dto.user.clientobject.UserProfileCO;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.jasypt.utils.AesUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.BizCode.ID_NOT_NULL;
import static org.laokou.admin.common.Constant.USER;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserProfileUpdateCmdExe {

	private final UserGateway userGateway;

	private final UserMapper userMapper;

	@DS(USER)
	public Result<Boolean> execute(UserProfileUpdateCmd cmd) {
		UserProfileCO userProfileCO = cmd.getUserProfileCO();
		validate(userProfileCO);
		encrypt(userProfileCO);
		return Result.of(userGateway.updateInfo(toUser(userProfileCO)));
	}

	private User toUser(UserProfileCO userProfileCO) {
		User user = ConvertUtil.sourceToTarget(userProfileCO, User.class);
		user.setEditor(UserUtil.getUserId());
		return user;
	}

	private void encrypt(UserProfileCO userProfileCO) {
		userProfileCO.setMobile(AesUtil.encrypt(userProfileCO.getMobile()));
		userProfileCO.setMail(AesUtil.encrypt(userProfileCO.getMail()));
	}

	private void validate(UserProfileCO userProfileCO) {
		if (userProfileCO.getId() == null) {
			throw new GlobalException(ID_NOT_NULL);
		}
		if (StringUtil.isNotEmpty(userProfileCO.getMobile())) {
			Long count = userMapper.selectCount(Wrappers.query(UserDO.class)
				.eq("tenant_id", UserUtil.getTenantId())
				.eq("mobile", userProfileCO.getMobile())
				.ne("id", userProfileCO.getId()));
			if (count > 0) {
				throw new GlobalException("手机号已被注册，请重新填写");
			}
		}
		if (StringUtil.isNotEmpty(userProfileCO.getMail())) {
			Long count = userMapper.selectCount(Wrappers.query(UserDO.class)
				.eq("tenant_id", UserUtil.getTenantId())
				.eq("mail", userProfileCO.getMail())
				.ne("id", userProfileCO.getId()));
			if (count > 0) {
				throw new GlobalException("邮箱地址已被注册，请重新填写");
			}
		}
	}

}
