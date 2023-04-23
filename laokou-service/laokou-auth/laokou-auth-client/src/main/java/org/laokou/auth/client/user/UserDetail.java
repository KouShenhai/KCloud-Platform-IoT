/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.auth.client.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import org.laokou.auth.client.enums.UserStatusEnum;
import org.laokou.common.jasypt.annotation.JasyptField;
import org.laokou.common.jasypt.enums.TypeEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * &#064;JsonTypeInfo(use  = JsonTypeInfo.Id.NAME) => 多态子类与抽象类绑定
 * @author laokou
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class UserDetail implements UserDetails, OAuth2AuthenticatedPrincipal, Serializable {

    @Serial
    private static final long serialVersionUID = 3319752558160144611L;
    private Long id;
    @JasyptField(type = TypeEnum.DECRYPT)
    private String username;
    private String avatar;
    private Integer superAdmin;
    private Integer status;
    @JasyptField(type = TypeEnum.DECRYPT)
    private transient String mail;
    @JasyptField(type = TypeEnum.DECRYPT)
    private transient String mobile;
    private transient String password;
    private Long deptId;
    private List<Long> deptIds;
    private List<String> permissionList;
    private Long tenantId;
    private String sourceName;
    private String loginIp;
    private Date loginDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetail that = (UserDetail) o;
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
        if (!deptIds.equals(that.deptIds)) {
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
        return loginDate.equals(that.loginDate);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + avatar.hashCode();
        result = 31 * result + superAdmin.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + deptId.hashCode();
        result = 31 * result + deptIds.hashCode();
        result = 31 * result + permissionList.hashCode();
        result = 31 * result + tenantId.hashCode();
        result = 31 * result + sourceName.hashCode();
        result = 31 * result + loginIp.hashCode();
        result = 31 * result + loginDate.hashCode();
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
        return this.status == UserStatusEnum.DISABLE.ordinal() ? false : true;
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
