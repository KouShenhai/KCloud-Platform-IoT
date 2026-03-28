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

package org.laokou.common.context.util;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.jackson.CoreJacksonModule;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author laokou
 * @see CoreJacksonModule
 */
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class UserExtDetails implements Serializable {

	@Serial
	private static final long serialVersionUID = 3319752558160144611L;

	/**
	 * 用户ID.
	 */
	private Long id;

	/**
	 * 用户名.
	 */
	private final String username;

	/**
	 * 密码.
	 */
	private final transient String password;

	/**
	 * 头像.
	 */
	private String avatar;

	/**
	 * 超级管理员标识.
	 */
	private Boolean superAdmin;

	/**
	 * 用户状态 0启用 1禁用.
	 */
	private Integer status;

	/**
	 * 邮箱.
	 */
	private String mail;

	/**
	 * 手机号.
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

	/**
	 * 菜单权限标识集合.
	 */
	private Set<String> permissions;

	/**
	 * 部门IDS.
	 */
	private Set<Long> deptIds;

	/**
	 * 创建者.
	 */
	private Long creator;

}
