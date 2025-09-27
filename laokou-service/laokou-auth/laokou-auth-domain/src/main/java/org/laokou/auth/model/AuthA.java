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

package org.laokou.auth.model;

import lombok.Getter;
import lombok.Setter;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.crypto.util.RSAUtils;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 认证聚合.
 *
 * @author laokou
 */
@Entity
public class AuthA extends AggregateRoot {

	/**
	 * 用户名.
	 */
	@Setter
	@Getter
	private String username;

	/**
	 * 用户密码.
	 */
	@Setter
	@Getter
	private String password;

	/**
	 * 租户编码.
	 */
	@Setter
	@Getter
	private String tenantCode;

	// @formatter:off
	/**
	 * 认证类型.
	 * mail邮箱
	 * mobile手机号
	 * username_password用户名密码
	 * authorization_code授权码
	 * test测试
	 */
	// @formatter:on
	@Setter
	@Getter
	private GrantTypeEnum grantTypeEnum;

	/**
	 * 验证码值对象.
	 */
	@Setter
	@Getter
	private CaptchaV captcha;

	/**
	 * 用户实体.
	 */
	@Getter
	private UserE user = DomainFactory.getUser();

	/**
	 * 菜单权限标识.
	 */
	@Getter
	private Set<String> permissions;

	/**
	 * 部门路径.
	 */
	@Getter
	private Set<String> deptPaths;

	/**
	 * 头像.
	 */
	@Getter
	private String avatar;

	/**
	 * 密码校验器.
	 */
	private final PasswordValidator passwordValidator;

	/**
	 * 验证码校验器.
	 */
	private final CaptchaValidator captchaValidator;

	private final AuthParamValidator authorizationCodeAuthParamValidator;

	private final AuthParamValidator mailAuthParamValidator;

	private final AuthParamValidator mobileAuthParamValidator;

	private final AuthParamValidator testAuthParamValidator;

	private final AuthParamValidator usernamePasswordAuthParamValidator;

	// @formatter:off
	public AuthA(IdGenerator idGenerator,
                 PasswordValidator passwordValidator,
                 CaptchaValidator captchaValidator,
                 @Qualifier("authorizationCodeAuthParamValidator") AuthParamValidator authorizationCodeAuthParamValidator,
                 @Qualifier("mailAuthParamValidator") AuthParamValidator mailAuthParamValidator,
                 @Qualifier("mobileAuthParamValidator") AuthParamValidator mobileAuthParamValidator,
                 @Qualifier("testAuthParamValidator") AuthParamValidator testAuthParamValidator,
                 @Qualifier("usernamePasswordAuthParamValidator") AuthParamValidator usernamePasswordAuthParamValidator) {
		this.id = idGenerator.getId();
		this.passwordValidator = passwordValidator;
		this.captchaValidator = captchaValidator;
		this.authorizationCodeAuthParamValidator = authorizationCodeAuthParamValidator;
		this.mailAuthParamValidator = mailAuthParamValidator;
		this.mobileAuthParamValidator = mobileAuthParamValidator;
		this.testAuthParamValidator = testAuthParamValidator;
		this.usernamePasswordAuthParamValidator = usernamePasswordAuthParamValidator;
	}

	// @formatter:on

	public void decryptUsernamePassword() {
		username = RSAUtils.decryptByPrivateKey(username);
		password = RSAUtils.decryptByPrivateKey(password);
	}

	public void createUserByUsernamePassword() throws Exception {
		fillUserValue(this.username, StringConstants.EMPTY, StringConstants.EMPTY);
	}

	public void createUserByMobile() throws Exception {
		fillUserValue(StringConstants.EMPTY, StringConstants.EMPTY, this.captcha.uuid());
	}

	public void createUserByMail() throws Exception {
		fillUserValue(StringConstants.EMPTY, this.captcha.uuid(), StringConstants.EMPTY);
	}

	public void createUserByAuthorizationCode() throws Exception {
		fillUserValue(this.username, StringConstants.EMPTY, StringConstants.EMPTY);
	}

	public void createUserByTest() throws Exception {
		fillUserValue(this.username, StringConstants.EMPTY, StringConstants.EMPTY);
	}

	public void getTenantId(Supplier<Long> supplier) {
		if (isDefaultTenant()) {
			this.user.setTenantId(0L);
		}
		else {
			this.user.setTenantId(supplier.get());
		}
	}

	public void getUserInfo(UserE user) {
		this.user = user;
	}

	public void getMenuPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public void getDeptPaths(List<String> deptPaths) {
		this.deptPaths = getPaths(deptPaths);
	}

