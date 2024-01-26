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

package org.laokou.auth;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.auth.domain.gateway.CaptchaGateway;
import org.laokou.common.core.utils.HttpUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.crypto.utils.RsaUtil;
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

import java.util.HashMap;
import java.util.Map;

import static org.laokou.common.i18n.common.NetworkConstants.LOCAL_IPV4;
import static org.laokou.common.i18n.common.StringConstants.RISK;
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

	private static final Long SNOWFLAKE_ID = IdGenerator.defaultSnowflakeId();

	private final CaptchaGateway captchaGateway;

	private final RedisUtil redisUtil;

	private final WebApplicationContext webApplicationContext;

	private final ServerProperties serverProperties;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testUsernamePasswordAuthApi() {
		String captcha = getCaptchasApi(SNOWFLAKE_ID.toString());
		String publicKey = getSecretsApi();
		String privateKey = RsaUtil.getPrivateKey();
		String encryptUsername = RsaUtil.encryptByPublicKey(USERNAME, publicKey);
		String encryptPassword = RsaUtil.encryptByPublicKey(PASSWORD, publicKey);
		String decryptUsername = RsaUtil.decryptByPrivateKey(encryptUsername, privateKey);
		String decryptPassword = RsaUtil.decryptByPrivateKey(encryptPassword, privateKey);
		Map<String, String> tokenMap = getUsernamePasswordAuthApi(SNOWFLAKE_ID, captcha, decryptUsername,
				decryptPassword);
		log.info("验证码：{}", captcha);
		log.info("加密用户名：{}", encryptUsername);
		log.info("加密密码：{}", encryptPassword);
		log.info("解密用户名：{}", decryptUsername);
		log.info("解密密码：{}", decryptPassword);
		log.info("uuid：{}", SNOWFLAKE_ID);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshTokenApi(tokenMap.get(REFRESH_TOKEN)));
	}

	@Test
	void testMailAuthApi() {
		String code = getCodeApi(MAIL);
		Map<String, String> tokenMap = getMailAuthApi(code);
		log.info("验证码：{}", code);
		log.info("验证码：{}", MAIL);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshTokenApi(tokenMap.get(REFRESH_TOKEN)));
	}

	@Test
	void testMobileAuthApi() {
		String code = getCodeApi(MOBILE);
		Map<String, String> tokenMap = getMobileAuthApi(code);
		log.info("验证码：{}", code);
		log.info("手机号：{}", MOBILE);
		log.info("token：{}", tokenMap.get(ACCESS_TOKEN));
		log.info("刷新token：{}", getRefreshTokenApi(tokenMap.get(REFRESH_TOKEN)));
	}

	private String getCodeApi(String uuid) {
		return getCaptchasApi(uuid);
	}

	private Map<String, String> getMobileAuthApi(String code) {
		String apiUrl = getOAuthApiUrl();
		HashMap<String, String> params = new HashMap<>();
		HashMap<String, String> headers = new HashMap<>(1);
		params.put("code", code);
		params.put("mobile", MOBILE);
		params.put("tenant_id", "0");
		params.put("grant_type", "mobile");
		headers.put("Authorization", "Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
		String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
		String accessToken = JacksonUtil.readTree(json).get("access_token").asText();
		String refreshToken = JacksonUtil.readTree(json).get("refresh_token").asText();
		Assert.isTrue(StringUtil.isNotEmpty(accessToken), "access token is empty");
		return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
	}

	private Map<String, String> getMailAuthApi(String code) {
		String apiUrl = getOAuthApiUrl();
		HashMap<String, String> params = new HashMap<>();
		HashMap<String, String> headers = new HashMap<>(1);
		params.put("code", code);
		params.put("mail", MAIL);
		params.put("tenant_id", "0");
		params.put("grant_type", "mail");
		headers.put("Authorization", "Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
		String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
		String accessToken = JacksonUtil.readTree(json).get("access_token").asText();
		String refreshToken = JacksonUtil.readTree(json).get("refresh_token").asText();
		Assert.isTrue(StringUtil.isNotEmpty(accessToken), "access token is empty");
		return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
	}

	@SneakyThrows
	private Map<String, String> getUsernamePasswordAuthApi(long uuid, String captcha, String username,
			String password) {
		String apiUrl = getOAuthApiUrl();
		HashMap<String, String> params = new HashMap<>();
		HashMap<String, String> headers = new HashMap<>(1);
		params.put("uuid", String.valueOf(uuid));
		params.put("username", username);
		params.put("password", password);
		params.put("tenant_id", "0");
		params.put("grant_type", "password");
		params.put("captcha", captcha);
		headers.put("Authorization", "Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
		String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
		String accessToken = JacksonUtil.readTree(json).get("access_token").asText();
		String refreshToken = JacksonUtil.readTree(json).get("refresh_token").asText();
		Assert.isTrue(StringUtil.isNotEmpty(accessToken), "access token is empty");
		return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
	}

	private String getRefreshTokenApi(String refreshToken) {
		String apiUrl = getOAuthApiUrl();
		HashMap<String, String> params = new HashMap<>();
		HashMap<String, String> headers = new HashMap<>(1);
		params.put("refresh_token", refreshToken);
		params.put("grant_type", "refresh_token");
		headers.put("Authorization", "Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
		String json = HttpUtil.doFormDataPost(apiUrl, params, headers, disabledSsl());
		return JacksonUtil.readTree(json).get("access_token").asText();
	}

	@SneakyThrows
	private String getCaptchasApi(String uuid) {
		String apiUrl = "/v1/captchas/";
		mockMvc.perform(get(apiUrl + uuid).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		String key = captchaGateway.key(uuid);
		String captcha = redisUtil.get(key).toString();
		Assert.isTrue(StringUtil.isNotEmpty(captcha), "captcha is empty");
		return captcha;
	}

	@SneakyThrows
	private String getSecretsApi() {
		String apiUrl = "/v1/secrets";
		MvcResult mvcResult = mockMvc.perform(get(apiUrl).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			// 打印到控制台
			.andDo(print())
			.andReturn();
		String secret = JacksonUtil.readTree(mvcResult.getResponse().getContentAsString()).get("data").asText();
		Assert.isTrue(StringUtil.isNotEmpty(secret), "secret is empty");
		return secret;
	}

	private String getOAuthApiUrl() {
		return getSchema(disabledSsl()) + LOCAL_IPV4 + RISK + serverProperties.getPort() + "/oauth2/token";
	}

	private String getSchema(boolean disabled) {
		return disabled ? "https://" : "http://";
	}

	private boolean disabledSsl() {
		return serverProperties.getSsl().isEnabled();
	}

}
