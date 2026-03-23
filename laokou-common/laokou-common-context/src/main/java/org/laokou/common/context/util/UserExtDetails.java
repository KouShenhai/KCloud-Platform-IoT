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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Set;

/**
 * @author laokou
 */
@Getter
@JsonTypeName("UserDetails")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public final class UserExtDetails extends User {

	@Serial
	private static final long serialVersionUID = 3319752558160144611L;

	/**
	 * 用户ID.
	 */
	private final Long id;

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
	private final String avatar;

	/**
	 * 超级管理员标识.
	 */
	private final Boolean superAdmin;

	/**
	 * 用户状态 0启用 1禁用.
	 */
	private final Integer status;

	/**
	 * 邮箱.
	 */
	private final String mail;

	/**
	 * 手机号.
	 */
	private final String mobile;

	/**
	 * 租户ID.
	 */
	private final Long tenantId;

	/**
	 * 部门ID.
	 */
	private final Long deptId;

	/**
	 * 菜单权限标识集合.
	 */
	private final Set<String> permissions;

	/**
	 * 部门IDS.
	 */
	private final Set<Long> deptIds;

	/**
	 * 创建者.
	 */
	private final Long creator;

	public UserExtDetails(@NonNull Long id, @NonNull String username, @NonNull String password, String avatar, @NonNull Boolean superAdmin,
						  @NonNull Integer status, String mail, String mobile, @NonNull Long tenantId, @NonNull Long deptId, @NonNull Set<String> permissions,
						  Set<Long> deptIds, Long creator) {
		super(username, password, AuthorityUtils.createAuthorityList(permissions));
		this.id = id;
		this.username = username;
		this.password = password;
		this.avatar = avatar;
		this.superAdmin = superAdmin;
		this.status = status;
		this.mail = mail;
		this.mobile = mobile;
		this.tenantId = tenantId;
		this.deptId = deptId;
		this.deptIds = deptIds;
		this.creator = creator;
		this.permissions = permissions;
	}

}
