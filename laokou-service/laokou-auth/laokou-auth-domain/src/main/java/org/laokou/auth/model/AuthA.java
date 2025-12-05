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
import org.laokou.auth.factory.DomainFactory;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.crypto.util.RSAUtils;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 认证聚合.
 *
 * @author laokou
 */
@Entity
@Getter
public class AuthA extends AggregateRoot {

	@Serial
	private static final long serialVersionUID = 3319752558160144699L;

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
	private GrantTypeEnum grantTypeEnum;

	/**
	 * 发送验证码类型.
	 */
	private SendCaptchaTypeEnum sendCaptchaTypeEnum;

	/**
	 * 用户值对象.
	 */
	private transient UserV userV;

	/**
	 * 验证码值对象.
	 */
	private transient CaptchaV captchaV;

	/**
	 * 用户实体.
	 */
	private UserE userE;

	/**
	 * 请求值Map映射.
	 */
	private final Map<String, String[]> parameterMap;

	/**
	 * 密码校验器.
	 */
	private final PasswordValidator passwordValidator;

	/**
	 * 验证码校验器.
	 */
	private final CaptchaValidator captchaValidator;

	/**
	 * 授权码登录校验器.
	 */
	private final AuthParamValidator authorizationCodeAuthParamValidator;

	/**
	 * 邮箱登录校验器.
	 */
	private final AuthParamValidator mailAuthParamValidator;

	/**
	 * 手机号登录校验器.
	 */
	private final AuthParamValidator mobileAuthParamValidator;

	/**
	 * 测试登录校验器.
	 */
	private final AuthParamValidator testAuthParamValidator;

	/**
	 * 用户名密码登录校验器.
	 */
	private final AuthParamValidator usernamePasswordAuthParamValidator;

	/**
	 * 邮箱验证码校验器.
	 */
	private final CaptchaParamValidator mailCaptchaParamValidator;

	/**
	 * 手机验证码校验器.
	 */
	private final CaptchaParamValidator mobileCaptchaParamValidator;

	// @formatter:off
	private AuthA(IdGenerator idGenerator,
				 HttpRequest httpRequest,
                 PasswordValidator passwordValidator,
                 CaptchaValidator captchaValidator,
                 @Qualifier("authorizationCodeAuthParamValidator") AuthParamValidator authorizationCodeAuthParamValidator,
                 @Qualifier("mailAuthParamValidator") AuthParamValidator mailAuthParamValidator,
                 @Qualifier("mobileAuthParamValidator") AuthParamValidator mobileAuthParamValidator,
                 @Qualifier("testAuthParamValidator") AuthParamValidator testAuthParamValidator,
                 @Qualifier("usernamePasswordAuthParamValidator") AuthParamValidator usernamePasswordAuthParamValidator,
				 @Qualifier("mailCaptchaParamValidator") CaptchaParamValidator mailCaptchaParamValidator,
				 @Qualifier("mobileCaptchaParamValidator") CaptchaParamValidator mobileCaptchaParamValidator) {
		super(idGenerator.getId(), InstantUtils.now());
		this.parameterMap = httpRequest.getParameterMap();
		this.userE = DomainFactory.getUser();
		this.passwordValidator = passwordValidator;
		this.captchaValidator = captchaValidator;
		this.authorizationCodeAuthParamValidator = authorizationCodeAuthParamValidator;
		this.mailAuthParamValidator = mailAuthParamValidator;
		this.mobileAuthParamValidator = mobileAuthParamValidator;
		this.testAuthParamValidator = testAuthParamValidator;
		this.usernamePasswordAuthParamValidator = usernamePasswordAuthParamValidator;
		this.mailCaptchaParamValidator = mailCaptchaParamValidator;
		this.mobileCaptchaParamValidator = mobileCaptchaParamValidator;
	}
	// @formatter:on

	public AuthA createUserVByUsernamePassword() throws Exception {
		this.grantTypeEnum = GrantTypeEnum.USERNAME_PASSWORD;
		this.captchaV = getCaptchaVByUsernamePassword();
		this.userV = getUserVByUsernamePassword();
		return this;
	}

