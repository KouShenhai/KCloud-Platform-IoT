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

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.ObjectUtils;
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
public final class UserExtDetails implements UserDetails, OAuth2AuthenticatedPrincipal, Serializable {

	@Serial
	private static final long serialVersionUID = 3319752558160144611L;

	/**
	 * 用户名解密失败.
	 */
	private static final String USERNAME_AES_DECRYPT_FAIL = "B_User_UsernameAESDecryptFail";

	/**
	 * 手机号解密失败.
	 */
	private static final String MOBILE_AES_DECRYPT_FAIL = "B_User_MobileAESDecryptFail";

	/**
	 * 邮箱解密失败.
	 */
	private static final String MAIL_AES_DECRYPT_FAIL = "B_User_MailAESDecryptFail";

	/**
	 * 用户ID.
	 */
	@Getter
	private Long id;

	/**
	 * 用户名.
	 */
	@Getter
	private String username;

	/**
	 * 头像.
	 */
	@Getter
	private String avatar;

	/**
	 * 超级管理员标识.
	 */
	@Getter
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
	@Getter
	private transient String password;

	/**
	 * 租户ID.
	 */
	@Getter
	private Long tenantId;

	/**
	 * 菜单权限标识集合.
	 */
	@Getter
	private Set<String> permissions;

	public UserExtDetails toUserDetail(Long id, Long tenantId) {
		this.id = id;
		this.tenantId = tenantId;
		return this;
	}

	public UserExtDetails toUserDetail(@NonNull User user) {
		this.id = user.id();
		this.username = getDecryptUsername(user.username());
		this.avatar = user.avatar();
		this.superAdmin = user.superAdmin();
		this.status = user.status();
		this.mail = getDecryptMail(user.mail());
		this.mobile = getDecryptMobile(user.mobile());
		this.password = StringConstants.EMPTY;
		this.tenantId = user.tenantId();
		this.permissions = user.permissions();
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (ObjectUtils.isNull(o) || getClass() != o.getClass()) {
			return false;
		}
		UserExtDetails that = (UserExtDetails) o;
		if (!ObjectUtils.equals(id, that.id)) {
			return false;
		}
		if (!ObjectUtils.equals(username, that.username)) {
			return false;
		}
		if (!ObjectUtils.equals(avatar, that.avatar)) {
			return false;
		}
		if (!ObjectUtils.equals(superAdmin, that.superAdmin)) {
			return false;
		}
		if (!ObjectUtils.equals(status, that.status)) {
			return false;
		}
		if (!ObjectUtils.equals(permissions, that.permissions)) {
			return false;
		}
		if (!ObjectUtils.equals(tenantId, that.tenantId)) {
			return false;
		}
		if (!ObjectUtils.equals(mobile, that.mobile)) {
			return false;
		}
		return ObjectUtils.equals(mail, that.mail);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + username.hashCode();
		result = 31 * result + avatar.hashCode();
		result = 31 * result + superAdmin.hashCode();
		result = 31 * result + status.hashCode();
		result = 31 * result + permissions.hashCode();
		result = 31 * result + tenantId.hashCode();
		result = 31 * result + mail.hashCode();
		result = 31 * result + mobile.hashCode();
		return result;
	}

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

	public UserExtDetails getDecryptInfo() {

		return this;
	}

	public String getDecryptUsername(String username) {
		if (StringExtUtils.isNotEmpty(username)) {
			try {
				return AESUtils.decrypt(username);
			}
			catch (Exception ex) {
				throw new BizException(USERNAME_AES_DECRYPT_FAIL, ex);
			}
		}
		return username;
	}

	public String getDecryptMail(String mail) {
		if (StringExtUtils.isNotEmpty(mail)) {
			try {
				return AESUtils.decrypt(mail);
			}
			catch (Exception ex) {
				throw new BizException(MAIL_AES_DECRYPT_FAIL, ex);
			}
		}
		return mail;
	}

	public String getDecryptMobile(String mobile) {
		if (StringExtUtils.isNotEmpty(mobile)) {
			try {
				return AESUtils.decrypt(mobile);
			}
			catch (Exception ex) {
				throw new BizException(MOBILE_AES_DECRYPT_FAIL, ex);
			}
		}
		return mobile;
	}

}
