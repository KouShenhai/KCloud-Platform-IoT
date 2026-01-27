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

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.CaptchaGateway;
import org.laokou.auth.gateway.MenuGateway;
import org.laokou.auth.gateway.OssLogGateway;
import org.laokou.auth.gateway.TenantGateway;
import org.laokou.auth.gateway.UserGateway;
import org.laokou.auth.model.constant.Constants;
import org.laokou.auth.model.entity.UserE;
import org.laokou.auth.model.enums.GrantType;
import org.laokou.auth.model.enums.SuperAdmin;
import org.laokou.auth.model.enums.UserStatus;
import org.laokou.auth.model.exception.UsernameNotFoundException;
import org.laokou.auth.model.function.HttpRequest;
import org.laokou.auth.model.validator.AuthParamValidator;
import org.laokou.auth.model.validator.CaptchaParamValidator;
import org.laokou.auth.model.validator.CaptchaValidator;
import org.laokou.auth.model.validator.PasswordValidator;
import org.laokou.auth.model.valueobject.CaptchaV;
import org.laokou.auth.model.valueobject.UserV;
import org.laokou.common.core.util.RegexUtils;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.crypto.util.RSAUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 领域服务测试.
 *
 * @author laokou
 */
@RequiredArgsConstructor
@DisplayName("Domain Service Tests")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = DomainServiceTest.TestConfig.class)
class DomainServiceTest {

	private final DomainService domainService;

	@MockitoBean
	private UserGateway userGateway;

	@MockitoBean
	private MenuGateway menuGateway;

	@MockitoBean
	private TenantGateway tenantGateway;

	@MockitoBean
	private CaptchaGateway captchaGateway;

	@MockitoBean
	private PasswordValidator passwordValidator;

	@MockitoBean
	private CaptchaValidator captchaValidator;

	@MockitoBean
	private IdGenerator idGenerator;

	@MockitoBean
	private HttpRequest httpRequest;

	@MockitoBean
	private OssLogGateway ossLogGateway;

	@MockitoBean("authorizationCodeAuthParamValidator")
	private AuthParamValidator authorizationCodeAuthParamValidator;

	@MockitoBean("mailAuthParamValidator")
	private AuthParamValidator mailAuthParamValidator;

	@MockitoBean("mobileAuthParamValidator")
	private AuthParamValidator mobileAuthParamValidator;

	@MockitoBean("testAuthParamValidator")
	private AuthParamValidator testAuthParamValidator;

	@MockitoBean("usernamePasswordAuthParamValidator")
	private AuthParamValidator usernamePasswordAuthParamValidator;

	@MockitoBean("mailCaptchaParamValidator")
	private CaptchaParamValidator mailCaptchaParamValidator;

	@MockitoBean("mobileCaptchaParamValidator")
	private CaptchaParamValidator mobileCaptchaParamValidator;

	private final String uuid = "070d5f1650ea4832951ded2cce5b4386";

	private final String captcha = "1234";

	private final String username = "admin";

	private final String password = "admin123";

	private final String tenantCode = "laokou";

	private final String mail = "2413176044@qq.com";

	private final String mobile = "18888888888";

	private final Long tenantId = 1L;

