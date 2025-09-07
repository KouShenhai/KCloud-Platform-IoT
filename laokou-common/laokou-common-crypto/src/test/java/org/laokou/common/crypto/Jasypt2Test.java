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

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 只针对 spring-boot 2.x.x.
 *
 * @author laokou
 */
@TestConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class Jasypt2Test {

	@Test
	void test_jasypt_2() {
		String factor = "laokou";
		String plainText = "laokou123";
		String encryptWithMD5AndDESStr = encryptWithMD5AndDES(plainText, factor);
		String decryptWithMD5AndDESStr = decryptWithMD5AndDES(encryptWithMD5AndDESStr, factor);
		assertThat(decryptWithMD5AndDESStr).isEqualTo(plainText);
	}

	/**
	 * Jasyp2.x 加密（PBEWithMD5AndDES）.
	 * @param plainText 待加密的原文
	 * @param factor 加密秘钥
	 * @return java.lang.String
	 */
	private String encryptWithMD5AndDES(String plainText, String factor) {
		// 3. 加密
		return encryptor(factor).encrypt(plainText);
	}

	/**
	 * Jaspy2.x 解密（PBEWithMD5AndDES）.
	 * @param encryptedText 待解密密文
	 * @param factor 解密秘钥
	 * @return java.lang.String
	 */
	private String decryptWithMD5AndDES(String encryptedText, String factor) {
		// 3. 解密
		return encryptor(factor).decrypt(encryptedText);
	}

	private StandardPBEStringEncryptor encryptor(String factor) {
		// 1. 创建加解密工具实例
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		// 2. 加解密配置
		EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setPassword(factor);
		encryptor.setConfig(config);
		return encryptor;
	}

}
