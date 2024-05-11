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

package org.laokou.admin.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * @author laokou
 */
@Data
@Schema(name = "User", description = "用户信息")
public class User extends AggregateRoot<Long> {

	@Schema(name = "username", description = "用户名")
	private String username;

	@Schema(name = "status", description = "用户状态 0启用 1禁用")
	private Integer status;

	@Schema(name = "roleIds", description = "角色IDS")
	private List<Long> roleIds;

	@Schema(name = "password", description = "密码")
	private String password;

	@Schema(name = "avatar", description = "头像")
	private String avatar;

	@Schema(name = "mail", description = "邮箱")
	private String mail;

	@Schema(name = "mobile", description = "手机号")
	private String mobile;

	@Schema(name = "superAdmin", description = "超级管理员标识 0否 1是")
	private Integer superAdmin;

	public void checkUserName(long count) {
		if (count > 0) {
			throw new SystemException("用户名已存在，请重新输入");
		}
	}

	public void checkMail(long count) {
		if (count > 0) {
			throw new SystemException("邮箱地址已被注册，请重新填写");
		}
	}

	public void checkMobile(long count) {
		if (count > 0) {
			throw new SystemException("手机号已被注册，请重新填写");
		}
	}

	public void encryptPassword(PasswordEncoder passwordEncoder, String pwd) {
		if (StringUtil.isNotEmpty(pwd)) {
			this.password = passwordEncoder.encode(pwd);
		}
	}

	public User(Long id, String password) {
		this.password = password;
		this.id = id;
	}

}
