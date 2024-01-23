/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.SuperAdminEnums.YES;
import static org.laokou.common.i18n.common.UserStatusEnums.ENABLED;

/**
 * &#064;JsonTypeInfo(use = JsonTypeInfo.Id.NAME) => 多态子类与抽象类绑定.
 *
 * @author laokou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "User", description = "用户")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class User implements UserDetails, OAuth2AuthenticatedPrincipal, Serializable {

	@Serial
	private static final long serialVersionUID = 3319752558160144611L;

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "username", description = "用户名")
	private String username;

	@Schema(name = "avatar", description = "头像")
	private String avatar;

	@Schema(name = "superAdmin", description = "超级管理员标识 0否 1是")
	private Integer superAdmin;

	@Schema(name = "status", description = "用户状态 0正常 1锁定")
	private Integer status;

	@Schema(name = "mail", description = "邮箱")
	private String mail;

	@Schema(name = "mobile", description = "手机号")
	private String mobile;

	@Schema(name = "password", description = "密码")
	private transient String password;

	@Schema(name = "deptId", description = "部门ID")
	private Long deptId;

	@Schema(name = "deptPath", description = "部门PATH")
	private String deptPath;

	@Schema(name = "deptPaths", description = "部门PATH集合")
	private List<String> deptPaths;

	@Schema(name = "permissionList", description = "菜单权限标识集合")
	private List<String> permissionList;

	@Schema(name = "tenantId", description = "租户ID")
	private Long tenantId;

	@Schema(name = "sourceName", description = "数据源名称")
	private String sourceName;

	@Schema(name = "loginIp", description = "登录IP")
	private String loginIp;

	@Schema(name = "loginDate", description = "登录时间")
	private LocalDateTime loginDate;

	public User(String username, Long tenantId) {
		this.username = username;
		this.tenantId = tenantId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (ObjectUtil.isNull(o) || getClass() != o.getClass()) {
			return false;
		}
		User that = (User) o;
		if (!id.equals(that.id)) {
			return false;
		}
		if (!username.equals(that.username)) {
			return false;
		}
		if (!avatar.equals(that.avatar)) {
			return false;
		}
		if (!superAdmin.equals(that.superAdmin)) {
			return false;
		}
		if (!status.equals(that.status)) {
			return false;
		}
		if (!deptId.equals(that.deptId)) {
			return false;
		}
		if (!deptPath.equals(that.deptPath)) {
			return false;
		}
		if (!deptPaths.equals(that.deptPaths)) {
			return false;
		}
		if (!permissionList.equals(that.permissionList)) {
			return false;
		}
		if (!tenantId.equals(that.tenantId)) {
			return false;
		}
		if (!sourceName.equals(that.sourceName)) {
			return false;
		}
		if (!loginIp.equals(that.loginIp)) {
			return false;
		}
		if (!loginDate.equals(that.loginDate)) {
			return false;
		}
		if (!mobile.equals(that.mobile)) {
			return false;
		}
		return mail.equals(that.mail);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + username.hashCode();
		result = 31 * result + avatar.hashCode();
		result = 31 * result + superAdmin.hashCode();
		result = 31 * result + status.hashCode();
		result = 31 * result + deptId.hashCode();
		result = 31 * result + deptPath.hashCode();
		result = 31 * result + deptPaths.hashCode();
		result = 31 * result + permissionList.hashCode();
		result = 31 * result + tenantId.hashCode();
		result = 31 * result + sourceName.hashCode();
		result = 31 * result + loginIp.hashCode();
		result = 31 * result + loginDate.hashCode();
		result = 31 * result + mail.hashCode();
		result = 31 * result + mobile.hashCode();
		return result;
	}

	@JsonIgnore
	public boolean isSuperAdministrator() {
		return this.superAdmin == YES.ordinal();
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>(this.permissionList.size());
		authorities.addAll(this.permissionList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
		return authorities;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return this.status == ENABLED.ordinal();
	}

	/**
	 * Get the OAuth 2.0 token attributes.
	 * @return the OAuth 2.0 token attributes
	 */
	@Override
	@JsonIgnore
	public Map<String, Object> getAttributes() {
		return new HashMap<>(0);
	}

	@Override
	@JsonIgnore
	public String getName() {
		return this.username;
	}

}
