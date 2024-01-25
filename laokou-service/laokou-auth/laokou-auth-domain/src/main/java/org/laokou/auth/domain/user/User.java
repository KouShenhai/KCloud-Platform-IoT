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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.dto.Identifier;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;
import static org.laokou.common.i18n.common.ErrorCodes.*;
import static org.laokou.common.i18n.common.MybatisPlusConstants.*;
import static org.laokou.common.i18n.common.OAuth2Constants.PASSWORD;
import static org.laokou.common.i18n.common.OAuth2Constants.USERNAME;
import static org.laokou.common.i18n.common.StatusCodes.CUSTOM_SERVER_ERROR;
import static org.laokou.common.i18n.common.StatusCodes.FORBIDDEN;
import static org.laokou.common.i18n.common.UserStatusEnums.ENABLED;
import static org.laokou.common.i18n.common.ValCodes.*;

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

    public static void checkMobile(String mobile) {
        if (StringUtil.isEmpty(mobile)) {
            throw new AuthException(CUSTOM_SERVER_ERROR, ValidatorUtil.getMessage(OAUTH2_MOBILE_REQUIRE));
        }
        if (!RegexUtil.mobileRegex(mobile)) {
            throw new AuthException(MOBILE_ERROR);
        }
    }

    public static void checkMail(String mail) {
        if (StringUtil.isEmpty(mail)) {
            throw new AuthException(CUSTOM_SERVER_ERROR, ValidatorUtil.getMessage(OAUTH2_MAIL_REQUIRE));
        }
        if (!RegexUtil.mailRegex(mail)) {
            throw new AuthException(MAIL_ERROR, MessageUtil.getMessage(MAIL_ERROR));
        }
    }

    public static void checkNullPassword(String password) {
        if (StringUtil.isEmpty(password)) {
            throw new AuthException(CUSTOM_SERVER_ERROR, ValidatorUtil.getMessage(OAUTH2_PASSWORD_REQUIRE));
        }
    }

    public static void checkNullUsername(String username) {
        if (StringUtil.isEmpty(username)) {
            throw new AuthException(CUSTOM_SERVER_ERROR, ValidatorUtil.getMessage(OAUTH2_USERNAME_REQUIRE));
        }
    }

    public static void checkCaptcha(Boolean validateResult) {
        if (ObjectUtil.isNull(validateResult)) {
            throw new AuthException(CAPTCHA_EXPIRED);
        }
        if (!validateResult) {
            throw new  AuthException(CAPTCHA_ERROR);
        }
    }

    public static void checkNull(User user) {
        if (ObjectUtil.isNull(user)) {
            throw new AuthException(ACCOUNT_PASSWORD_ERROR);
        }
    }

    public static void checkPassword(String password, String clientPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(password, clientPassword)) {
            throw new AuthException(ACCOUNT_PASSWORD_ERROR);
        }
    }

    public static void checkStatus(Integer status) {
        if (ObjectUtil.equals(ENABLED.ordinal(), status)) {
            throw new AuthException(ACCOUNT_DISABLE);
        }
    }

    public static void checkPermissions(Set<String> permissions) {
        if (CollectionUtil.isEmpty(permissions)) {
            throw new AuthException(FORBIDDEN);
        }
    }

}
