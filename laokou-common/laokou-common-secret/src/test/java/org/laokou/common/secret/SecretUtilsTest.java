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

package org.laokou.common.secret;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.secret.util.ApiSecretParamValidator;
import org.laokou.common.secret.util.SecretUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * SecretUtils and ApiSecretParamValidator test class.
 *
 * @author laokou
 */
class SecretUtilsTest {

	// ==================== SecretUtils Tests ====================

	@Test
	@DisplayName("Test sign generates consistent signature for same input")
	void testSignConsistentSignature() {
		String appKey = "testAppKey";
		String appSecret = "testAppSecret";
		String nonce = "randomNonce123";
		String timestamp = "1705400000000";
		String params = "key1=value1&key2=value2";

		String sign1 = SecretUtils.sign(appKey, appSecret, nonce, timestamp, params);
		String sign2 = SecretUtils.sign(appKey, appSecret, nonce, timestamp, params);

		Assertions.assertThat(sign1).isNotEmpty();
		Assertions.assertThat(sign1).isEqualTo(sign2);
	}

	@Test
	@DisplayName("Test sign generates different signature for different inputs")
	void testSignDifferentSignatureForDifferentInputs() {
		String appKey = "testAppKey";
		String appSecret = "testAppSecret";
		String nonce = "randomNonce123";
		String timestamp = "1705400000000";

		String sign1 = SecretUtils.sign(appKey, appSecret, nonce, timestamp, "params1");
		String sign2 = SecretUtils.sign(appKey, appSecret, nonce, timestamp, "params2");

		Assertions.assertThat(sign1).isNotEqualTo(sign2);
	}

	@Test
	@DisplayName("Test sign with empty params")
	void testSignWithEmptyParams() {
		String appKey = SecretUtils.APP_KEY;
		String appSecret = SecretUtils.APP_SECRET;
		String nonce = UUID.randomUUID().toString();
		String timestamp = String.valueOf(System.currentTimeMillis());

		String sign = SecretUtils.sign(appKey, appSecret, nonce, timestamp, "");
		Assertions.assertThat(sign).isNotEmpty();
	}

	@Test
	@DisplayName("Test sign with special characters in params")
	void testSignWithSpecialCharacters() {
		String appKey = SecretUtils.APP_KEY;
		String appSecret = SecretUtils.APP_SECRET;
		String nonce = "test-nonce";
		String timestamp = "1705400000000";
		String params = "name=张三&email=test@example.com";

		String sign = SecretUtils.sign(appKey, appSecret, nonce, timestamp, params);
		Assertions.assertThat(sign).isNotEmpty();
	}

	@Test
	@DisplayName("Test default APP_KEY and APP_SECRET are set")
	void testDefaultAppKeyAndSecret() {
		Assertions.assertThat(SecretUtils.APP_KEY).isNotEmpty();
		Assertions.assertThat(SecretUtils.APP_SECRET).isNotEmpty();
		Assertions.assertThat(SecretUtils.APP_KEY).isEqualTo("laokou2025");
		Assertions.assertThat(SecretUtils.APP_SECRET).isEqualTo("vb05f6c45d67340zaz95v7fa6d49v99zx");
	}

	// ==================== ApiSecretParamValidator Tests ====================

	@Test
	@DisplayName("Test validateAppKey with valid appKey")
	void testValidateAppKeyValid() {
		ParamValidator.Validate result = ApiSecretParamValidator.validateAppKey(SecretUtils.APP_KEY);
		Assertions.assertThat(result.isValidate()).isTrue();
	}

	@Test
	@DisplayName("Test validateAppKey with invalid appKey")
	void testValidateAppKeyInvalid() {
		ParamValidator.Validate result = ApiSecretParamValidator.validateAppKey("invalidKey");
		Assertions.assertThat(result.isValidate()).isFalse();
		Assertions.assertThat(result.getMessage()).isEqualTo("appKey不存在");
	}

	@Test
	@DisplayName("Test validateAppKey with empty appKey")
	void testValidateAppKeyEmpty() {
		ParamValidator.Validate result = ApiSecretParamValidator.validateAppKey("");
		Assertions.assertThat(result.isValidate()).isFalse();
		Assertions.assertThat(result.getMessage()).isEqualTo("appKey不为空");
	}

