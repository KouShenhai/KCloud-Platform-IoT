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

package org.laokou.common.context.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @param id 用户ID.
 * @param username 用户名.
 * @param avatar 头像.
 * @param superAdmin 超级管理员标识.
 * @param status 用户状态 0启用 1禁用.
 * @param mail 邮箱.
 * @param mobile 手机号.
 * @param tenantId 租户ID.
 * @param permissions 菜单权限标识集合.
 * @author laokou
 */
@JsonTypeName("User")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
		isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public record User(Long id, String username, String avatar, Boolean superAdmin, Integer status, String mail,
		String mobile, Long tenantId, Set<String> permissions) implements Authentication, Principal, Serializable {

	@Override
	@NullMarked
	public String getName() {
		return username;
	}

	@Override
	@NullMarked
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
	}

	@Override
	public @Nullable Object getCredentials() {
		return null;
	}

	@Override
	public @Nullable Object getDetails() {
		return username;
	}

	@Override
	public @Nullable Object getPrincipal() {
		return username;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
	}

}
