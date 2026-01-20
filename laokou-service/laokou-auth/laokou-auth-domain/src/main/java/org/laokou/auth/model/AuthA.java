/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.auth.model.constant.Constants;
import org.laokou.auth.model.constant.OAuth2Constants;
import org.laokou.auth.model.entity.UserE;
import org.laokou.auth.model.enums.DataScope;
import org.laokou.auth.model.enums.GrantType;
import org.laokou.auth.model.enums.SendCaptchaTypeEnum;
import org.laokou.auth.model.enums.UserStatus;
import org.laokou.auth.model.exception.CaptchaErrorException;
import org.laokou.auth.model.exception.CaptchaExpiredException;
import org.laokou.auth.model.exception.DeptNotFoundException;
import org.laokou.auth.model.exception.PasswordErrorException;
import org.laokou.auth.model.exception.TenantNotFoundException;
import org.laokou.auth.model.exception.UserDisabledException;
import org.laokou.auth.model.exception.UserForbiddenException;
import org.laokou.auth.model.function.HttpRequest;
import org.laokou.auth.model.validator.AuthParamValidator;
import org.laokou.auth.model.validator.CaptchaParamValidator;
import org.laokou.auth.model.validator.CaptchaValidator;
import org.laokou.auth.model.validator.PasswordValidator;
import org.laokou.auth.model.valueobject.CaptchaV;
import org.laokou.auth.model.valueobject.DataFilterV;
import org.laokou.auth.model.valueobject.UserV;
import org.laokou.common.core.util.RandomStringUtils;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.crypto.util.RSAUtils;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.dto.ValidateName;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.util.Collections;
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
public class AuthA extends AggregateRoot implements ValidateName {

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
	private GrantType grantType;

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
	 * 数据权限值对象.
	 */
	private transient DataFilterV dataFilterV;

	/**
	 * 用户实体.
	 */
	private UserE userE;

	/**
	 * ID生成器.
	 */
	private final transient IdGenerator idGenerator;

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
		this.idGenerator = idGenerator;
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

	public AuthA createUserVByUsernamePasswordAuth() throws Exception {
		this.grantType = GrantType.USERNAME_PASSWORD;
		this.captchaV = getCaptchaVByUsernamePasswordAuth();
		this.userV = getUserVByUsernamePasswordAuth();
		return init();
	}

	public AuthA createUserVByMobileAuth() throws Exception {
		this.grantType = GrantType.MOBILE;
		this.captchaV = getCaptchaVByMobileAuth();
		this.userV = getUserVByMobileAuth();
		return init();
	}

	public AuthA createUserVByMailAuth() throws Exception {
		this.grantType = GrantType.MAIL;
		this.captchaV = getCaptchaVByMailAuth();
		this.userV = getUserVByMailAuth();
		return init();
	}

	public AuthA createUserVByAuthorizationCodeAuth() throws Exception {
		this.grantType = GrantType.AUTHORIZATION_CODE;
		this.userV = getUserVByAuthorizationCodeAuth();
		return init();
	}

	public AuthA createUserVByTestAuth() throws Exception {
		this.grantType = GrantType.TEST;
		this.userV = getUserVByTestAuth();
		return init();
	}

	public AuthA createCaptchaVBySend(String uuid, String tag, String tenantCode) {
		this.sendCaptchaTypeEnum = SendCaptchaTypeEnum.getByCode(tag);
		this.captchaV = CaptchaV.builder().uuid(uuid).build();
		this.userV = UserV.builder().tenantCode(tenantCode).build();
		return init();
	}

	public String getCaptchaBySend() {
		this.captchaV = this.captchaV.toBuilder().captcha(RandomStringUtils.randomNumeric()).build();
		return this.captchaV.captcha();
	}

