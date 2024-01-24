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

package org.laokou.auth.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.Identifier;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;
import static org.laokou.common.i18n.common.MybatisPlusConstants.*;
import static org.laokou.common.i18n.common.OAuth2Constants.PASSWORD;
import static org.laokou.common.i18n.common.OAuth2Constants.USERNAME;

/**
 * @author laokou
 */
@Data
@Builder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@Schema(name = "User", description = "用户信息")
public class User extends Identifier<Long> {

    @Schema(name = USERNAME, description = "用户名")
    private String username;

    @Schema(name = PASSWORD, description = "密码", example = "123456")
    private String password;

    @Schema(name = "superAdmin", description = "超级管理员标识 0否 1是", example = "1")
    private Integer superAdmin;

    @Schema(name = "avatar", description = "头像", example = "https://pic.cnblogs.com/avatar/simple_avatar.gif")
    private String avatar;

    @Schema(name = "mail", description = "邮箱", example = "2413176044@qq.com")
    private String mail;

    @Schema(name = "status", description = "用户状态 0正常 1锁定", example = "0")
    private Integer status;

    @Schema(name = "mobile", description = "手机号", example = "18974432500")
    private String mobile;

    @Schema(name = DEPT_ID, description = "部门ID")
    private Long deptId;

    @Schema(name = DEPT_PATH, description = "部门PATH")
    private String deptPath;

    @Schema(name = TENANT_ID, description = "租户ID")
    private Long tenantId;

    @Schema(name = "captcha", description = "验证码")
    private Captcha captcha;

    @Schema(name = "auth", description = "认证")
    private Auth auth;

    @JsonIgnore
    public static void checkNull(User user) {

    }

    @JsonIgnore
    public static void checkPassword(String password, String clientPassword, PasswordEncoder passwordEncoder) {

    }

    @JsonIgnore
    public static void checkStatus(Integer status) {

    }

    @JsonIgnore
    public static void checkPermissions(Set<String> permissions) {

    }

}
