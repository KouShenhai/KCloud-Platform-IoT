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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @param id 用户ID.
 * @param username 用户名.
 * @param password 密码.
 * @param avatar 头像.
 * @param superAdmin 超级管理员标识.
 * @param status 用户状态 0启用 1禁用.
 * @param mail 邮箱.
 * @param mobile 手机号.
 * @param tenantId 租户ID.
 * @param deptId 部门ID.
 * @param permissions 菜单权限标识集合.
 * @param deptIds 部门IDS.
 * @param creator 创建者.
 * @author laokou
 */
@JsonTypeName("OAuth2Authentication")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
		isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public record OAuth2Authentication(Long id, String username, String password, String avatar, Boolean superAdmin,
		Integer status, String mail, String mobile, Long tenantId, Long deptId, Set<String> permissions,
		Set<Long> deptIds, Long creator) implements UserDetails, Authentication {

	@Serial
	private static final long serialVersionUID = 3319752558160144611L;

	@Override
	@NullMarked
	public Collection<GrantedAuthority> getAuthorities() {
		return Collections.emptySet();
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	@NullMarked
	public String getUsername() {
		return this.username;
	}

	@Override
	public Object getCredentials() {
		return this.id.toString();
	}

	@Override
	public Object getDetails() {
		return this.id.toString();
	}

	@Override
	@NonNull public Object getPrincipal() {
		return this;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return this.id.toString();
	}

}