	@Test
	@DisplayName("Test username password auth success")
	void test_auth_usernamePassword_success() throws Exception {
		createUsernamePasswordAuthInfo(createUsernamePasswordAuthParam());
		AuthA usernamePasswordAuth = createAuth().createUsernamePasswordAuth();
		Assertions.assertThat(usernamePasswordAuth).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(usernamePasswordAuth)).doesNotThrowAnyException();
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId(this.tenantCode);
		Mockito.verify(httpRequest, Mockito.times(1)).getParameterMap();
		Mockito.verify(captchaValidator, Mockito.times(1))
			.validateCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey(this.uuid), this.captcha);
		Mockito.verify(passwordValidator, Mockito.times(1)).validatePassword(this.password, this.password);
		Mockito.verify(userGateway, Mockito.times(1)).getUserProfile(createUserVByUsernamePassword());
		Mockito.verify(menuGateway, Mockito.times(1)).getMenuPermissions(createUserE());
		Mockito.verify(ossLogGateway, Mockito.times(1)).getOssUrl(1L);
		Assertions.assertThat(usernamePasswordAuth.getUserV().avatar()).isEqualTo("https://1.png");
		Assertions.assertThat(usernamePasswordAuth.getUserE().getDeptId()).isEqualTo(1L);
		Mockito.verify(usernamePasswordAuthParamValidator, Mockito.times(1)).validateAuth(usernamePasswordAuth);
	}

	@Test
	@DisplayName("Test username password auth with username not found")
	void test_auth_usernamePassword_with_usernameNotFound() throws Exception {
		createUsernamePasswordAuthInfo(createUsernamePasswordAuthParamWithUsernameNotFound());
		AuthA usernamePasswordAuthParamWithUsernameNotFound = createAuth().createUsernamePasswordAuth();
		Assertions.assertThat(usernamePasswordAuthParamWithUsernameNotFound).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(usernamePasswordAuthParamWithUsernameNotFound))
			.isInstanceOf(UsernameNotFoundException.class);
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId(this.tenantCode);
		Mockito.verify(httpRequest, Mockito.times(1)).getParameterMap();
		Mockito.verify(captchaValidator, Mockito.times(1))
			.validateCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey(this.uuid), this.captcha);
		Mockito.verify(userGateway, Mockito.times(1))
			.getUserProfile(createUserVByUsernamePasswordWithUsernameNotFound());
		Mockito.verify(usernamePasswordAuthParamValidator, Mockito.times(1))
			.validateAuth(usernamePasswordAuthParamWithUsernameNotFound);
	}

	@Test
	@DisplayName("Test username password auth validate empty value")
	void test_auth_usernamePassword_validateEmptyValue() throws Exception {
		createUsernamePasswordAuthInfo(createUsernamePasswordEmptyAuthParam());
		AuthA usernamePasswordAuth = createAuth().createUsernamePasswordAuth();
		Assertions.assertThat(usernamePasswordAuth).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(usernamePasswordAuth))
			.isInstanceOf(IllegalArgumentException.class);
		Mockito.verify(usernamePasswordAuthParamValidator, Mockito.times(1)).validateAuth(usernamePasswordAuth);
	}

	@Test
	@DisplayName("Test mail auth success")
	void test_auth_mail_auth_success() throws Exception {
		createMailAuthInfo(createMailAuthParam());
		AuthA mailAuth = createAuth().createMailAuth();
		Assertions.assertThat(mailAuth).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(mailAuth)).doesNotThrowAnyException();
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId(this.tenantCode);
		Mockito.verify(httpRequest, Mockito.times(1)).getParameterMap();
		Mockito.verify(captchaValidator, Mockito.times(1))
			.validateCaptcha(RedisKeyUtils.getMailAuthCaptchaKey(this.mail), this.captcha);
		Mockito.verify(userGateway, Mockito.times(1)).getUserProfile(createUserVByMail());
		Mockito.verify(menuGateway, Mockito.times(1)).getMenuPermissions(createUserE());
		Mockito.verify(ossLogGateway, Mockito.times(1)).getOssUrl(1L);
		Assertions.assertThat(mailAuth.getUserV().avatar()).isEqualTo("https://1.png");
		Assertions.assertThat(mailAuth.getUserE().getDeptId()).isEqualTo(1L);
		Mockito.verify(mailAuthParamValidator, Mockito.times(1)).validateAuth(mailAuth);
	}

	@Test
	@DisplayName("Test mail auth validate empty value")
	void test_auth_mail_validateEmptyValue() throws Exception {
		createMailAuthInfo(createMailEmptyAuthParam());
		AuthA mailAuth = createAuth().createMailAuth();
		Assertions.assertThat(mailAuth).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(mailAuth)).isInstanceOf(IllegalArgumentException.class);
		Mockito.verify(mailAuthParamValidator, Mockito.times(1)).validateAuth(mailAuth);
	}

	@Test
	@DisplayName("Test mobile auth success")
	void test_auth_mobile_auth_success() throws Exception {
		createMobileAuthInfo(createMobileAuthParam());
		AuthA mobileAuth = createAuth().createMobileAuth();
		Assertions.assertThat(mobileAuth).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(mobileAuth)).doesNotThrowAnyException();
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId(this.tenantCode);
		Mockito.verify(httpRequest, Mockito.times(1)).getParameterMap();
		Mockito.verify(captchaValidator, Mockito.times(1))
			.validateCaptcha(RedisKeyUtils.getMobileAuthCaptchaKey(this.mobile), this.captcha);
		Mockito.verify(userGateway, Mockito.times(1)).getUserProfile(createUserVByMobile());
		Mockito.verify(menuGateway, Mockito.times(1)).getMenuPermissions(createUserE());
		Mockito.verify(ossLogGateway, Mockito.times(1)).getOssUrl(1L);
		Assertions.assertThat(mobileAuth.getUserV().avatar()).isEqualTo("https://1.png");
		Assertions.assertThat(mobileAuth.getUserE().getDeptId()).isEqualTo(1L);
		Mockito.verify(mobileAuthParamValidator, Mockito.times(1)).validateAuth(mobileAuth);
	}

	@Test
	@DisplayName("Test mobile auth validate empty value")
	void test_auth_mobile_validateEmptyValue() throws Exception {
		createMobileAuthInfo(createMobileEmptyAuthParam());
		AuthA mobileAuth = createAuth().createMobileAuth();
		Assertions.assertThat(mobileAuth).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(mobileAuth)).isInstanceOf(IllegalArgumentException.class);
		Mockito.verify(mobileAuthParamValidator, Mockito.times(1)).validateAuth(mobileAuth);
	}

	@Test
	@DisplayName("Test test auth validate empty value")
	void test_auth_test_validateEmptyValue() throws Exception {
		createTestAuthInfo(createTestEmptyAuthParam());
		AuthA testAuth = createAuth().createTestAuth();
		Assertions.assertThat(testAuth).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(testAuth)).isInstanceOf(IllegalArgumentException.class);
		Mockito.verify(testAuthParamValidator, Mockito.times(1)).validateAuth(testAuth);
	}

	@Test
	@DisplayName("Test username password auth success")
	void test_auth_test_success() throws Exception {
		createTestAuthInfo(createTestAuthParam());
		AuthA testAuth = createAuth().createTestAuth();
		Assertions.assertThat(testAuth).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(testAuth)).doesNotThrowAnyException();
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId(this.tenantCode);
		Mockito.verify(httpRequest, Mockito.times(1)).getParameterMap();
		Mockito.verify(passwordValidator, Mockito.times(1)).validatePassword(this.password, this.password);
		Mockito.verify(userGateway, Mockito.times(1)).getUserProfile(createUserVByTest());
		Mockito.verify(menuGateway, Mockito.times(1)).getMenuPermissions(createUserE());
		Mockito.verify(ossLogGateway, Mockito.times(1)).getOssUrl(1L);
		Assertions.assertThat(testAuth.getUserV().avatar()).isEqualTo("https://1.png");
		Assertions.assertThat(testAuth.getUserE().getDeptId()).isEqualTo(1L);
		Mockito.verify(testAuthParamValidator, Mockito.times(1)).validateAuth(testAuth);
	}

	@Test
	@DisplayName("Test authorization code password auth success")
	void test_auth_authorizationCode_success() throws Exception {
		createAuthorizationCodeAuthInfo(createAuthorizationCodeAuthParam());
		AuthA authorizationCodeAuth = createAuth().createAuthorizationCodeAuth();
		Assertions.assertThat(authorizationCodeAuth).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(authorizationCodeAuth)).doesNotThrowAnyException();
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId(this.tenantCode);
		Mockito.verify(httpRequest, Mockito.times(1)).getParameterMap();
		Mockito.verify(passwordValidator, Mockito.times(1)).validatePassword(this.password, this.password);
		Mockito.verify(userGateway, Mockito.times(1)).getUserProfile(createUserVByAuthorizationCode());
		Mockito.verify(menuGateway, Mockito.times(1)).getMenuPermissions(createUserE());
		Mockito.verify(ossLogGateway, Mockito.times(1)).getOssUrl(1L);
		Assertions.assertThat(authorizationCodeAuth.getUserV().avatar()).isEqualTo("https://1.png");
		Assertions.assertThat(authorizationCodeAuth.getUserE().getDeptId()).isEqualTo(1L);
		Mockito.verify(authorizationCodeAuthParamValidator, Mockito.times(1)).validateAuth(authorizationCodeAuth);
	}

	@Test
	@DisplayName("Test authorization code auth validate empty value")
	void test_auth_authorizationCode_validateEmptyValue() throws Exception {
		createAuthorizationCodeAuthInfo(createAuthorizationCodeEmptyAuthParam());
		AuthA authorizationCodeAuth = createAuth().createAuthorizationCodeAuth();
		Assertions.assertThat(authorizationCodeAuth).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(authorizationCodeAuth))
			.isInstanceOf(IllegalArgumentException.class);
		Mockito.verify(authorizationCodeAuthParamValidator, Mockito.times(1)).validateAuth(authorizationCodeAuth);
	}

	private void createMailAuthInfo(MailAuthParam mailAuthParam) {
		Mockito.when(idGenerator.getId()).thenReturn(1L);
		Mockito.doAnswer(invocation -> {
			AuthA authA = invocation.getArgument(0);
			UserV userV = authA.getUserV();
			CaptchaV captchaV = authA.getCaptchaV();
			if (StringExtUtils.isEmpty(userV.tenantCode())) {
				throw new IllegalArgumentException("tenant code must not be empty");
			}
			if (StringExtUtils.isEmpty(captchaV.captcha())) {
				throw new IllegalArgumentException("captcha must not be empty");
			}
			if (StringExtUtils.isEmpty(captchaV.uuid())) {
				throw new IllegalArgumentException("mail must not be empty");
			}
			else if (!RegexUtils.mailRegex(captchaV.uuid())) {
				throw new IllegalArgumentException("mail must match");
			}
			return null;
		}).when(mailAuthParamValidator).validateAuth(Mockito.any());
		Mockito.when(captchaValidator.validateCaptcha(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(tenantGateway.getTenantId(Mockito.anyString())).thenReturn(1L);
		Mockito.when(userGateway.getUserProfile(Mockito.any())).thenReturn(createUserE());
		Mockito.when(menuGateway.getMenuPermissions(Mockito.any())).thenReturn(List.of("sys:user:save"));
		Mockito.when(ossLogGateway.getOssUrl(Mockito.anyLong())).thenReturn("https://1.png");
		Map<String, String[]> params = new HashMap<>(4);
		params.put(Constants.MAIL, new String[] { mailAuthParam.mail() });
		params.put(Constants.CODE, new String[] { mailAuthParam.code() });
		params.put(Constants.TENANT_CODE, new String[] { mailAuthParam.tenantCode() });
		params.put(Constants.GRANT_TYPE, new String[] { mailAuthParam.grantType() });
		Mockito.when(httpRequest.getParameterMap()).thenReturn(params);
	}

	private void createMobileAuthInfo(MobileAuthParam mobileAuthParam) {
		Mockito.when(idGenerator.getId()).thenReturn(1L);
		Mockito.doAnswer(invocation -> {
			AuthA authA = invocation.getArgument(0);
			UserV userV = authA.getUserV();
			CaptchaV captchaV = authA.getCaptchaV();
			if (StringExtUtils.isEmpty(userV.tenantCode())) {
				throw new IllegalArgumentException("tenant code must not be empty");
			}
			if (StringExtUtils.isEmpty(captchaV.captcha())) {
				throw new IllegalArgumentException("captcha must not be empty");
			}
			if (StringExtUtils.isEmpty(captchaV.uuid())) {
				throw new IllegalArgumentException("mobile must not be empty");
			}
			else if (!RegexUtils.mobileRegex(captchaV.uuid())) {
				throw new IllegalArgumentException("mobile must match");
			}
			return null;
		}).when(mobileAuthParamValidator).validateAuth(Mockito.any());
		Mockito.when(captchaValidator.validateCaptcha(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(tenantGateway.getTenantId(Mockito.anyString())).thenReturn(1L);
		Mockito.when(userGateway.getUserProfile(Mockito.any())).thenReturn(createUserE());
		Mockito.when(menuGateway.getMenuPermissions(Mockito.any())).thenReturn(List.of("sys:user:save"));
		Mockito.when(ossLogGateway.getOssUrl(Mockito.anyLong())).thenReturn("https://1.png");
		Map<String, String[]> params = new HashMap<>(4);
		params.put(Constants.MOBILE, new String[] { mobileAuthParam.mobile() });
		params.put(Constants.CODE, new String[] { mobileAuthParam.code() });
		params.put(Constants.TENANT_CODE, new String[] { mobileAuthParam.tenantCode() });
		params.put(Constants.GRANT_TYPE, new String[] { mobileAuthParam.grantType() });
		Mockito.when(httpRequest.getParameterMap()).thenReturn(params);
	}

	private void createTestAuthInfo(TestAuthParam testAuthParam) {
		Mockito.when(idGenerator.getId()).thenReturn(1L);
		Mockito.doAnswer(invocation -> {
			AuthA authA = invocation.getArgument(0);
			UserV userV = authA.getUserV();
			if (StringExtUtils.isEmpty(userV.tenantCode())) {
				throw new IllegalArgumentException("tenant code must not be empty");
			}
			if (StringExtUtils.isEmpty(userV.username())) {
				throw new IllegalArgumentException("username must not be empty");
			}
			if (StringExtUtils.isEmpty(userV.password())) {
				throw new IllegalArgumentException("password must not be empty");
			}
			return null;
		}).when(testAuthParamValidator).validateAuth(Mockito.any());
		Mockito.when(passwordValidator.validatePassword(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(tenantGateway.getTenantId(Mockito.anyString())).thenReturn(1L);
		Mockito.when(userGateway.getUserProfile(Mockito.any())).thenReturn(createUserE());
		Mockito.when(menuGateway.getMenuPermissions(Mockito.any())).thenReturn(List.of("sys:user:save"));
		Mockito.when(ossLogGateway.getOssUrl(Mockito.anyLong())).thenReturn("https://1.png");
		Map<String, String[]> params = new HashMap<>(4);
		params.put(Constants.USERNAME, new String[] { testAuthParam.username() });
		params.put(Constants.PASSWORD, new String[] { testAuthParam.password() });
		params.put(Constants.TENANT_CODE, new String[] { testAuthParam.tenantCode() });
		params.put(Constants.GRANT_TYPE, new String[] { testAuthParam.grantType() });
		Mockito.when(httpRequest.getParameterMap()).thenReturn(params);
	}

	private void createAuthorizationCodeAuthInfo(AuthorizationCodAuthParam authorizationCodeAuthParam) {
		Mockito.when(idGenerator.getId()).thenReturn(1L);
		Mockito.doAnswer(invocation -> {
			AuthA authA = invocation.getArgument(0);
			UserV userV = authA.getUserV();
			if (StringExtUtils.isEmpty(userV.tenantCode())) {
				throw new IllegalArgumentException("tenant code must not be empty");
			}
			if (StringExtUtils.isEmpty(userV.username())) {
				throw new IllegalArgumentException("username must not be empty");
			}
			if (StringExtUtils.isEmpty(userV.password())) {
				throw new IllegalArgumentException("password must not be empty");
			}
			return null;
		}).when(authorizationCodeAuthParamValidator).validateAuth(Mockito.any());
		Mockito.when(passwordValidator.validatePassword(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(tenantGateway.getTenantId(Mockito.anyString())).thenReturn(1L);
		Mockito.when(userGateway.getUserProfile(Mockito.any())).thenReturn(createUserE());
		Mockito.when(menuGateway.getMenuPermissions(Mockito.any())).thenReturn(List.of("sys:user:save"));
		Mockito.when(ossLogGateway.getOssUrl(Mockito.anyLong())).thenReturn("https://1.png");
		Map<String, String[]> params = new HashMap<>(4);
		params.put(Constants.USERNAME, new String[] { authorizationCodeAuthParam.username() });
		params.put(Constants.PASSWORD, new String[] { authorizationCodeAuthParam.password() });
		params.put(Constants.TENANT_CODE, new String[] { authorizationCodeAuthParam.tenantCode() });
		params.put(Constants.GRANT_TYPE, new String[] { authorizationCodeAuthParam.grantType() });
		Mockito.when(httpRequest.getParameterMap()).thenReturn(params);
	}

	private void createUsernamePasswordAuthInfo(UsernamePasswordAuthParam usernamePasswordAuthParam) {
		Mockito.when(idGenerator.getId()).thenReturn(1L);
		Mockito.doAnswer(invocation -> {
			AuthA authA = invocation.getArgument(0);
			UserV userV = authA.getUserV();
			CaptchaV captchaV = authA.getCaptchaV();
			if (StringExtUtils.isEmpty(userV.tenantCode())) {
				throw new IllegalArgumentException("tenant code must not be empty");
			}
			if (StringExtUtils.isEmpty(captchaV.uuid())) {
				throw new IllegalArgumentException("uuid must not be empty");
			}
			if (StringExtUtils.isEmpty(captchaV.captcha())) {
				throw new IllegalArgumentException("captcha must not be empty");
			}
			if (StringExtUtils.isEmpty(userV.username())) {
				throw new IllegalArgumentException("username must not be empty");
			}
			if (StringExtUtils.isEmpty(userV.password())) {
				throw new IllegalArgumentException("password must not be empty");
			}
			return null;
		}).when(usernamePasswordAuthParamValidator).validateAuth(Mockito.any());
		Mockito.when(passwordValidator.validatePassword(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(captchaValidator.validateCaptcha(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(tenantGateway.getTenantId(Mockito.anyString())).thenReturn(1L);
		if (ObjectUtils.equals(this.username, RSAUtils.decryptByPrivateKey(usernamePasswordAuthParam.username()))) {
			Mockito.when(userGateway.getUserProfile(Mockito.any())).thenReturn(createUserE());
		}
		else {
			Mockito.when(userGateway.getUserProfile(Mockito.any())).thenReturn(null);
		}
		Mockito.when(menuGateway.getMenuPermissions(Mockito.any())).thenReturn(List.of("sys:user:save"));
		Mockito.when(ossLogGateway.getOssUrl(Mockito.anyLong())).thenReturn("https://1.png");
		Map<String, String[]> params = new HashMap<>(6);
		params.put(Constants.USERNAME, new String[] { usernamePasswordAuthParam.username() });
		params.put(Constants.PASSWORD, new String[] { usernamePasswordAuthParam.password() });
		params.put(Constants.TENANT_CODE, new String[] { usernamePasswordAuthParam.tenantCode() });
		params.put(Constants.UUID, new String[] { usernamePasswordAuthParam.uuid() });
		params.put(Constants.CAPTCHA, new String[] { usernamePasswordAuthParam.captcha() });
		params.put(Constants.GRANT_TYPE, new String[] { usernamePasswordAuthParam.grantType() });
		Mockito.when(httpRequest.getParameterMap()).thenReturn(params);
	}

	private UsernamePasswordAuthParam createUsernamePasswordAuthParam() {
		String username = RSAUtils.encryptByPublicKey(this.username);
		String password = RSAUtils.encryptByPublicKey(this.password);
		return new UsernamePasswordAuthParam(this.uuid, this.captcha, username, password, this.tenantCode,
				GrantType.USERNAME_PASSWORD.getCode());
	}

	private UsernamePasswordAuthParam createUsernamePasswordEmptyAuthParam() {
		return new UsernamePasswordAuthParam(null, null, null, null, null, GrantType.USERNAME_PASSWORD.getCode());
	}

	private UsernamePasswordAuthParam createUsernamePasswordAuthParamWithUsernameNotFound() {
		String username = RSAUtils.encryptByPublicKey("test");
		String password = RSAUtils.encryptByPublicKey(this.password);
		return new UsernamePasswordAuthParam(this.uuid, this.captcha, username, password, this.tenantCode,
				GrantType.USERNAME_PASSWORD.getCode());
	}

	private MailAuthParam createMailAuthParam() {
		return new MailAuthParam(this.mail, this.captcha, this.tenantCode, GrantType.MAIL.getCode());
	}

	private MobileAuthParam createMobileAuthParam() {
		return new MobileAuthParam(this.mobile, this.captcha, this.tenantCode, GrantType.MOBILE.getCode());
	}

	private TestAuthParam createTestAuthParam() {
		return new TestAuthParam(this.username, this.password, this.tenantCode, GrantType.TEST.getCode());
	}

	private AuthorizationCodAuthParam createAuthorizationCodeAuthParam() {
		return new AuthorizationCodAuthParam(this.username, this.password, this.tenantCode,
				GrantType.AUTHORIZATION_CODE.getCode());
	}

	private MailAuthParam createMailEmptyAuthParam() {
		return new MailAuthParam(null, null, null, GrantType.MAIL.getCode());
	}

	private MobileAuthParam createMobileEmptyAuthParam() {
		return new MobileAuthParam(null, null, null, GrantType.MOBILE.getCode());
	}

	private TestAuthParam createTestEmptyAuthParam() {
		return new TestAuthParam(null, null, null, GrantType.TEST.getCode());
	}

	private AuthorizationCodAuthParam createAuthorizationCodeEmptyAuthParam() {
		return new AuthorizationCodAuthParam(null, null, null, GrantType.AUTHORIZATION_CODE.getCode());
	}

	private UserV createUserVByUsernamePassword() throws Exception {
		return new UserV(AESUtils.encrypt(this.username), this.password, null, StringConstants.EMPTY,
				StringConstants.EMPTY, this.tenantCode, tenantId, null);
	}

	private UserV createUserVByUsernamePasswordWithUsernameNotFound() throws Exception {
		return new UserV(AESUtils.encrypt("test"), this.password, null, StringConstants.EMPTY, StringConstants.EMPTY,
				this.tenantCode, tenantId, null);
	}

	private UserV createUserVByMail() throws Exception {
		return new UserV(StringConstants.EMPTY, null, null, AESUtils.encrypt(this.mail), StringConstants.EMPTY,
				this.tenantCode, tenantId, null);
	}

	private UserV createUserVByMobile() throws Exception {
		return new UserV(StringConstants.EMPTY, null, null, StringConstants.EMPTY, AESUtils.encrypt(this.mobile),
				this.tenantCode, tenantId, null);
	}

	private UserV createUserVByTest() throws Exception {
		return new UserV(AESUtils.encrypt(this.username), this.password, null, StringConstants.EMPTY,
				StringConstants.EMPTY, this.tenantCode, tenantId, null);
	}

	private UserV createUserVByAuthorizationCode() throws Exception {
		return new UserV(AESUtils.encrypt(this.username), this.password, null, StringConstants.EMPTY,
				StringConstants.EMPTY, this.tenantCode, tenantId, null);
	}

	private UserE createUserE() {
		return DomainFactory.createUser()
			.toBuilder()
			.id(1L)
			.username(this.username)
			.password(this.password)
			.avatar(1L)
			.tenantId(tenantId)
			.mail(this.mail)
			.mobile(this.mobile)
			.deptId(1L)
			.superAdmin(SuperAdmin.YES.getCode())
			.status(UserStatus.ENABLE.getCode())
			.build();
	}

	private AuthA createAuth() {
		return DomainFactory.createAuth();
	}

	@SpringBootConfiguration
	@ComponentScan(basePackages = { "org.laokou" })
	static class TestConfig {

	}

	private record UsernamePasswordAuthParam(String uuid, String captcha, String username, String password,
			String tenantCode, String grantType) {
	}

	private record MobileAuthParam(String mobile, String code, String tenantCode, String grantType) {
	}

	private record MailAuthParam(String mail, String code, String tenantCode, String grantType) {
	}

	private record TestAuthParam(String username, String password, String tenantCode, String grantType) {
	}

	private record AuthorizationCodAuthParam(String username, String password, String tenantCode, String grantType) {
	}

}
