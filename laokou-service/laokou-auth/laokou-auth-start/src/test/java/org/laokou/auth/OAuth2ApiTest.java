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

package org.laokou.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.auth.dto.CaptchaSendCmd;
import org.laokou.auth.dto.TokenRemoveCmd;
import org.laokou.auth.dto.clientobject.CaptchaCO;
import org.laokou.common.core.util.HttpUtils;
import org.laokou.common.core.util.OkHttpUtils;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.core.util.UUIDGenerator;
import org.laokou.common.crypto.util.RSAUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.DateUtils;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.idempotent.aop.IdempotentAop;
import org.laokou.common.redis.util.RedisUtils;
import org.laokou.common.security.config.OAuth2OpaqueTokenIntrospector;
import org.laokou.common.trace.util.MDCUtils;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.test.context.TestConstructor;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@DisplayName("OAuth2测试")
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OAuth2ApiTest {

	private static final String USERNAME = "admin";

	private static final String PASSWORD = "admin123";

	private static final String ACCESS_TOKEN = "accessToken";

	private static final String REFRESH_TOKEN = "refreshToken";

	private static final String MAIL = "2413176044@qq.com";

	private static final String MOBILE = "18888888888";

	private static final String TENANT_CODE = "laokou";

	private static final String DEVICE_CODE = "device_code";

	private static final String CODE = "iQWEh8YSpdwZ8_5QsA8C_1tVpR-6_fcLMYDKGnhozJW9MmTzf30aYvq6F_O3sSL0PP0bEVVdoXeau8QOuTln3ABn2c-00x7irutqFKAHRJVFZGln_6Wmuab4ostt-3-y";

	private static final String UUID = UUIDGenerator.generateUUID();

	private static final String TOKEN = "eyJraWQiOiI2MTIyYjcyOC0xOTMxLTQ3NWMtYjMyMS0yYjdmYmVjMGQ0OTEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Ijk1VHhTc1RQRkEzdEYxMlRCU01tVVZLMGRhIiwibmJmIjoxNzEwMzEzMTEyLCJzY29wZSI6WyJwYXNzd29yZCIsIm1haWwiLCJvcGVuaWQiLCJtb2JpbGUiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo1NTU1L2F1dGgiLCJleHAiOjE3MTAzMTY3MTIsImlhdCI6MTcxMDMxMzExMiwianRpIjoiZjRlYWU1YjctOWQzNy00NTM1LWEyODgtNWFjNWEwNzc2MjU1In0.Sg4LYn6hoYKB3vDM4NnFfDd3MBxpu-Bja-iYTNDDVBTkDMPjWXdbSTpupplud5aQ-mwRMhSuMF_ctzMFT5So1VckhNV8dg35DhKsRzEYfLaya_vk4eiFUaSU8ibfSPSEACa524L01SHb8wgb04LnvVAuJnPEzDZNRZxwHKbxA0irqwCafuTax8EFKGxHskHsxeuaaCvQdGLKSbYCdC3tHA85SIUKdsnm8fSS4_5El9gztbFUxDHZWRgagN_fHRqyDSd32PCulPeG3uOut-uUwC2Dv4xodLuaCYEouyn0aMY_juz2uHkpf1MnLh74caeE30lmbqBF5tv2ErOsqdMIaw";

	private final RedisUtils redisUtils;

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	private final ServerProperties serverProperties;

	private final RestClient restClient;

	private final PasswordEncoder passwordEncoder;

	@Test
	void test_sendMailCaptcha() {
		CaptchaCO co = new CaptchaCO();
		co.setTenantCode(TENANT_CODE);
		co.setUuid(MAIL);
		CaptchaSendCmd cmd = new CaptchaSendCmd();
		cmd.setCo(co);
		restClient.post()
			.uri(getSendMailCaptchaUrl())
			.header(IdempotentAop.REQUEST_ID, UUIDGenerator.generateUUID())
			.body(cmd)
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.toBodilessEntity();
	}

	@Test
	void test_sendMobileCaptcha() {
		CaptchaCO co = new CaptchaCO();
		co.setTenantCode(TENANT_CODE);
		co.setUuid(MOBILE);
		CaptchaSendCmd cmd = new CaptchaSendCmd();
		cmd.setCo(co);
		restClient.post()
			.uri(getSendMobileCaptchaUrl())
			.header(IdempotentAop.REQUEST_ID, UUIDGenerator.generateUUID())
			.body(cmd)
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.toBodilessEntity();
	}

	@Test
	void test_ttlMDC() {
		MDCUtils.put("111", "222");
		try (ExecutorService virtualTaskExecutor = ThreadUtils.newTtlVirtualTaskExecutor()) {
			virtualTaskExecutor
				.execute(() -> log.info("TraceId：{}，SpanId：{}", MDCUtils.getSpanId(), MDCUtils.getSpanId()));
		}
	}

	@Test
	void test_setInstant() {
		String key = "test:instant";
		redisUtils.set(key, DateUtils.nowInstant());
		log.info("获取Instant值：{}", redisUtils.get(key));
		redisUtils.del(key);
	}

	@Test
	void test_encodePassword() {
		String pwd = passwordEncoder.encode(PASSWORD);
		log.info("生成密码：{}", pwd);
		Assertions.assertThat(passwordEncoder.matches(PASSWORD, pwd)).isTrue();
	}

	@Test
	void test_usernamePasswordAuthApi() {
		log.info("---------- 用户名密码认证模式开始 ----------");
		String captcha = getCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey(UUID));
		String encryptUsername = RSAUtils.encryptByPublicKey(USERNAME);
		String encryptPassword = RSAUtils.encryptByPublicKey(PASSWORD);
		String decryptUsername = RSAUtils.decryptByPrivateKey(encryptUsername);
		String decryptPassword = RSAUtils.decryptByPrivateKey(encryptPassword);
		Assertions.assertThat(decryptUsername).isEqualTo(USERNAME);
		Assertions.assertThat(decryptPassword).isEqualTo(PASSWORD);
		Map<String, String> tokenMap = usernamePasswordAuth(captcha, encryptUsername, encryptPassword);
		log.info("验证码：{}", captcha);
		log.info("加密用户名：{}", encryptUsername);
		log.info("加密密码：{}", encryptPassword);
		log.info("解密用户名：{}", decryptUsername);
		log.info("解密密码：{}", decryptPassword);
		log.info("uuid：{}", UUID);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		String token = getRefreshToken(tokenMap.get(REFRESH_TOKEN),
				"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
		log.info("刷新token：{}", token);
		log.info("---------- 模拟认证开始 ----------");
		Assertions.assertThat(token).isNotBlank();
		OAuth2OpaqueTokenIntrospector introspector = new OAuth2OpaqueTokenIntrospector(oAuth2AuthorizationService);
		log.info("认证数据：{}", JacksonUtils.toJsonStr(introspector.introspect(token)));
		log.info("---------- 模拟认证结束 ----------");
		log.info("---------- 用户名密码认证模式结束 ----------");
	}

	@Test
	void test_mailAuthApi() {
		log.info("---------- 邮箱认证开始 ----------");
		String code = getCode(RedisKeyUtils.getMailAuthCaptchaKey(MAIL));
		Map<String, String> tokenMap = mailAuth(code);
		log.info("验证码：{}", code);
		log.info("验证码：{}", MAIL);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshToken(tokenMap.get(REFRESH_TOKEN),
				"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8="));
		log.info("---------- 邮箱认证结束 ----------");
	}

	@Test
	void test_mobileAuthApi() {
		log.info("---------- 手机号认证开始 ----------");
		String code = getCode(RedisKeyUtils.getMobileAuthCaptchaKey(MOBILE));
		Map<String, String> tokenMap = mobileAuth(code);
		log.info("验证码：{}", code);
		log.info("手机号：{}", MOBILE);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshToken(tokenMap.get(REFRESH_TOKEN),
				"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8="));
		log.info("---------- 手机号认证结束 ----------");
	}

	@Test
	void test_authorizationCodeAuthApi() {
		log.info("---------- 授权码认证模式开始 ----------");
		Map<String, String> tokenMap = authorizationCodeAuth();
		log.info("编码：{}", CODE);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshToken(tokenMap.get(REFRESH_TOKEN),
				"Basic ZWI3RGVkNWJiRmJkNzg5NmY4YTJjZmREYzk6RHBBa1BmejRlVzE4ZDI="));
		log.info("---------- 授权码认证模式结束 ----------");
	}

	@Test
	void test_testAuthApi() {
		log.info("---------- 测试认证模式开始 ----------");
		String encryptUsername = RSAUtils.encryptByPublicKey(USERNAME);
		String encryptPassword = RSAUtils.encryptByPublicKey(PASSWORD);
		String decryptUsername = RSAUtils.decryptByPrivateKey(encryptUsername);
		String decryptPassword = RSAUtils.decryptByPrivateKey(encryptPassword);
		Map<String, String> tokenMap = testAuth(decryptUsername, decryptPassword);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		String token = getRefreshToken(tokenMap.get(REFRESH_TOKEN),
				"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
		log.info("刷新token：{}", token);
		log.info("---------- 测试认证模式结束 ----------");
	}

	@Test
	void test_clientCredentialsAuthApi() {
		log.info("---------- 客户端认证模式开始 ----------");
		Map<String, String> tokenMap = clientCredentialsAuth();
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("---------- 客户端认证模式结束 ----------");
	}

	@Test
	void test_deviceAuthorizationCodeAuthApi() {
		log.info("---------- 设备授权码认证模式开始 ----------");
		String deviceCode = getDeviceCode();
		// 需要用户确认
		Map<String, String> tokenMap = deviceAuthorizationCodeAuth(deviceCode);
		log.info("设备码：{}", deviceCode);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshToken(tokenMap.get(REFRESH_TOKEN),
				"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8="));
		log.info("---------- 设备授权码认证模式结束 ----------");
	}

	@Test
	void test_logoutApi() {
		log.info("---------- 登录已注销，开始清除令牌 ----------");
		String apiUrl = getTokenUrlV3();
		TokenRemoveCmd cmd = new TokenRemoveCmd();
		cmd.setToken(TOKEN);
		restClient.method(HttpMethod.DELETE)
			.uri(apiUrl)
			.body(cmd)
			.contentType(MediaType.APPLICATION_JSON)
			.retrieve()
			.toBodilessEntity();
		log.info("---------- 登录已注销，结束令牌清除 ----------");
	}

	private Map<String, String> deviceAuthorizationCodeAuth(String deviceCode) {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("device_code", deviceCode, "grant_type",
					"urn:ietf:params:oauth:grant-type:device_code");
			Map<String, String> headers = Collections.singletonMap("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
			String json = HttpUtils.doFormDataPost(apiUrl, params, headers);
			log.info("设备授权码认证模式，返回信息：{}", json);
			Assertions.assertThat(json).isNotBlank();
			String accessToken = JacksonUtils.readTree(json).get("access_token").asText();
			String refreshToken = JacksonUtils.readTree(json).get("refresh_token").asText();
			Assert.isTrue(StringUtils.isNotEmpty(accessToken), "access token is empty");
			return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
		}
		catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	private Map<String, String> clientCredentialsAuth() {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("grant_type", "client_credentials");
			Map<String, String> headers = Collections.singletonMap("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
			String json = HttpUtils.doFormDataPost(apiUrl, params, headers);
			log.info("客户端认证模式，返回信息：{}", json);
			Assertions.assertThat(json).isNotBlank();
			String accessToken = JacksonUtils.readTree(json).get("access_token").asText();
			Assert.isTrue(StringUtils.isNotEmpty(accessToken), "access token is empty");
			return Map.of(ACCESS_TOKEN, accessToken);
		}
		catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	private Map<String, String> authorizationCodeAuth() {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("code", CODE, "redirect_uri", "http://127.0.0.1:8001", "grant_type",
					"authorization_code");
			Map<String, String> headers = Collections.singletonMap("Authorization",
					"Basic ZWI3RGVkNWJiRmJkNzg5NmY4YTJjZmREYzk6RHBBa1BmejRlVzE4ZDI=");
			String json = HttpUtils.doFormDataPost(apiUrl, params, headers);
			log.info("授权码认证模式，返回信息：{}", json);
			Assertions.assertThat(json).isNotBlank();
			String accessToken = JacksonUtils.readTree(json).get("access_token").asText();
			String refreshToken = JacksonUtils.readTree(json).get("refresh_token").asText();
			Assert.isTrue(StringUtils.isNotEmpty(accessToken), "access token is empty");
			return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
		}
		catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	private String getCode(String key) {
		String value = "123456";
		redisUtils.set(key, value, RedisUtils.FIVE_MINUTE_EXPIRE);
		return value;
	}

	private Map<String, String> mobileAuth(String code) {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("code", code, "mobile", MOBILE, "tenant_code", TENANT_CODE,
					"grant_type", "mobile");
			Map<String, String> headers = Map.of("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=", "User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");
			String json = HttpUtils.doFormDataPost(apiUrl, params, headers);
			log.info("手机号认证，返回信息：{}", json);
			Assertions.assertThat(json).isNotBlank();
			String accessToken = JacksonUtils.readTree(json).get("access_token").asText();
			String refreshToken = JacksonUtils.readTree(json).get("refresh_token").asText();
			Assert.isTrue(StringUtils.isNotEmpty(accessToken), "access token is empty");
			return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
		}
		catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	private Map<String, String> mailAuth(String code) {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("code", code, "mail", MAIL, "tenant_code", TENANT_CODE, "grant_type",
					"mail");
			Map<String, String> headers = Map.of("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=", "User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");
			String json = HttpUtils.doFormDataPost(apiUrl, params, headers);
			log.info("邮箱认证，返回信息：{}", json);
			Assertions.assertThat(json).isNotBlank();
			String accessToken = JacksonUtils.readTree(json).get("access_token").asText();
			String refreshToken = JacksonUtils.readTree(json).get("refresh_token").asText();
			Assert.isTrue(StringUtils.isNotEmpty(accessToken), "access token is empty");
			return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
		}
		catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	private Map<String, String> usernamePasswordAuth(String captcha, String username, String password) {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("uuid", UUID, "username", username, "password", password, "tenant_code",
					TENANT_CODE, "grant_type", "username_password", "captcha", captcha);
			Map<String, String> headers = Map.of("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=", "User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");
			String json = OkHttpUtils.doFormDataPost(apiUrl, params, headers);
			log.info("用户名密码认证模式，返回信息：{}", json);
			Assertions.assertThat(json).isNotBlank();
			String accessToken = JacksonUtils.readTree(json).get("access_token").asText();
			String refreshToken = JacksonUtils.readTree(json).get("refresh_token").asText();
			Assert.isTrue(StringUtils.isNotEmpty(accessToken), "access token is empty");
			return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
		}
		catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	private Map<String, String> testAuth(String username, String password) {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("username", username, "password", password, "tenant_code", TENANT_CODE,
					"grant_type", "test");
			Map<String, String> headers = Map.of("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=", "User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");
			String json = OkHttpUtils.doFormDataPost(apiUrl, params, headers);
			log.info("测试认证模式，返回信息：{}", json);
			Assertions.assertThat(json).isNotBlank();
			String accessToken = JacksonUtils.readTree(json).get("access_token").asText();
			String refreshToken = JacksonUtils.readTree(json).get("refresh_token").asText();
			Assert.isTrue(StringUtils.isNotEmpty(accessToken), "access token is empty");
			return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
		}
		catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	private String getRefreshToken(String refreshToken, String secretHeadValue) {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("refresh_token", refreshToken, "grant_type", "refresh_token");
			Map<String, String> headers = Collections.singletonMap("Authorization", secretHeadValue);
			String json = HttpUtils.doFormDataPost(apiUrl, params, headers);
			log.info("刷新令牌模式，返回信息；{}", json);
			Assertions.assertThat(json).isNotBlank();
			return JacksonUtils.readTree(json).get("access_token").asText();
		}
		catch (Exception e) {
			return null;
		}
	}

	private String getCaptcha(String key) {
		restClient.get().uri(URI.create(getCaptchaApiUrlV3())).retrieve().toBodilessEntity();
		String captcha = redisUtils.get(key).toString();
		Assert.isTrue(StringUtils.isNotEmpty(captcha), "captcha is empty");
		return captcha;
	}

	private String getDeviceCode() {
		try {
			String json = restClient.method(HttpMethod.POST)
				.uri(getDeviceCodeApiUrl())
				.headers(getHeaders())
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.retrieve()
				.body(String.class);
			return JacksonUtils.readTree(json).get(DEVICE_CODE).asText();
		}
		catch (Exception e) {
			return null;
		}
	}

	private Consumer<HttpHeaders> getHeaders() {
		return headers -> headers.add(HttpHeaders.AUTHORIZATION,
				"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
	}

	private String getOAuthApiUrl() {
		return getSchema(disabledSsl()) + "auth" + StringConstants.RISK + serverProperties.getPort() + "/oauth2/token";
	}

	private String getDeviceCodeApiUrl() {
		return getSchema(disabledSsl()) + "auth" + StringConstants.RISK + serverProperties.getPort()
				+ "/oauth2/device_authorization";
	}

	private String getCaptchaApiUrlV3() {
		return getSchema(disabledSsl()) + "auth" + StringConstants.RISK + serverProperties.getPort() + "/v3/captchas/"
				+ OAuth2ApiTest.UUID;
	}

	private String getTokenUrlV3() {
		return getSchema(disabledSsl()) + "auth" + StringConstants.RISK + serverProperties.getPort() + "/v3/tokens";
	}

	public String getSendMailCaptchaUrl() {
		return getSchema(disabledSsl()) + "auth" + StringConstants.RISK + serverProperties.getPort()
				+ "/v3/captchas/send/mail";
	}

	public String getSendMobileCaptchaUrl() {
		return getSchema(disabledSsl()) + "auth" + StringConstants.RISK + serverProperties.getPort()
				+ "/v3/captchas/send/mobile";
	}

	private String getSchema(boolean disabled) {
		return disabled ? "https://" : "http://";
	}

	private boolean disabledSsl() {
		return serverProperties.getSsl().isEnabled();
	}

}