	public AuthA createUserVByMobile() throws Exception {
		this.grantTypeEnum = GrantTypeEnum.MOBILE;
		this.captchaV = getCaptchaVByMobile();
		this.userV = getUserVByMobile();
		return this;
	}

	public AuthA createUserVByMail() throws Exception {
		this.grantTypeEnum = GrantTypeEnum.MAIL;
		this.captchaV = getCaptchaVByMail();
		this.userV = getUserVByMail();
		return this;
	}

	public AuthA createUserVByAuthorizationCode() throws Exception {
		this.grantTypeEnum = GrantTypeEnum.AUTHORIZATION_CODE;
		this.userV = getUserVByAuthorizationCode();
		return this;
	}

	public AuthA createUserVByTest() throws Exception {
		this.grantTypeEnum = GrantTypeEnum.TEST;
		this.userV = getUserVByTest();
		return this;
	}

	public AuthA createCaptchaV(String uuid, String tag, String tenantCode) {
		this.sendCaptchaTypeEnum = SendCaptchaTypeEnum.getByCode(tag);
		this.captchaV = CaptchaV.builder().uuid(uuid).build();
		this.userV = UserV.builder().tenantCode(tenantCode).build();
		return this;
	}

	public void getTenantId(Supplier<Long> supplier) {
		if (isDefaultTenant()) {
			this.userV = this.userV.toBuilder().tenantId(0L).build();
		}
		else {
			this.userV = this.userV.toBuilder().tenantId(supplier.get()).build();
		}
	}

	public void getUserInfo(UserE userE) {
		this.userE = userE;
	}

	public void getMenuPermissions(Set<String> permissions) {
		this.userV = this.userV.toBuilder().permissions(permissions).build();
	}

	public void checkCaptchaParam() {
		switch (sendCaptchaTypeEnum) {
			case SEND_MAIL_CAPTCHA -> this.mailCaptchaParamValidator.validateCaptcha(this);
			case SEND_MOBILE_CAPTCHA -> this.mobileCaptchaParamValidator.validateCaptcha(this);
			default -> {
			}
		}
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
		if (ObjectUtils.isNull(this.userE.getTenantId())) {
			throw new BizException(OAuth2Constants.TENANT_NOT_EXIST);
		}
	}

	public void checkCaptcha() {
		if (isUseCaptcha()) {
			Boolean validate = this.captchaValidator.validateCaptcha(getCaptchaCacheKey(), this.captchaV.captcha());
			if (ObjectUtils.isNull(validate)) {
				throw new BizException(OAuth2Constants.CAPTCHA_EXPIRED);
			}
			if (!validate) {
				throw new BizException(OAuth2Constants.CAPTCHA_ERROR);
			}
		}
	}

	public void checkUsername() {
		if (ObjectUtils.isNull(this.userE)) {
			this.grantTypeEnum.checkUsernameNotExist();
		}
	}

	public void checkPassword() {
		if (isUsePassword()
				&& !this.passwordValidator.validatePassword(this.userV.password(), this.userE.getPassword())) {
			throw new BizException(OAuth2Constants.USERNAME_PASSWORD_ERROR);
		}
	}

	public void checkUserStatus() {
		if (ObjectUtils.equals(UserStatusEnum.DISABLE.getCode(), this.userE.getStatus())) {
			throw new BizException(OAuth2Constants.USER_DISABLED);
		}
	}

	public void checkMenuPermissions() {
		if (CollectionUtils.isEmpty(this.userV.permissions())) {
			throw new BizException(StatusCode.FORBIDDEN);
		}
	}

	public String getLoginName() {
		if (List.of(GrantTypeEnum.USERNAME_PASSWORD, GrantTypeEnum.AUTHORIZATION_CODE, GrantTypeEnum.TEST)
			.contains(grantTypeEnum)) {
			return this.userV.username();
		}
		return this.captchaV.uuid();
	}

	public void getUserAvatar(String avatar) {
		this.userV = this.userV.toBuilder().avatar(avatar).build();
	}

