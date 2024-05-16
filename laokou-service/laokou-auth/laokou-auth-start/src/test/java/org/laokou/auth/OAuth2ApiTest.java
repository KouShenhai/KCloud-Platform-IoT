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

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.auth.gateway.CaptchaGateway;
import org.laokou.common.core.utils.HttpUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.crypto.utils.RsaUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Map;

import static org.laokou.common.i18n.common.constants.StringConstant.RISK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
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

	private final CaptchaGateway captchaGateway;

	private final RedisUtil redisUtil;

	private final WebApplicationContext webApplicationContext;

	private final ServerProperties serverProperties;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testUsernamePasswordAuthApi() {
		log.info("---------- 用户名密码认证模式开始 ----------");
		String captcha = getCaptcha(UUID);
		String publicKey = getPublicKey();
		String privateKey = RsaUtil.getPrivateKey();
		String encryptUsername = RsaUtil.encryptByPublicKey(USERNAME, publicKey);
		String encryptPassword = RsaUtil.encryptByPublicKey(PASSWORD, publicKey);
		String decryptUsername = RsaUtil.decryptByPrivateKey(encryptUsername, privateKey);
		String decryptPassword = RsaUtil.decryptByPrivateKey(encryptPassword, privateKey);
		Map<String, String> tokenMap = usernamePasswordAuth(captcha, decryptUsername, decryptPassword);
		log.info("验证码：{}", captcha);
		log.info("加密用户名：{}", encryptUsername);
		log.info("加密密码：{}", encryptPassword);
		log.info("解密用户名：{}", decryptUsername);
		log.info("解密密码：{}", decryptPassword);
		log.info("uuid：{}", UUID);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshToken(tokenMap.get(REFRESH_TOKEN)));
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
	void testTenantOptionsApi() {
		String apiUrl = getTenantOptionsApiUrlV3();
		String json = HttpUtil.doGet(apiUrl, Collections.emptyMap(), Collections.emptyMap(), disabledSsl());
		log.info("查询租户下拉选择项列表，返回信息：{}", json);
	}

	@Test
	void testTenantIdByDomainNameApi() {
		String apiUrl = getTenantIdByDomainNameApiUrlV3();
		String json = HttpUtil.doGet(apiUrl, Collections.emptyMap(), Collections.emptyMap(), disabledSsl());
		log.info("根据域名查看租户ID，返回信息：{}", json);
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
			Map<String, String> params = Map.of("code", code, "mobile", MOBILE, "tenant_id", "0", "grant_type",
					"mobile");
			Map<String, String> headers = Collections.singletonMap("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
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
			Map<String, String> params = Map.of("code", code, "mail", MAIL, "tenant_id", "0", "grant_type", "mail");
			Map<String, String> headers = Collections.singletonMap("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
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
			Map<String, String> params = Map.of("uuid", UUID, "username", username, "password", password, "tenant_id",
					"0", "grant_type", "password", "captcha", captcha);
			Map<String, String> headers = Map.of("Authorization",
					"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=", "trace-id",
					String.valueOf(System.currentTimeMillis()));
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
		return getSchema(disabledSsl()) + "127.0.0.1" + RISK + serverProperties.getPort() + "/oauth2/token";
	}

	private String getDeviceCodeApiUrl() {
		return getSchema(disabledSsl()) + "127.0.0.1" + RISK + serverProperties.getPort()
				+ "/oauth2/device_authorization";
	}

	private String getCaptchaApiUrlV3(String uuid) {
		return getSchema(disabledSsl()) + "127.0.0.1" + RISK + serverProperties.getPort() + "/v3/captchas/" + uuid;
	}

	private String getSecretApiUrlV3() {
		return getSchema(disabledSsl()) + "127.0.0.1" + RISK + serverProperties.getPort() + "/v3/secrets";
	}

	private String getTenantOptionsApiUrlV3() {
		return getSchema(disabledSsl()) + "127.0.0.1" + RISK + serverProperties.getPort() + "/v3/tenants/options";
	}

	private String getTenantIdByDomainNameApiUrlV3() {
		return getSchema(disabledSsl()) + "127.0.0.1" + RISK + serverProperties.getPort() + "/v3/tenants/id";
	}

	private String getSchema(boolean disabled) {
		return disabled ? "https://" : "http://";
	}

	private boolean disabledSsl() {
		return serverProperties.getSsl().isEnabled();
	}

}
