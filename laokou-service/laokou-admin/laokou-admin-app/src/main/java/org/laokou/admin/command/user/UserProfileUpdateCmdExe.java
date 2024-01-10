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
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.laokou.common.i18n.common.ValCodes.SYSTEM_ID_REQUIRE;
import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserProfileUpdateCmdExe {

	private final UserGateway userGateway;

	private final UserMapper userMapper;

	@DS(TENANT)
	public Result<Boolean> execute(UserProfileUpdateCmd cmd) {
		UserProfileCO co = cmd.getUserProfileCO();
		validate(co);
		encrypt(co);
		return Result.of(userGateway.updateInfo(toUser(co)));
	}

	private User toUser(UserProfileCO co) {
		User user = ConvertUtil.sourceToTarget(co, User.class);
		Assert.isTrue(ObjectUtil.isNotNull(user), "user is null");
		user.setEditor(UserUtil.getUserId());
		return user;
	}

	private void encrypt(UserProfileCO co) {
		co.setMobile(AesUtil.encrypt(co.getMobile()));
		co.setMail(AesUtil.encrypt(co.getMail()));
	}

	private void validate(UserProfileCO co) {
		if (ObjectUtil.isNull(co.getId())) {
			throw new SystemException(ValidatorUtil.getMessage(SYSTEM_ID_REQUIRE));
		}
		if (StringUtil.isNotEmpty(co.getMobile())) {
			Long count = userMapper.selectCount(Wrappers.lambdaQuery(UserDO.class)
				.eq(UserDO::getMobile, co.getMobile())
				.ne(UserDO::getId, co.getId()));
			if (count > 0) {
				throw new SystemException("手机号已被注册，请重新填写");
			}
		}
		if (StringUtil.isNotEmpty(co.getMail())) {
			Long count = userMapper.selectCount(
					Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getMail, co.getMail()).ne(UserDO::getId, co.getId()));
			if (count > 0) {
				throw new SystemException("邮箱地址已被注册，请重新填写");
			}
		}
	}

}