	@Test
	@DisplayName("Test validateAppKey with null appKey")
	void testValidateAppKeyNull() {
		ParamValidator.Validate result = ApiSecretParamValidator.validateAppKey(null);
		Assertions.assertThat(result.isValidate()).isFalse();
		Assertions.assertThat(result.getMessage()).isEqualTo("appKey不为空");
	}

	@Test
	@DisplayName("Test validateAppSecret with valid appSecret")
	void testValidateAppSecretValid() {
		ParamValidator.Validate result = ApiSecretParamValidator.validateAppSecret(SecretUtils.APP_SECRET);
		Assertions.assertThat(result.isValidate()).isTrue();
	}

	@Test
	@DisplayName("Test validateAppSecret with invalid appSecret")
	void testValidateAppSecretInvalid() {
		ParamValidator.Validate result = ApiSecretParamValidator.validateAppSecret("invalidSecret");
		Assertions.assertThat(result.isValidate()).isFalse();
		Assertions.assertThat(result.getMessage()).isEqualTo("appSecret不存在");
	}

	@Test
	@DisplayName("Test validateAppSecret with empty appSecret")
	void testValidateAppSecretEmpty() {
		ParamValidator.Validate result = ApiSecretParamValidator.validateAppSecret("");
		Assertions.assertThat(result.isValidate()).isFalse();
		Assertions.assertThat(result.getMessage()).isEqualTo("appSecret不为空");
	}

	@Test
	@DisplayName("Test validateNonce with valid nonce")
	void testValidateNonceValid() {
		ParamValidator.Validate result = ApiSecretParamValidator.validateNonce("randomNonce123");
		Assertions.assertThat(result.isValidate()).isTrue();
	}

	@Test
	@DisplayName("Test validateNonce with empty nonce")
	void testValidateNonceEmpty() {
		ParamValidator.Validate result = ApiSecretParamValidator.validateNonce("");
		Assertions.assertThat(result.isValidate()).isFalse();
		Assertions.assertThat(result.getMessage()).isEqualTo("nonce不为空");
	}

	@Test
	@DisplayName("Test validateTimestamp with valid timestamp")
	void testValidateTimestampValid() {
		String timestamp = String.valueOf(System.currentTimeMillis());
		ParamValidator.Validate result = ApiSecretParamValidator.validateTimestamp(timestamp);
		Assertions.assertThat(result.isValidate()).isTrue();
	}

	@Test
	@DisplayName("Test validateTimestamp with expired timestamp")
	void testValidateTimestampExpired() {
		// Timestamp from 2 minutes ago (beyond 60 second timeout)
		String timestamp = String.valueOf(System.currentTimeMillis() - 120000);
		ParamValidator.Validate result = ApiSecretParamValidator.validateTimestamp(timestamp);
		Assertions.assertThat(result.isValidate()).isFalse();
		Assertions.assertThat(result.getMessage()).isEqualTo("timestamp已过期");
	}

	@Test
	@DisplayName("Test validateTimestamp with future timestamp")
	void testValidateTimestampFuture() {
		// Timestamp from 2 minutes in future (beyond 60 second tolerance)
		String timestamp = String.valueOf(System.currentTimeMillis() + 120000);
		ParamValidator.Validate result = ApiSecretParamValidator.validateTimestamp(timestamp);
		Assertions.assertThat(result.isValidate()).isFalse();
		Assertions.assertThat(result.getMessage()).isEqualTo("timestamp已过期");
	}

	@Test
	@DisplayName("Test validateTimestamp with empty timestamp")
	void testValidateTimestampEmpty() {
		ParamValidator.Validate result = ApiSecretParamValidator.validateTimestamp("");
		Assertions.assertThat(result.isValidate()).isFalse();
		Assertions.assertThat(result.getMessage()).isEqualTo("timestamp不为空");
	}

