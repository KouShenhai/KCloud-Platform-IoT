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
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.core.util.RegexUtils;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import static org.laokou.common.i18n.util.ParamValidator.invalidate;
import static org.laokou.common.i18n.util.ParamValidator.validate;

/**
 * @author laokou
 */
public final class UserParamValidator {

	private UserParamValidator() {

	}

	public static ParamValidator.Validate validateId(UserE userE) {
		Long id = userE.getId();
		if (ObjectUtils.isNull(id)) {
			return invalidate("用户ID不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validatePassword(UserE userE, PasswordEncoder passwordEncoder,
			UserMapper userMapper) {
		String password = userE.getPassword();
		if (StringUtils.isEmpty(password)) {
			return invalidate("用户密码不能为空");
		}
		if (password.length() < 6 || password.length() > 30) {
			return invalidate("用户密码长度为6-30个字符");
		}
		UserDO userDO = userMapper.selectById(userE.getId());
		if (ObjectUtils.isNull(userDO)) {
			return invalidate("用户不存在");
		}
		if (passwordEncoder.matches(password, userDO.getPassword())) {
			return invalidate("用户新密码不能与旧密码相同");
		}
		return validate();
	}

	public static ParamValidator.Validate validateUsername(UserE userE, UserMapper userMapper, boolean isSave)
			throws Exception {
		String username = userE.getUsername();
		Long id = userE.getId();
		String encryptUsername = AESUtils.encrypt(username);
		if (StringUtils.isEmpty(username)) {
			return invalidate("用户名不能为空");
		}
		if (username.length() > 30) {
			return invalidate("用户名不能超过30个字符");
		}
		if (!RegexUtils.matches("^[A-Za-z]+$|^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9]+$", username)) {
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
		if (StringUtils.isNotEmpty(mail)) {
			if (!RegexUtils.mailRegex(mail)) {
				return invalidate("用户邮箱错误");
			}
			if (mail.length() > 30) {
				return invalidate("用户邮箱不能超过30个字符");
			}
			Long id = userE.getId();
			String encryptMail = AESUtils.encrypt(mail);
			if (isSave && userMapper
				.selectCount(Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getMail, encryptMail)) > 0) {
				return invalidate("用户邮箱已存在");
			}
			if (!isSave && userMapper.selectCount(
					Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getMail, encryptMail).ne(UserDO::getId, id)) > 0) {
				return invalidate("用户邮箱已存在");
			}
		}
		return validate();
	}

	public static ParamValidator.Validate validateMobile(UserE userE, UserMapper userMapper, boolean isSave)
			throws Exception {
		String mobile = userE.getMobile();
		if (StringUtils.isNotEmpty(mobile)) {
			if (!RegexUtils.mobileRegex(mobile)) {
				return invalidate("用户手机号错误");
			}
			Long id = userE.getId();
			String encryptMobile = AESUtils.encrypt(mobile);
			if (isSave && userMapper
				.selectCount(Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getMobile, encryptMobile)) > 0) {
				return invalidate("用户手机号已存在");
			}
			if (!isSave && userMapper.selectCount(Wrappers.lambdaQuery(UserDO.class)
				.eq(UserDO::getMobile, encryptMobile)
				.ne(UserDO::getId, id)) > 0) {
				return invalidate("用户手机号已存在");
			}
		}
		return validate();
	}

	public static ParamValidator.Validate validateRoleIds(UserE userE) {
		List<String> roleIds = userE.getRoleIds();
		if (CollectionUtils.isEmpty(roleIds)) {
			return invalidate("用户角色IDS不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateDeptIds(UserE userE) {
		List<String> deptIds = userE.getDeptIds();
		if (CollectionUtils.isEmpty(deptIds)) {
			return invalidate("用户部门IDS不能为空");
		}
		return validate();
	}

}
