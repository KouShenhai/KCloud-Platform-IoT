/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.auth.dto.TokenRemoveCmd;
import org.laokou.auth.gateway.CaptchaGateway;
import org.laokou.common.core.annotation.EnableTaskExecutor;
import org.laokou.common.core.utils.*;
import org.laokou.common.crypto.utils.RSAUtil;
import org.laokou.common.domain.event.RemoveCacheEvent;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.rocketmq.template.SendMessageType;
import org.laokou.common.security.config.GlobalOpaqueTokenIntrospector;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.laokou.common.data.cache.constant.NameConstant.TENANTS;
import static org.laokou.common.i18n.common.constant.StringConstant.RISK;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@EnableTaskExecutor
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OAuth2ApiTest {

	private static final String USERNAME = "admin";

	private static final String PASSWORD = "admin123";

	private static final String ACCESS_TOKEN = "accessToken";

	private static final String REFRESH_TOKEN = "refreshToken";

	private static final String MAIL = "2413176044@qq.com";

	private static final String MOBILE = "xxx";

	private static final String DEVICE_CODE = "device_code";

	private static final String CODE = "iQWEh8YSpdwZ8_5QsA8C_1tVpR-6_fcLMYDKGnhozJW9MmTzf30aYvq6F_O3sSL0PP0bEVVdoXeau8QOuTln3ABn2c-00x7irutqFKAHRJVFZGln_6Wmuab4ostt-3-y";

	private static final String UUID = String.valueOf(IdGenerator.defaultSnowflakeId());

	private static final String TOKEN = "eyJraWQiOiI2MTIyYjcyOC0xOTMxLTQ3NWMtYjMyMS0yYjdmYmVjMGQ0OTEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Ijk1VHhTc1RQRkEzdEYxMlRCU01tVVZLMGRhIiwibmJmIjoxNzEwMzEzMTEyLCJzY29wZSI6WyJwYXNzd29yZCIsIm1haWwiLCJvcGVuaWQiLCJtb2JpbGUiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo1NTU1L2F1dGgiLCJleHAiOjE3MTAzMTY3MTIsImlhdCI6MTcxMDMxMzExMiwianRpIjoiZjRlYWU1YjctOWQzNy00NTM1LWEyODgtNWFjNWEwNzc2MjU1In0.Sg4LYn6hoYKB3vDM4NnFfDd3MBxpu-Bja-iYTNDDVBTkDMPjWXdbSTpupplud5aQ-mwRMhSuMF_ctzMFT5So1VckhNV8dg35DhKsRzEYfLaya_vk4eiFUaSU8ibfSPSEACa524L01SHb8wgb04LnvVAuJnPEzDZNRZxwHKbxA0irqwCafuTax8EFKGxHskHsxeuaaCvQdGLKSbYCdC3tHA85SIUKdsnm8fSS4_5El9gztbFUxDHZWRgagN_fHRqyDSd32PCulPeG3uOut-uUwC2Dv4xodLuaCYEouyn0aMY_juz2uHkpf1MnLh74caeE30lmbqBF5tv2ErOsqdMIaw";

	private final CaptchaGateway captchaGateway;

	private final RedisUtil redisUtil;

	private final WebApplicationContext webApplicationContext;

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	private final ServerProperties serverProperties;

	private final RestClient restClient;

	private final PasswordEncoder passwordEncoder;

	private final DomainEventPublisher rocketMQDomainEventPublisher;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testTtlMDC() {
		MDCUtil.put("111", "222");
		try (ExecutorService executor = ThreadUtil.newVirtualTaskExecutor()) {
			executor.execute(() -> log.info("TraceId：{}，SpanId：{}", MDCUtil.getSpanId(), MDCUtil.getSpanId()));
		}
	}

	@Test
	void testRestClientToSendJsonPostRequest() {
		String url = "https://jsonplaceholder.typicode.com/posts";
		String json = restClient.method(POST)
			.uri(url)
			.contentType(MediaType.APPLICATION_JSON)
			.retrieve()
			.toEntity(String.class)
			.toString();
		assertNotNull(json);
	}

	@Test
	void testSetInstantObj() {
		String key = "test:instant:obj";
		redisUtil.set(key, new InstantTest(DateUtil.nowInstant()));
		log.info("获取Instant obj值：{}", redisUtil.get(key));
		redisUtil.del(key);
	}

	@Test
	void testSetInstant() {
		String key = "test:instant";
		redisUtil.set(key, DateUtil.nowInstant());
		log.info("获取Instant值：{}", redisUtil.get(key));
		redisUtil.del(key);
	}

	@Test
	void testEncodePassword() {
		String pwd = passwordEncoder.encode(PASSWORD);
		log.info("生成密码：{}", pwd);
		Assertions.assertTrue(passwordEncoder.matches(PASSWORD, pwd));
	}

	@Test
	void testUsernamePasswordAuthApi() {
		log.info("---------- 用户名密码认证模式开始 ----------");
		String captcha = getCaptcha(UUID);
		String encryptUsername = RSAUtil.encryptByPublicKey(USERNAME);
		String encryptPassword = RSAUtil.encryptByPublicKey(PASSWORD);
		String decryptUsername = RSAUtil.decryptByPrivateKey(encryptUsername);
		String decryptPassword = RSAUtil.decryptByPrivateKey(encryptPassword);
		Map<String, String> tokenMap = usernamePasswordAuth(captcha, decryptUsername, decryptPassword);
		log.info("验证码：{}", captcha);
		log.info("加密用户名：{}", encryptUsername);
		log.info("加密密码：{}", encryptPassword);
		log.info("解密用户名：{}", decryptUsername);
		log.info("解密密码：{}", decryptPassword);
		log.info("uuid：{}", UUID);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		String token = getRefreshToken(tokenMap.get(REFRESH_TOKEN));
		log.info("刷新token：{}", token);
		log.info("---------- 模拟认证开始 ----------");
		assertNotNull(token);
		GlobalOpaqueTokenIntrospector introspector = new GlobalOpaqueTokenIntrospector(oAuth2AuthorizationService);
		log.info("认证数据：{}", JacksonUtil.toJsonStr(introspector.introspect(token)));
		log.info("---------- 模拟认证结束 ----------");
		log.info("---------- 用户名密码认证模式结束 ----------");
	}

	@Test
	void testMailAuthApi() {
		log.info("---------- 邮箱认证开始 ----------");
		String code = getCodeApi(MAIL);
		Map<String, String> tokenMap = mailAuth(code);
		log.info("验证码：{}", code);
		log.info("验证码：{}", MAIL);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshToken(tokenMap.get(REFRESH_TOKEN)));
		log.info("---------- 邮箱认证结束 ----------");
	}

	@Test
	void testMobileAuthApi() {
		log.info("---------- 手机号认证开始 ----------");
		String code = getCodeApi(MOBILE);
		Map<String, String> tokenMap = mobileAuth(code);
		log.info("验证码：{}", code);
		log.info("手机号：{}", MOBILE);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshToken(tokenMap.get(REFRESH_TOKEN)));
		log.info("---------- 手机号认证结束 ----------");
	}

	@Test
	void testAuthorizationCodeAuthApi() {
		log.info("---------- 授权码认证模式开始 ----------");
		Map<String, String> tokenMap = authorizationCodeAuth();
		log.info("编码：{}", CODE);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshToken(tokenMap.get(REFRESH_TOKEN)));
		log.info("---------- 授权码认证模式结束 ----------");
	}

	@Test
	void testClientCredentialsAuthApi() {
		log.info("---------- 客户端认证模式开始 ----------");
		Map<String, String> tokenMap = clientCredentialsAuth();
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("---------- 客户端认证模式结束 ----------");
	}

	@Test
	void testDeviceAuthorizationCodeAuthApi() {
		log.info("---------- 设备授权码认证模式开始 ----------");
		String deviceCode = getDeviceCode();
		// 需要用户确认
		Map<String, String> tokenMap = deviceAuthorizationCodeAuth(deviceCode);
		log.info("设备码：{}", deviceCode);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshToken(tokenMap.get(REFRESH_TOKEN)));
		log.info("---------- 设备授权码认证模式结束 ----------");
	}

	@Test
	void testLogoutApi() {
		log.info("---------- 登录已注销，开始清除令牌 ----------");
		String apiUrl = getTokenUrlV3();
		restClient.method(HttpMethod.DELETE)
			.uri(apiUrl)
			.body(new TokenRemoveCmd(TOKEN))
			.contentType(MediaType.APPLICATION_JSON)
			.retrieve()
			.toBodilessEntity();
		log.info("---------- 登录已注销，结束令牌清除 ----------");
	}

	@Test
	void testRemoveCache() {
		MDCUtil.put("0", "0");
		rocketMQDomainEventPublisher.publish(new RemoveCacheEvent(TENANTS, "1"), SendMessageType.ASYNC);
	}

	private Map<String, String> deviceAuthorizationCodeAuth(String deviceCode) {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("device_code", deviceCode, "grant_type",
					"urn:ietf:params:oauth:grant-type:device_code");
			Map<String, String> headers = Collections.singletonMap("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
			String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
			log.info("设备授权码认证模式，返回信息：{}", json);
			String accessToken = JacksonUtil.readTree(json).get("access_token").asText();
			String refreshToken = JacksonUtil.readTree(json).get("refresh_token").asText();
			Assert.isTrue(StringUtil.isNotEmpty(accessToken), "access token is empty");
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
			String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
			log.info("客户端认证模式，返回信息：{}", json);
			String accessToken = JacksonUtil.readTree(json).get("access_token").asText();
			Assert.isTrue(StringUtil.isNotEmpty(accessToken), "access token is empty");
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
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
			String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
			log.info("授权码认证模式，返回信息：{}", json);
			String accessToken = JacksonUtil.readTree(json).get("access_token").asText();
			String refreshToken = JacksonUtil.readTree(json).get("refresh_token").asText();
			Assert.isTrue(StringUtil.isNotEmpty(accessToken), "access token is empty");
			return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
		}
		catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	private String getCodeApi(String uuid) {
		return getCaptcha(uuid);
	}

	private Map<String, String> mobileAuth(String code) {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("code", code, "mobile", MOBILE, "tenant_code", "laokou", "grant_type",
					"mobile");
			Map<String, String> headers = Map.of("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=", "User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");
			String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
			log.info("手机号认证，返回信息：{}", json);
			String accessToken = JacksonUtil.readTree(json).get("access_token").asText();
			String refreshToken = JacksonUtil.readTree(json).get("refresh_token").asText();
			Assert.isTrue(StringUtil.isNotEmpty(accessToken), "access token is empty");
			return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
		}
		catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	private Map<String, String> mailAuth(String code) {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("code", code, "mail", MAIL, "tenant_code", "laokou", "grant_type",
					"mail");
			Map<String, String> headers = Map.of("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=", "User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");
			String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
			log.info("邮箱认证，返回信息：{}", json);
			String accessToken = JacksonUtil.readTree(json).get("access_token").asText();
			String refreshToken = JacksonUtil.readTree(json).get("refresh_token").asText();
			Assert.isTrue(StringUtil.isNotEmpty(accessToken), "access token is empty");
			return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
		}
		catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	@SneakyThrows
	private Map<String, String> usernamePasswordAuth(String captcha, String username, String password) {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("uuid", UUID, "username", username, "password", password, "tenant_code",
					"laokou", "grant_type", "password", "captcha", captcha);
			Map<String, String> headers = Map.of("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=", "trace-id",
					String.valueOf(IdGenerator.defaultSnowflakeId()), "User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");
			String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
			log.info("用户名密码认证模式，返回信息：{}", json);
			String accessToken = JacksonUtil.readTree(json).get("access_token").asText();
			String refreshToken = JacksonUtil.readTree(json).get("refresh_token").asText();
			Assert.isTrue(StringUtil.isNotEmpty(accessToken), "access token is empty");
			return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
		}
		catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	private String getRefreshToken(String refreshToken) {
		try {
			String apiUrl = getOAuthApiUrl();
			Map<String, String> params = Map.of("refresh_token", refreshToken, "grant_type", "refresh_token");
			Map<String, String> headers = Collections.singletonMap("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
			String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
			log.info("刷新令牌模式，返回信息；{}", json);
			return JacksonUtil.readTree(json).get("access_token").asText();
		}
		catch (Exception e) {
			return null;
		}
	}

	@SneakyThrows
	private String getCaptcha(String uuid) {
		mockMvc.perform(get(getCaptchaApiUrlV3(uuid)).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		String key = captchaGateway.getKey(uuid);
		String captcha = redisUtil.get(key).toString();
		Assert.isTrue(StringUtil.isNotEmpty(captcha), "captcha is empty");
		return captcha;
	}

	@SneakyThrows
	private String getPublicKey() {
		MvcResult mvcResult = mockMvc.perform(get(getSecretApiUrlV3()).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			// 打印到控制台
			.andDo(print())
			.andReturn();
		String publicKey = JacksonUtil.readTree(mvcResult.getResponse().getContentAsString())
			.get("data")
			.get("publicKey")
			.asText();
		Assert.isTrue(StringUtil.isNotEmpty(publicKey), "publicKey is empty");
		return publicKey;
	}

	private String getDeviceCode() {
		try {
			String apiUrl = getDeviceCodeApiUrl();
			Map<String, String> params = Collections.emptyMap();
			Map<String, String> headers = Collections.singletonMap("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
			String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
			return JacksonUtil.readTree(json).get(DEVICE_CODE).asText();
		}
		catch (Exception e) {
			return null;
		}
	}

	private String getOAuthApiUrl() {
		return getSchema(disabledSsl()) + "auth" + RISK + serverProperties.getPort() + "/oauth2/token";
	}

	private String getDeviceCodeApiUrl() {
		return getSchema(disabledSsl()) + "auth" + RISK + serverProperties.getPort() + "/oauth2/device_authorization";
	}

	private String getCaptchaApiUrlV3(String uuid) {
		return getSchema(disabledSsl()) + "auth" + RISK + serverProperties.getPort() + "/v3/captchas/" + uuid;
	}

	private String getSecretApiUrlV3() {
		return getSchema(disabledSsl()) + "auth" + RISK + serverProperties.getPort() + "/v3/secrets";
	}

	private String getTokenUrlV3() {
		return getSchema(disabledSsl()) + "auth" + RISK + serverProperties.getPort() + "/v3/tokens";
	}

	private String getSchema(boolean disabled) {
		return disabled ? "https://" : "http://";
	}

	private boolean disabledSsl() {
		return serverProperties.getSsl().isEnabled();
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class InstantTest implements Serializable {

		private Instant instant;

	}

}