	@Test
	@DisplayName("Test validateSign with valid signature")
	void testValidateSignValid() {
		String appKey = SecretUtils.APP_KEY;
		String appSecret = SecretUtils.APP_SECRET;
		String nonce = "testNonce";
		String timestamp = String.valueOf(System.currentTimeMillis());
		Map<String, String> map = new LinkedHashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");

		// Generate the correct sign
		String params = "key1=value1&key2=value2";
		String correctSign = SecretUtils.sign(appKey, appSecret, nonce, timestamp, params);

		ParamValidator.Validate result = ApiSecretParamValidator.validateSign(appKey, appSecret, correctSign, nonce,
				timestamp, map);
		Assertions.assertThat(result.isValidate()).isTrue();
	}

	@Test
	@DisplayName("Test validateSign with invalid signature")
	void testValidateSignInvalid() {
		String appKey = SecretUtils.APP_KEY;
		String appSecret = SecretUtils.APP_SECRET;
		String nonce = "testNonce";
		String timestamp = String.valueOf(System.currentTimeMillis());
		Map<String, String> map = new HashMap<>();
		map.put("key1", "value1");

		ParamValidator.Validate result = ApiSecretParamValidator.validateSign(appKey, appSecret, "wrongSign", nonce,
				timestamp, map);
		Assertions.assertThat(result.isValidate()).isFalse();
		Assertions.assertThat(result.getMessage()).isEqualTo("Api验签失败");
	}

	@Test
	@DisplayName("Test validateSign with empty sign")
	void testValidateSignEmpty() {
		Map<String, String> map = new HashMap<>();
		ParamValidator.Validate result = ApiSecretParamValidator.validateSign(SecretUtils.APP_KEY,
				SecretUtils.APP_SECRET, "", "nonce", "timestamp", map);
		Assertions.assertThat(result.isValidate()).isFalse();
		Assertions.assertThat(result.getMessage()).isEqualTo("sign不能为空");
	}

	@Test
	@DisplayName("Test validateSign with empty params map")
	void testValidateSignWithEmptyParamsMap() {
		String appKey = SecretUtils.APP_KEY;
		String appSecret = SecretUtils.APP_SECRET;
		String nonce = "testNonce";
		String timestamp = String.valueOf(System.currentTimeMillis());
		Map<String, String> emptyMap = new LinkedHashMap<>();

		// Generate sign with empty params
		String correctSign = SecretUtils.sign(appKey, appSecret, nonce, timestamp, "");

		ParamValidator.Validate result = ApiSecretParamValidator.validateSign(appKey, appSecret, correctSign, nonce,
				timestamp, emptyMap);
		Assertions.assertThat(result.isValidate()).isTrue();
	}

	// ==================== Integration Tests ====================

	@Test
	@DisplayName("Test complete API signature workflow")
	void testCompleteApiSignatureWorkflow() {
		// Simulate a complete API call with signature verification
		String appKey = SecretUtils.APP_KEY;
		String appSecret = SecretUtils.APP_SECRET;
		String nonce = UUID.randomUUID().toString();
		String timestamp = String.valueOf(System.currentTimeMillis());

		// Build request parameters
		Map<String, String> requestParams = new LinkedHashMap<>();
		requestParams.put("action", "query");
		requestParams.put("userId", "12345");

		// Generate signature (client side)
		String params = "action=query&userId=12345";
		String sign = SecretUtils.sign(appKey, appSecret, nonce, timestamp, params);

		// Validate all parameters (server side)
		ParamValidator.Validate appKeyResult = ApiSecretParamValidator.validateAppKey(appKey);
		ParamValidator.Validate appSecretResult = ApiSecretParamValidator.validateAppSecret(appSecret);
		ParamValidator.Validate nonceResult = ApiSecretParamValidator.validateNonce(nonce);
		ParamValidator.Validate timestampResult = ApiSecretParamValidator.validateTimestamp(timestamp);
		ParamValidator.Validate signResult = ApiSecretParamValidator.validateSign(appKey, appSecret, sign, nonce,
				timestamp, requestParams);

		Assertions.assertThat(appKeyResult.isValidate()).isTrue();
		Assertions.assertThat(appSecretResult.isValidate()).isTrue();
		Assertions.assertThat(nonceResult.isValidate()).isTrue();
		Assertions.assertThat(timestampResult.isValidate()).isTrue();
		Assertions.assertThat(signResult.isValidate()).isTrue();
	}

}
