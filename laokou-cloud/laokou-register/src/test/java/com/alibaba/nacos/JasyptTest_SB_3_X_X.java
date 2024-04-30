/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package com.alibaba.nacos;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * 只针对 spring-boot 3.x.x.
 *
 * @author laokou
 */
@Slf4j
public class JasyptTest_SB_3_X_X {

	public static void main(String[] args) {
		String factor = "slat";
		String plainText = "laokou";
		String encryptWithMD5Str = encryptWithHMACSHA512ANDAES(plainText, factor);
		String decryptWithMD5Str = decryptWithHMACSHA512ANDAES(encryptWithMD5Str, factor);
		log.info("采用HMACSHA512ANDAES加密前原文密文：" + encryptWithMD5Str);
		log.info("采用HMACSHA512ANDAES解密后密文原文:" + decryptWithMD5Str);
	}

	/**
	 * Jasypt 3.x 加密（PBEWITHHMACSHA512ANDAES_256）.
	 * @param plainText 待加密的原文
	 * @param factor 加密秘钥
	 * @return java.lang.String
	 */
	public static String encryptWithHMACSHA512ANDAES(String plainText, String factor) {
		return pooledPBEStringEncryptor(factor).encrypt(plainText);
	}

	/**
	 * Jasypt 3.x 解密（PBEWITHHMACSHA512ANDAES_256）.
	 * @param encryptedText 待解密密文
	 * @param factor 解密秘钥
	 * @return java.lang.String
	 */
	public static String decryptWithHMACSHA512ANDAES(String encryptedText, String factor) {
		return pooledPBEStringEncryptor(factor).decrypt(encryptedText);
	}

	public static PooledPBEStringEncryptor pooledPBEStringEncryptor(String factor) {
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
