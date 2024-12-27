/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth;

import org.laokou.auth.ability.validator.PasswordValidator;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 密码验证器测试.
 *
 * @author laokou
 */
class PasswordValidatorTest implements PasswordValidator {

	@Override
	public boolean validate(CharSequence rawPassword, String encodedPassword) {
		return ObjectUtil.equals(getEncodePassword(rawPassword.toString()), encodedPassword);
	}

	static String getEncodePassword(String password) {
		return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
	}

}
