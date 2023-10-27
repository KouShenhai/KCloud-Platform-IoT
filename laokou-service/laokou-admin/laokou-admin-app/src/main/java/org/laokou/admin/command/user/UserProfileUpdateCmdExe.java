/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.user.UserProfileUpdateCmd;
import org.laokou.admin.dto.user.clientobject.UserProfileCO;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.common.jasypt.utils.AesUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.ValCode.SYSTEM_ID_REQUIRE;
import static org.laokou.common.mybatisplus.constant.DsConstant.USER;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserProfileUpdateCmdExe {

	private final UserGateway userGateway;

	private final UserMapper userMapper;

	public Result<Boolean> execute(UserProfileUpdateCmd cmd) {
		UserProfileCO co = cmd.getUserProfileCO();
		DynamicDataSourceContextHolder.push(USER);
		validate(co);
		encrypt(co);
		return Result.of(userGateway.updateInfo(toUser(co)));
	}

	private User toUser(UserProfileCO co) {
		User user = ConvertUtil.sourceToTarget(co, User.class);
		user.setEditor(UserUtil.getUserId());
		return user;
	}

	private void encrypt(UserProfileCO co) {
		co.setMobile(AesUtil.encrypt(co.getMobile()));
		co.setMail(AesUtil.encrypt(co.getMail()));
	}

	private void validate(UserProfileCO co) {
		if (co.getId() == null) {
			throw new SystemException(ValidatorUtil.getMessage(SYSTEM_ID_REQUIRE));
		}
		if (StringUtil.isNotEmpty(co.getMobile())) {
			// Long count = userMapper.selectCount(Wrappers.query(UserDO.class)
			// .eq("mobile", co.getMobile())
			// .ne("id", co.getId()));
			// if (count > 0) {
			// throw new GlobalException("手机号已被注册，请重新填写");
			// }
		}
		if (StringUtil.isNotEmpty(co.getMail())) {
			// Long count = userMapper.selectCount(Wrappers.query(UserDO.class)
			// .eq("mail", co.getMail())
			// .ne("id", co.getId()));
			// if (count > 0) {
			// throw new GlobalException("邮箱地址已被注册，请重新填写");
			// }
		}
	}

}
