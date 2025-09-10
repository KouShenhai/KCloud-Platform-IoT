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

package org.laokou.common.crypto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.crypto.util.RSAUtils;

/**
 * @author laokou
 */
class CryptoTest {

	private static final String USERNAME = "laokou";

	private static final String PASSWORD = "laokou123";

	@Test
	void test_rsa() {
		String encryptUsername = RSAUtils.encryptByPublicKey(USERNAME);
		String encryptPassword = RSAUtils.encryptByPublicKey(PASSWORD);
		String decryptUsername = RSAUtils.decryptByPrivateKey(encryptUsername);
		String decryptPassword = RSAUtils.decryptByPrivateKey(encryptPassword);
		Assertions.assertThat(decryptUsername).isEqualTo(USERNAME);
		Assertions.assertThat(decryptPassword).isEqualTo(PASSWORD);
	}

	@Test
	void test_aes() throws Exception {
		String encryptUsername = AESUtils.encrypt(USERNAME);
		String decryptUsername = AESUtils.decrypt(encryptUsername);
		Assertions.assertThat(decryptUsername).isEqualTo(USERNAME);
		String encryptPassword = AESUtils.encrypt(PASSWORD);
		String decryptPassword = AESUtils.decrypt(encryptPassword);
		Assertions.assertThat(decryptPassword).isEqualTo(PASSWORD);
	}

}