	public void checkAuthParam() {
		switch (grantTypeEnum) {
			case MOBILE -> this.mobileAuthParamValidator.validateAuth(this);
			case MAIL -> this.mailAuthParamValidator.validateAuth(this);
			case USERNAME_PASSWORD -> this.usernamePasswordAuthParamValidator.validateAuth(this);
			case AUTHORIZATION_CODE -> this.authorizationCodeAuthParamValidator.validateAuth(this);
			case TEST -> this.testAuthParamValidator.validateAuth(this);
			default -> {
			}
		}
	}

	public void checkTenantId() {
		if (ObjectUtils.isNull(this.user.getTenantId())) {
			throw new BizException(OAuth2Constants.TENANT_NOT_EXIST);
		}
	}

	public void checkCaptcha() {
		if (isUseCaptcha()) {
			Boolean validate = this.captchaValidator.validateCaptcha(getCaptchaCacheKey(), captcha.captcha());
			if (ObjectUtils.isNull(validate)) {
				throw new BizException(OAuth2Constants.CAPTCHA_EXPIRED);
			}
			if (!validate) {
				throw new BizException(OAuth2Constants.CAPTCHA_ERROR);
			}
		}
	}

	public void checkUsername() {
		if (ObjectUtils.isNull(this.user)) {
			this.grantTypeEnum.checkUsernameNotExist();
		}
	}

	public void checkPassword() {
		if (isUsePassword() && !this.passwordValidator.validatePassword(this.password, user.getPassword())) {
			throw new BizException(OAuth2Constants.USERNAME_PASSWORD_ERROR);
		}
	}

	public void checkUserStatus() {
		if (ObjectUtils.equals(UserStatusEnum.DISABLE.getCode(), this.user.getStatus())) {
			throw new BizException(OAuth2Constants.USER_DISABLED);
		}
	}

	public void checkMenuPermissions() {
		if (CollectionUtils.isEmpty(this.permissions)) {
			throw new BizException(StatusCode.FORBIDDEN);
		}
	}

	public void checkDeptPaths() {
		if (CollectionUtils.isEmpty(this.deptPaths)) {
			throw new BizException(StatusCode.FORBIDDEN);
		}
	}

	public String getLoginName() {
		if (List.of(GrantTypeEnum.USERNAME_PASSWORD, GrantTypeEnum.AUTHORIZATION_CODE, GrantTypeEnum.TEST)
			.contains(grantTypeEnum)) {
			return this.username;
		}
		return this.captcha.uuid();
	}

	public void getUserAvatar(String avatar) {
		this.avatar = avatar;
	}

	private boolean isUseCaptcha() {
		return List.of(GrantTypeEnum.USERNAME_PASSWORD, GrantTypeEnum.MOBILE, GrantTypeEnum.MAIL)
			.contains(grantTypeEnum);
	}

	private boolean isUsePassword() {
		return List.of(GrantTypeEnum.USERNAME_PASSWORD, GrantTypeEnum.AUTHORIZATION_CODE, GrantTypeEnum.TEST)
			.contains(grantTypeEnum);
	}

	private Set<String> getPaths(List<String> list) {
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptySet();
		}
		// 字符串长度排序
		list.sort(Comparator.comparingInt(String::length));
		Set<String> paths = new HashSet<>(list.size());
		paths.add(list.getFirst());
		for (String path : list.subList(1, list.size())) {
			int find = paths.size();
			for (String p : paths) {
				if (path.contains(p)) {
					break;
				}
				find--;
			}
			if (find == 0) {
				paths.add(path);
			}
		}
		return paths;
	}

	private String getCaptchaCacheKey() {
		return switch (grantTypeEnum) {
			case MOBILE -> RedisKeyUtils.getMobileAuthCaptchaKey(captcha.uuid());
			case MAIL -> RedisKeyUtils.getMailAuthCaptchaKey(captcha.uuid());
			case USERNAME_PASSWORD -> RedisKeyUtils.getUsernamePasswordAuthCaptchaKey(captcha.uuid());
			case AUTHORIZATION_CODE, TEST -> StringConstants.EMPTY;
		};
	}

	private boolean isDefaultTenant() {
		return ObjectUtils.equals(Constants.DEFAULT_TENANT, tenantCode);
	}

	private void fillUserValue(String username, String mail, String mobile) throws Exception {
		this.user.setUsername(AESUtils.encrypt(username));
		this.user.setMail(AESUtils.encrypt(mail));
		this.user.setMobile(AESUtils.encrypt(mobile));
	}

}
