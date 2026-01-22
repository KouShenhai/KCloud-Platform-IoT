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
import org.laokou.auth.model.entity.UserE;
import org.laokou.auth.model.enums.GrantType;
import org.laokou.auth.model.enums.SuperAdmin;
import org.laokou.auth.model.enums.UserStatus;
import org.laokou.auth.model.function.HttpRequest;
import org.laokou.auth.model.validator.AuthParamValidator;
import org.laokou.auth.model.validator.CaptchaParamValidator;
import org.laokou.auth.model.validator.CaptchaValidator;
import org.laokou.auth.model.validator.PasswordValidator;
import org.laokou.auth.model.valueobject.CaptchaV;
import org.laokou.auth.model.valueobject.UserV;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.crypto.util.RSAUtils;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 认证聚合根测试.
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

	@Test
	@DisplayName("Test username password auth success")
	void test_auth_usernamePassword_success() throws Exception {
		createUsernamePasswordAuthInfo(createUsernamePasswordAuthParam());
		AuthA usernamePasswordAuth = createAuth().createUsernamePasswordAuth();
		Assertions.assertThat(usernamePasswordAuth).isNotNull();
		Assertions.assertThatCode(() -> domainService.auth(usernamePasswordAuth)).doesNotThrowAnyException();
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId("laokou");
		Mockito.verify(httpRequest, Mockito.times(1)).getParameterMap();
		Mockito.verify(captchaValidator, Mockito.times(1))
			.validateCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey("070d5f1650ea4832951ded2cce5b4386"),
					"1234");
		Mockito.verify(passwordValidator, Mockito.times(1)).validatePassword("admin123", "admin123");
		Mockito.verify(userGateway, Mockito.times(1)).getUserProfile(getUserV());
		Mockito.verify(menuGateway, Mockito.times(1)).getMenuPermissions(getUserE());
		Mockito.verify(ossLogGateway, Mockito.times(1)).getOssUrl(1L);
	}

	@Test
	@DisplayName("Test username password validate empty value")
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
	void test_auth_mail_auth_success() {

	}

	@Test
	@DisplayName("Test mail validate empty value")
	void test_auth_mail_validateEmptyValue() {

	}

	private void createMailAuthInfo() {

	}

	private void createMobileAuthInfo() {

	}

	private void createTestAuthInfo() {

	}

	private void createAuthorizationCodeAuthInfo() {

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
		Mockito.when(userGateway.getUserProfile(Mockito.any())).thenReturn(getUserE());
		Mockito.when(menuGateway.getMenuPermissions(Mockito.any())).thenReturn(Set.of("sys:user:save"));
		Mockito.when(ossLogGateway.getOssUrl(Mockito.anyLong())).thenReturn("https://1.png");
		Map<String, String[]> params = new HashMap<>(6);
		params.put("username", new String[] { usernamePasswordAuthParam.username() });
		params.put("password", new String[] { usernamePasswordAuthParam.password() });
		params.put("tenant_code", new String[] { usernamePasswordAuthParam.tenantCode() });
		params.put("uuid", new String[] { usernamePasswordAuthParam.uuid() });
		params.put("captcha", new String[] { usernamePasswordAuthParam.captcha() });
		params.put("grant_type", new String[] { usernamePasswordAuthParam.grantType() });
		Mockito.when(httpRequest.getParameterMap()).thenReturn(params);
	}

	private UsernamePasswordAuthParam createUsernamePasswordAuthParam() {
		String username = RSAUtils.encryptByPublicKey("admin");
		String password = RSAUtils.encryptByPublicKey("admin123");
		String tenantCode = "laokou";
		String uuid = "070d5f1650ea4832951ded2cce5b4386";
		String captcha = "1234";
		String grantType = GrantType.USERNAME_PASSWORD.getCode();
		return new UsernamePasswordAuthParam(uuid, captcha, username, password, tenantCode, grantType);
	}

	private UsernamePasswordAuthParam createUsernamePasswordEmptyAuthParam() {
		return new UsernamePasswordAuthParam(null, null, null, null, null, GrantType.USERNAME_PASSWORD.getCode());
	}

	private UserV getUserV() throws Exception {
		return new UserV(AESUtils.encrypt("admin"), "admin123", null, "", "", "laokou", 1L, null);
	}

	private UserE getUserE() {
		return DomainFactory.createUser()
			.toBuilder()
			.id(1L)
			.username("admin")
			.password("admin123")
			.avatar(1L)
			.tenantId(1L)
			.mail("2413176044@qq.com")
			.mobile("18888888888")
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
