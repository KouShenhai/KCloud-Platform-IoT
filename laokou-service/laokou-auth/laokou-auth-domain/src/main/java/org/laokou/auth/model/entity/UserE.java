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

package org.laokou.auth.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.laokou.auth.model.enums.SuperAdmin;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.util.ObjectUtils;

import java.io.Serializable;

/**
 * 用户实体.
 *
 * @author laokou
 */
@Entity
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserE implements Serializable {

	/**
	 * ID.
	 */
	private Long id;

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
	private Long avatar;

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

	/**
	 * 部门ID.
	 */
	private Long deptId;

	public boolean isSuperAdministrator() {
		return ObjectUtils.equals(SuperAdmin.YES.getCode(), this.superAdmin);
	}

}
