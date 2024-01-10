/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
package org.laokou.common.jasypt.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.laokou.common.core.utils.ResourceUtil;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

import static org.laokou.common.i18n.common.SysConstants.ALGORITHM_RSA;

/**
 * @author laokou
 */
@Slf4j
public class RsaUtil {

	@SneakyThrows
	public static String decryptByPrivateKey(String body, String key) {
		byte[] bytes = decryptByPrivateKey(decryptBase64(body), decryptBase64(key));
		return new String(Objects.requireNonNull(bytes), StandardCharsets.UTF_8);
	}

	@SneakyThrows
	public static String encryptByPublicKey(String body, String key) {
		byte[] bytes = encryptByPublicKey(body.getBytes(StandardCharsets.UTF_8), decryptBase64(key));
		return decryptBase64(bytes);
	}

	@SneakyThrows
	public static String getPrivateKey() {
		try (InputStream inputStream = ResourceUtil.getResource("/conf/privateKey.scr").getInputStream()) {
			return new String(Objects.requireNonNull(inputStream.readAllBytes()), StandardCharsets.UTF_8);
		}
	}

	@SneakyThrows
	public static String getPublicKey() {
		try (InputStream inputStream = ResourceUtil.getResource("/conf/publicKey.scr").getInputStream()) {
			return new String(Objects.requireNonNull(inputStream.readAllBytes()), StandardCharsets.UTF_8);
		}
	}

	/**
	 * base64解密
	 * @param body 数据
	 */
	private static byte[] decryptBase64(String body) {
		return Base64.decodeBase64(body);
	}

	/**
	 * base64加密
	 * @param bodyBytes 数据
	 */
	private static String decryptBase64(byte[] bodyBytes) {
		return Base64.encodeBase64String(bodyBytes);
	}

	/**
	 * 通过公钥加密
	 * @param bodyBytes 加密字符
	 * @param keyBytes 公钥
	 */
	private static byte[] encryptByPublicKey(byte[] bodyBytes, byte[] keyBytes) throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(bodyBytes);
	}

	/**
	 * 通过私钥解密
	 * @param bodyBytes 加密字符
	 * @param keyBytes 私钥
	 */
	private static byte[] decryptByPrivateKey(byte[] bodyBytes, byte[] keyBytes) throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(bodyBytes);
	}

}
