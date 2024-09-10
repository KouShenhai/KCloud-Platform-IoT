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

package org.laokou.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * 只针对 spring-boot 3.x.x.
 *
 * @author laokou
 */
@Slf4j
class Jasypt3Test {

	@Test
	void testJasypt3() {
		String factor = "slat";
		String plainText = "laokou";
		String encryptWithMD5ANDAES256Str = encryptWithHMACSHA512ANDAES256(plainText, factor);
		String decryptWithMD5ANDAES256Str = decryptWithHMACSHA512ANDAES256(encryptWithMD5ANDAES256Str, factor);
		log.info("采用PBEWITHHMACSHA512ANDAES_256加密前原文密文：{}", encryptWithMD5ANDAES256Str);
		log.info("采用PBEWITHHMACSHA512ANDAES_256解密后密文原文：{}", decryptWithMD5ANDAES256Str);
		Assertions.assertArrayEquals(decryptWithMD5ANDAES256Str.getBytes(StandardCharsets.UTF_8),
			plainText.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Jasypt 3.x 加密（PBEWITHHMACSHA512ANDAES_256）.
	 *
	 * @param plainText 待加密的原文
	 * @param factor    加密秘钥
	 * @return java.lang.String
	 */
	public String encryptWithHMACSHA512ANDAES256(String plainText, String factor) {
		return pooledPBEStringEncryptor(factor).encrypt(plainText);
	}

	/**
	 * Jasypt 3.x 解密（PBEWITHHMACSHA512ANDAES_256）.
	 *
	 * @param encryptedText 待解密密文
	 * @param factor        解密秘钥
	 * @return java.lang.String
	 */
	public String decryptWithHMACSHA512ANDAES256(String encryptedText, String factor) {
		return pooledPBEStringEncryptor(factor).decrypt(encryptedText);
	}

	public PooledPBEStringEncryptor pooledPBEStringEncryptor(String factor) {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(factor);
		config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		return encryptor;
	}

}