	public String getCaptchaCacheKeyBySend() {
		return sendCaptchaTypeEnum.getCaptchaCacheKey(this.captchaV.uuid());
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

	public void getDataFilter(Set<String> dataScopes, Supplier<Set<Long>> deptIdsSupplier) {
		if (CollectionUtils.isEmpty(dataScopes)) {
			this.dataFilterV = null;
			return;
		}
		if (dataScopes.contains(DataScope.ALL.getCode())) {
			this.dataFilterV = DataFilterV.builder().deptIds(Collections.emptySet()).creator(null).build();
		}
		else {
			Set<Long> deptIds = Collections.emptySet();
			Long creator = null;
			if (dataScopes.contains(DataScope.BELOW_DEPT.getCode())
					|| dataScopes.contains(DataScope.SELF_DEPT.getCode())
					|| dataScopes.contains(DataScope.CUSTOM.getCode())) {
				deptIds = deptIdsSupplier.get();
			}
			if (dataScopes.contains(DataScope.SELF.getCode())) {
				creator = this.userE.getId();
			}
			this.dataFilterV = DataFilterV.builder().deptIds(deptIds).creator(creator).build();
		}
	}

	public void checkCaptchaParam() {
		switch (sendCaptchaTypeEnum) {
			case SEND_MAIL_CAPTCHA -> this.mailCaptchaParamValidator.validateCaptcha(this);
			case SEND_MOBILE_CAPTCHA -> this.mobileCaptchaParamValidator.validateCaptcha(this);
			default -> throw new UnsupportedOperationException("Unsupported captcha type");
		}
	}

	public void checkAuthParam() {
		switch (grantType) {
			case MOBILE -> this.mobileAuthParamValidator.validateAuth(this);
			case MAIL -> this.mailAuthParamValidator.validateAuth(this);
			case USERNAME_PASSWORD -> this.usernamePasswordAuthParamValidator.validateAuth(this);
			case AUTHORIZATION_CODE -> this.authorizationCodeAuthParamValidator.validateAuth(this);
			case TEST -> this.testAuthParamValidator.validateAuth(this);
			default -> throw new UnsupportedOperationException("Unsupported grant type");
		}
	}

	public void checkTenantId() {
		if (ObjectUtils.isNull(this.userV.tenantId())) {
			throw new TenantNotFoundException(OAuth2Constants.TENANT_NOT_FOUND);
		}
	}

	public void checkDeptId() {
		if (ObjectUtils.isNull(this.userE.getDeptId())) {
			throw new DeptNotFoundException(OAuth2Constants.DEPT_NOT_FOUND);
		}
	}

	public void checkCaptcha() {
		if (isUseCaptcha()) {
			Boolean validate = this.captchaValidator.validateCaptcha(getCaptchaCacheKeyByAuth(),
					this.captchaV.captcha());
			if (ObjectUtils.isNull(validate)) {
				throw new CaptchaExpiredException(OAuth2Constants.CAPTCHA_EXPIRED);
			}
			if (!validate) {
				throw new CaptchaErrorException(OAuth2Constants.CAPTCHA_ERROR);
			}
		}
	}

	public void checkUsername() {
		if (ObjectUtils.isNull(this.userE)) {
			this.grantType.checkUsernameNotFound();
		}
	}

	public void checkPassword() {
		if (isUsePassword()
				&& !this.passwordValidator.validatePassword(this.userV.password(), this.userE.getPassword())) {
			throw new PasswordErrorException(OAuth2Constants.USERNAME_PASSWORD_ERROR);
		}
	}

	public void checkUserStatus() {
		if (ObjectUtils.equals(UserStatus.DISABLE.getCode(), this.userE.getStatus())) {
			throw new UserDisabledException(OAuth2Constants.USER_DISABLED);
		}
	}

	public void checkMenuPermissions() {
		if (CollectionUtils.isEmpty(this.userV.permissions())) {
			throw new UserForbiddenException(StatusCode.FORBIDDEN);
		}
	}

	public void checkDataFilter() {
		if (ObjectUtils.isNull(dataFilterV)) {
			throw new UserForbiddenException(StatusCode.FORBIDDEN);
		}
	}

	public String getLoginName() {
		if (List.of(GrantType.USERNAME_PASSWORD, GrantType.AUTHORIZATION_CODE, GrantType.TEST).contains(grantType)) {
			return this.userV.username();
		}
		return this.captchaV.uuid();
	}

	public void getUserAvatar(String avatar) {
		this.userV = this.userV.toBuilder().avatar(avatar).build();
	}

	@Override
	public String getValidateName() {
		return "OAuth2";
	}

	private boolean isUseCaptcha() {
		return List.of(GrantType.USERNAME_PASSWORD, GrantType.MOBILE, GrantType.MAIL).contains(grantType);
	}

	private boolean isUsePassword() {
		return List.of(GrantType.USERNAME_PASSWORD, GrantType.AUTHORIZATION_CODE, GrantType.TEST).contains(grantType);
	}

	private String getCaptchaCacheKeyByAuth() {
		return switch (grantType) {
			case MOBILE -> RedisKeyUtils.getMobileAuthCaptchaKey(this.captchaV.uuid());
			case MAIL -> RedisKeyUtils.getMailAuthCaptchaKey(this.captchaV.uuid());
			case USERNAME_PASSWORD -> RedisKeyUtils.getUsernamePasswordAuthCaptchaKey(this.captchaV.uuid());
			case AUTHORIZATION_CODE, TEST -> throw new UnsupportedOperationException("Unsupported grant type");
		};
	}

	private boolean isDefaultTenant() {
		return ObjectUtils.equals(Constants.DEFAULT_TENANT, this.userV.tenantCode());
	}

	private String getParameterValue(String key) {
		return parameterMap.getOrDefault(key, new String[] { StringConstants.EMPTY })[0];
	}

	private CaptchaV getCaptchaVByUsernamePasswordAuth() {
		String uuid = getParameterValue(Constants.UUID);
		String captcha = getParameterValue(Constants.CAPTCHA);
		return CaptchaV.builder().uuid(uuid).captcha(captcha).build();
	}

	private CaptchaV getCaptchaVByMobileAuth() {
		String mobile = getParameterValue(Constants.MOBILE);
		String code = getParameterValue(Constants.CODE);
		return CaptchaV.builder().uuid(mobile).captcha(code).build();
	}

	private CaptchaV getCaptchaVByMailAuth() {
		String mail = getParameterValue(Constants.MAIL);
		String code = getParameterValue(Constants.CODE);
		return CaptchaV.builder().uuid(mail).captcha(code).build();
	}

	private UserV getUserVByUsernamePasswordAuth() throws Exception {
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

	private UserV getUserVByTestAuth() throws Exception {
		return getUserVByAuthorizationCodeAuth();
	}

	private UserV getUserVByAuthorizationCodeAuth() throws Exception {
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

	private UserV getUserVByMobileAuth() throws Exception {
		String tenantCode = getParameterValue(Constants.TENANT_CODE);
		return UserV.builder()
			.username(StringConstants.EMPTY)
			.tenantCode(tenantCode)
			.mail(StringConstants.EMPTY)
			.mobile(AESUtils.encrypt(this.captchaV.uuid()))
			.build();
	}

	private UserV getUserVByMailAuth() throws Exception {
		String tenantCode = getParameterValue(Constants.TENANT_CODE);
		return UserV.builder()
			.username(StringConstants.EMPTY)
			.tenantCode(tenantCode)
			.mail(AESUtils.encrypt(this.captchaV.uuid()))
			.mobile(StringConstants.EMPTY)
			.build();
	}

	private AuthA init() {
		super.id = idGenerator.getId();
		super.createTime = InstantUtils.now();
		return this;
	}

}
