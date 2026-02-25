/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.user.service.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.admin.user.gatewayimpl.database.UserMapper;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.user.model.UserA;
import org.laokou.common.core.util.CollectionExtUtils;
import org.laokou.common.core.util.RegexUtils;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * @author laokou
 */
final class UserParamValidator {

	private UserParamValidator() {

	}

	public static ParamValidator.Validate validateId(UserA userA) {
		Long id = userA.getId();
		if (ObjectUtils.isNull(id)) {
			return ParamValidator.invalidate("用户ID不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validatePassword(UserA userA, PasswordEncoder passwordEncoder,
			UserMapper userMapper) {
		String password = userA.getUserE().getPassword();
		if (StringExtUtils.isEmpty(password)) {
			return ParamValidator.invalidate("用户密码不能为空");
		}
		if (password.length() < 6 || password.length() > 30) {
			return ParamValidator.invalidate("用户密码长度为6-30个字符");
		}
		UserDO userDO = userMapper.selectById(userA.getId());
		if (ObjectUtils.isNull(userDO)) {
			return ParamValidator.invalidate("用户不存在");
		}
		if (passwordEncoder.matches(password, userDO.getPassword())) {
			return ParamValidator.invalidate("用户新密码不能与旧密码相同");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateUsername(UserA userA, UserMapper userMapper, boolean isSave)
			throws Exception {
		String username = userA.getUserE().getUsername();
		Long id = userA.getId();
		String encryptUsername = AESUtils.encrypt(username);
		if (StringExtUtils.isEmpty(username)) {
			return ParamValidator.invalidate("用户名不能为空");
		}
		if (username.length() > 30) {
			return ParamValidator.invalidate("用户名不能超过30个字符");
		}
		if (!RegexUtils.matches("^[A-Za-z]+$|^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9]+$", username)) {
			return ParamValidator.invalidate("用户名只能为大小写字母或大小写字母与数字的组合");
		}
		if (isSave && userMapper
			.selectCount(Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, encryptUsername)) > 0) {
			return ParamValidator.invalidate("用户名已存在");
		}
		if (!isSave && userMapper.selectCount(Wrappers.lambdaQuery(UserDO.class)
			.eq(UserDO::getUsername, encryptUsername)
			.ne(UserDO::getId, id)) > 0) {
			return ParamValidator.invalidate("用户名已存在");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateMail(UserA userA, UserMapper userMapper, boolean isSave)
			throws Exception {
		String mail = userA.getUserE().getMail();
		if (StringExtUtils.isNotEmpty(mail)) {
			if (!RegexUtils.mailRegex(mail)) {
				return ParamValidator.invalidate("用户邮箱错误");
			}
			if (mail.length() > 30) {
				return ParamValidator.invalidate("用户邮箱不能超过30个字符");
			}
			Long id = userA.getId();
			String encryptMail = AESUtils.encrypt(mail);
			if (isSave && userMapper
				.selectCount(Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getMail, encryptMail)) > 0) {
				return ParamValidator.invalidate("用户邮箱已存在");
			}
			if (!isSave && userMapper.selectCount(
					Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getMail, encryptMail).ne(UserDO::getId, id)) > 0) {
				return ParamValidator.invalidate("用户邮箱已存在");
			}
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateMobile(UserA userA, UserMapper userMapper) throws Exception {
		String mobile = userA.getUserE().getMobile();
		if (StringExtUtils.isNotEmpty(mobile)) {
			if (!RegexUtils.mobileRegex(mobile)) {
				return ParamValidator.invalidate("用户手机号错误");
			}
			Long id = userA.getId();
			String encryptMobile = AESUtils.encrypt(mobile);
			if (userA.isSave() && userMapper
				.selectCount(Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getMobile, encryptMobile)) > 0) {
				return ParamValidator.invalidate("用户手机号已存在");
			}
			if (userA.isModify() && userMapper.selectCount(Wrappers.lambdaQuery(UserDO.class)
				.eq(UserDO::getMobile, encryptMobile)
				.ne(UserDO::getId, id)) > 0) {
				return ParamValidator.invalidate("用户手机号已存在");
			}
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateRoleIds(UserA userA) {
		List<String> roleIds = userA.getUserE().getRoleIds();
		if (CollectionExtUtils.isEmpty(roleIds)) {
			return ParamValidator.invalidate("用户角色IDS不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateDeptId(UserA userA) {
		if (ObjectUtils.isNull(userA.getUserE().getDeptId())) {
			return ParamValidator.invalidate("用户部门ID不能为空");
		}
		return ParamValidator.validate();
	}

}
