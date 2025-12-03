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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public final class UserExtDetails implements UserDetails, OAuth2AuthenticatedPrincipal, Serializable {

	@Serial
	private static final long serialVersionUID = 3319752558160144611L;

	/**
	 * 用户ID.
	 */
	@EqualsAndHashCode.Include
	private Long id;

	/**
	 * 用户名.
	 */
	@EqualsAndHashCode.Include
	private String username;

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
	 * 密码.
	 */
	private transient String password;

	/**
	 * 租户ID.
	 */
	@EqualsAndHashCode.Include
	private Long tenantId;

	/**
	 * 菜单权限标识集合.
	 */
	private Set<String> permissions;

	@Override
	@NullMarked
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	/**
	 * Get the OAuth 2.0 token attributes.
	 * @return the OAuth 2.0 token attributes
	 */
	@Override
	public Map<String, Object> getAttributes() {
		return Collections.emptyMap();
	}

	@Override
	@NullMarked
	public String getName() {
		return this.username;
	}

	UserExtDetails decryptUsername() {
		if (StringExtUtils.isNotEmpty(this.username)) {
			try {
				this.username = AESUtils.decrypt(this.username);
			}
			catch (Exception ex) {
				throw new BizException("B_User_UsernameAESDecryptFail", ex);
			}
		}
		return this;
	}

	UserExtDetails decryptMail() {
		if (StringExtUtils.isNotEmpty(this.mail)) {
			try {
				this.mail = AESUtils.decrypt(this.mail);
			}
			catch (Exception ex) {
				throw new BizException("B_User_MailAESDecryptFail", ex);
			}
		}
		return this;
	}

	UserExtDetails decryptMobile() {
		if (StringExtUtils.isNotEmpty(this.mobile)) {
			try {
				this.mobile = AESUtils.decrypt(this.mobile);
			}
			catch (Exception ex) {
				throw new BizException("B_User_MobileAESDecryptFail", ex);
			}
		}
		return this;
	}

}
