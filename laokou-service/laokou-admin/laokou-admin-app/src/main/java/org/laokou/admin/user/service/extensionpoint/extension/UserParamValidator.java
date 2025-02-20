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

package org.laokou.admin.user.service.extensionpoint.extension;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.admin.user.gatewayimpl.database.UserMapper;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.user.model.UserE;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.crypto.utils.AESUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.ParamValidator;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;

import java.util.List;

import static org.laokou.common.i18n.common.exception.ParamException.OAuth2.MAIL_ERROR;
import static org.laokou.common.i18n.common.exception.ParamException.OAuth2.MOBILE_ERROR;
import static org.laokou.common.i18n.utils.ParamValidator.invalidate;
import static org.laokou.common.i18n.utils.ParamValidator.validate;

/**
 * @author laokou
 */
public final class UserParamValidator {

	private UserParamValidator() {

	}

	public static ParamValidator.Validate validateId(UserE userE) {
		Long id = userE.getId();
		if (ObjectUtil.isNull(id)) {
			return invalidate("ID不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateUserName(UserE userE, UserMapper userMapper, boolean isSave)
			throws Exception {
		String username = userE.getUsername();
		Long id = userE.getId();
		String encryptUsername = AESUtil.encrypt(username);
		if (StringUtil.isEmpty(username)) {
			return invalidate("用户名不能为空");
		}
		if (username.length() > 30) {
			return invalidate("用户名不能超过30个字符");
		}
		if (!RegexUtil.matches("^[A-Za-z]+$|^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9]+$", username)) {
			return invalidate("用户名只能为大小写字母或大小写字母与数字的组合");
		}
		if (isSave && userMapper
			.selectCount(Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, encryptUsername)) > 0) {
			return invalidate("用户名已存在");
		}
		if (!isSave && userMapper.selectCount(Wrappers.lambdaQuery(UserDO.class)
			.eq(UserDO::getUsername, encryptUsername)
			.ne(UserDO::getId, id)) > 0) {
			return invalidate("用户名已存在");
		}
		return validate();
	}

	public static ParamValidator.Validate validateMail(UserE userE, UserMapper userMapper, boolean isSave)
			throws Exception {
		String mail = userE.getMail();
		if (StringUtil.isNotEmpty(mail)) {
			if (!RegexUtil.mailRegex(mail)) {
				return invalidate(ValidatorUtil.getMessage(MAIL_ERROR));
			}
			if (mail.length() > 30) {
				return invalidate("邮箱不能超过30个字符");
			}
			Long id = userE.getId();
			String encryptMail = AESUtil.encrypt(mail);
			if (isSave && userMapper
				.selectCount(Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getMail, encryptMail)) > 0) {
				return invalidate("邮箱已存在");
			}
			if (!isSave && userMapper.selectCount(
					Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getMail, encryptMail).ne(UserDO::getId, id)) > 0) {
				return invalidate("邮箱已存在");
			}
		}
		return validate();
	}

	public static ParamValidator.Validate validateMobile(UserE userE, UserMapper userMapper, boolean isSave)
			throws Exception {
		String mobile = userE.getMobile();
		if (StringUtil.isNotEmpty(mobile)) {
			if (!RegexUtil.mobileRegex(mobile)) {
				return invalidate(ValidatorUtil.getMessage(MOBILE_ERROR));
			}
			Long id = userE.getId();
			String encryptMobile = AESUtil.encrypt(mobile);
			if (isSave && userMapper
				.selectCount(Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getMobile, encryptMobile)) > 0) {
				return invalidate("手机号已存在");
			}
			if (!isSave && userMapper.selectCount(Wrappers.lambdaQuery(UserDO.class)
				.eq(UserDO::getMobile, encryptMobile)
				.ne(UserDO::getId, id)) > 0) {
				return invalidate("手机号已存在");
			}
		}
		return validate();
	}

	public static ParamValidator.Validate validateRoleIds(UserE userE) {
		List<String> roleIds = userE.getRoleIds();
		if (CollectionUtil.isEmpty(roleIds)) {
			return invalidate("角色IDS不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateDeptIds(UserE userE) {
		List<String> deptIds = userE.getDeptIds();
		if (CollectionUtil.isEmpty(deptIds)) {
			return invalidate("部门IDS不能为空");
		}
		return validate();
	}

}
