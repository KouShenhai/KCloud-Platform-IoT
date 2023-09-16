/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.auth.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * &#064;JsonTypeInfo(use = JsonTypeInfo.Id.NAME) => 多态子类与抽象类绑定
 *
 * @author laokou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class User implements UserDetails, OAuth2AuthenticatedPrincipal, Serializable {

	@Serial
	private static final long serialVersionUID = 3319752558160144611L;

	private Long id;

	private String username;

	private String avatar;

	private Integer superAdmin;

	private Integer status;

	private transient String mail;

	private transient String mobile;

	private transient String password;

	private Long deptId;

	private String deptPath;

	private List<String> deptPaths;

	private List<String> permissionList;

	private Long tenantId;

	private String sourceName;

	private String loginIp;

	private LocalDateTime loginDate;

	private LocalDateTime expireDate;

	public User(Long id, Integer superAdmin, Long tenantId) {
		this.id = id;
		this.superAdmin = superAdmin;
		this.tenantId = tenantId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
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
		return expireDate.equals(that.expireDate);
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
		result = 31 * result + expireDate.hashCode();
		return result;
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
		return this.status == Status.ENABLED.ordinal();
	}

	/**
	 * Get the OAuth 2.0 token attributes
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
