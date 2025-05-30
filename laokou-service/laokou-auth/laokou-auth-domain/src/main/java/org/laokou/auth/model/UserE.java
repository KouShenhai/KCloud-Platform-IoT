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

package org.laokou.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.dto.Identifier;
import org.laokou.common.i18n.util.ObjectUtils;

import static org.laokou.auth.model.SuperAdminEnum.YES;

/**
 * 用户实体.
 *
 * @author laokou
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
public class UserE extends Identifier {

	/**
	 * 用户名.
	 */
	private String username;

	/**
	 * 用户密码.
	 */
	private String password;

	/**
	 * 超级管理员标识 0否 1是.
	 */
	private Integer superAdmin;

	/**
	 * 用户头像.
	 */
	private String avatar;

	/**
	 * 用户邮箱.
	 */
	private String mail;

	/**
	 * 用户状态 0启用 1禁用.
	 */
	private Integer status;

	/**
	 * 用户手机号.
	 */
	private String mobile;

	/**
	 * 租户ID.
	 */
	private Long tenantId;

	public UserE fillValue(String username, String mail, String mobile, Long tenantId) throws Exception {
		this.username = AESUtils.encrypt(username);
		this.mail = AESUtils.encrypt(mail);
		this.mobile = AESUtils.encrypt(mobile);
		this.tenantId = tenantId;
		return this;
	}

	public boolean isSuperAdministrator() {
		return ObjectUtils.equals(YES.getCode(), this.superAdmin);
	}

}