	private boolean isUseCaptcha() {
		return List.of(GrantTypeEnum.USERNAME_PASSWORD, GrantTypeEnum.MOBILE, GrantTypeEnum.MAIL)
			.contains(grantTypeEnum);
	}

	private boolean isUsePassword() {
		return List.of(GrantTypeEnum.USERNAME_PASSWORD, GrantTypeEnum.AUTHORIZATION_CODE, GrantTypeEnum.TEST)
			.contains(grantTypeEnum);
	}

	private String getCaptchaCacheKey() {
		return switch (grantTypeEnum) {
			case MOBILE -> RedisKeyUtils.getMobileAuthCaptchaKey(this.captchaV.uuid());
			case MAIL -> RedisKeyUtils.getMailAuthCaptchaKey(this.captchaV.uuid());
			case USERNAME_PASSWORD -> RedisKeyUtils.getUsernamePasswordAuthCaptchaKey(this.captchaV.uuid());
			case AUTHORIZATION_CODE, TEST -> StringConstants.EMPTY;
		};
	}

	private boolean isDefaultTenant() {
		return ObjectUtils.equals(Constants.DEFAULT_TENANT, this.userV.tenantCode());
	}

	private String getParameterValue(String key) {
		return parameterMap.getOrDefault(key, new String[] { StringConstants.EMPTY })[0];
	}

	private CaptchaV getCaptchaVByUsernamePassword() {
		String uuid = getParameterValue(Constants.UUID);
		String captcha = getParameterValue(Constants.CAPTCHA);
		return CaptchaV.builder().uuid(uuid).captcha(captcha).build();
	}

	private CaptchaV getCaptchaVByMobile() {
		String mobile = getParameterValue(Constants.MOBILE);
		String code = getParameterValue(Constants.CODE);
		return CaptchaV.builder().uuid(mobile).captcha(code).build();
	}

	private CaptchaV getCaptchaVByMail() {
		String mail = getParameterValue(Constants.MAIL);
		String code = getParameterValue(Constants.CODE);
		return CaptchaV.builder().uuid(mail).captcha(code).build();
	}

	private UserV getUserVByUsernamePassword() throws Exception {
		String username = RSAUtils.decryptByPrivateKey(getParameterValue(Constants.USERNAME));
		String password = RSAUtils.decryptByPrivateKey(getParameterValue(Constants.PASSWORD));
		String tenantCode = getParameterValue(Constants.TENANT_CODE);
		return UserV.builder()
			.username(AESUtils.encrypt(username))
			.password(password)
			.tenantCode(tenantCode)
			.mail(StringConstants.EMPTY)
			.mobile(StringConstants.EMPTY)
			.build();
	}

	private UserV getUserVByTest() throws Exception {
		return getUserVByAuthorizationCode();
	}

	private UserV getUserVByAuthorizationCode() throws Exception {
		String username = getParameterValue(Constants.USERNAME);
		String password = getParameterValue(Constants.PASSWORD);
		String tenantCode = getParameterValue(Constants.TENANT_CODE);
		return UserV.builder()
			.username(AESUtils.encrypt(username))
			.password(password)
			.tenantCode(tenantCode)
			.mail(StringConstants.EMPTY)
			.mobile(StringConstants.EMPTY)
			.build();
	}

	private UserV getUserVByMobile() throws Exception {
		String tenantCode = getParameterValue(Constants.TENANT_CODE);
		return UserV.builder()
			.username(StringConstants.EMPTY)
			.tenantCode(tenantCode)
			.mail(StringConstants.EMPTY)
			.mobile(AESUtils.encrypt(this.captchaV.uuid()))
			.build();
	}

	private UserV getUserVByMail() throws Exception {
		String tenantCode = getParameterValue(Constants.TENANT_CODE);
		return UserV.builder()
			.username(StringConstants.EMPTY)
			.tenantCode(tenantCode)
			.mail(AESUtils.encrypt(this.captchaV.uuid()))
			.mobile(StringConstants.EMPTY)
			.build();
	}

}
