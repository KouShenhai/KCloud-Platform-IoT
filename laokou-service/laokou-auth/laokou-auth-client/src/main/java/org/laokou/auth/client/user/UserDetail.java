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
import lombok.Getter;
import lombok.Setter;
import org.laokou.auth.client.enums.UserStatusEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @JsonTypeInfo(use = JsonTypeInfo.Id.NAME) => 多态子类与抽象类绑定
 * @author laokou
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class UserDetail implements UserDetails, OAuth2AuthenticatedPrincipal, Serializable {

    @Serial
    private static final long serialVersionUID = 3319752558160144611L;
    private Long userId;
    private String username;
    private String imgUrl;
    private Integer superAdmin;
    private Integer status;
    private transient String mail;
    private transient String mobile;
    private transient String password;
    private Long deptId;
    private List<Long> deptIds;
    private List<String> permissionList;
    private Long tenantId;
    private String sourceName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetail that = (UserDetail) o;

        if (!Objects.equals(userId, that.userId)) {
            return false;
        }
        if (!Objects.equals(username, that.username)) {
            return false;
        }
        if (!Objects.equals(imgUrl, that.imgUrl)) {
            return false;
        }
        if (!Objects.equals(superAdmin, that.superAdmin)) {
            return false;
        }
        if (!Objects.equals(status, that.status)) {
            return false;
        }
        if (!Objects.equals(deptId, that.deptId)) {
            return false;
        }
        if (!Objects.equals(deptIds, that.deptIds)) {
            return false;
        }
        if (!Objects.equals(permissionList, that.permissionList)) {
            return false;
        }
        if (!Objects.equals(tenantId, that.tenantId)) {
            return false;
        }
        return Objects.equals(sourceName, that.sourceName);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
        result = 31 * result + (superAdmin != null ? superAdmin.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (deptId != null ? deptId.hashCode() : 0);
        result = 31 * result + (deptIds != null ? deptIds.hashCode() : 0);
        result = 31 * result + (permissionList != null ? permissionList.hashCode() : 0);
        result = 31 * result + (tenantId != null ? tenantId.hashCode() : 0);
        result = 31 * result + (sourceName != null ? sourceName.hashCode() : 0);
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
