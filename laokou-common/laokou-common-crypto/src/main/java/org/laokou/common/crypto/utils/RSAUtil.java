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

package org.laokou.common.crypto.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.ResourceUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密与解密.
 *
 * @author laokou
 */
@Slf4j
public class RSAUtil {

	/**
	 * RSA算法.
	 */
	public static final String RSA = "RSA";

	/**
	 * RSA签名提供者.
	 */
	public static final String SUN_RSA_SIGN_PROVIDER = "SunRsaSign";

	private static final String PUBLIC_KEY;
	private static final String PRIVATE_KEY;

	static {
		try (InputStream inputStream1 = ResourceUtil.getResource("/conf/publicKey.scr").getInputStream();
			 InputStream inputStream2 = ResourceUtil.getResource("/conf/privateKey.scr").getInputStream()) {
			PUBLIC_KEY = new String(inputStream1.readAllBytes(), StandardCharsets.UTF_8).trim();
			PRIVATE_KEY = new String(inputStream2.readAllBytes(), StandardCharsets.UTF_8).trim();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据私钥解密.
	 * @param body 数据
	 * @param key 私钥
	 * @return 解密后的字符串
	 */
	@SneakyThrows
	public static String decryptByPrivateKey(String body, String key) {
		byte[] privateKey = StringUtil.isNotEmpty(key) ? decryptBase64(key) : decryptBase64(PRIVATE_KEY);
		byte[] bytes = decryptByPrivateKey(decryptBase64(body), privateKey);
		return new String(ObjectUtil.requireNotNull(bytes), StandardCharsets.UTF_8);
	}

	/**
	 * 根据私钥解密.
	 * @param body 数据
	 * @return 解密后的字符串
	 */
	@SneakyThrows
	public static String decryptByPrivateKey(String body) {
		byte[] privateKey = decryptBase64(PRIVATE_KEY);
		byte[] bytes = decryptByPrivateKey(decryptBase64(body), privateKey);
		return new String(ObjectUtil.requireNotNull(bytes), StandardCharsets.UTF_8);
	}

	/**
	 * 根据公钥加密.
	 * @param body 数据
	 * @param key 公钥
	 * @return 加密后的字符串
	 */
	@SneakyThrows
	public static String encryptByPublicKey(String body, String key) {
		byte[] publicKey = StringUtil.isNotEmpty(key) ? decryptBase64(key) : decryptBase64(PUBLIC_KEY);
		byte[] bytes = encryptByPublicKey(body.getBytes(StandardCharsets.UTF_8), publicKey);
		return encryptBase64(bytes);
	}

	/**
	 * 根据公钥加密.
	 * @param body 数据
	 * @return 加密后的字符串
	 */
	@SneakyThrows
	public static String encryptByPublicKey(String body) {
		byte[] publicKey = decryptBase64(PUBLIC_KEY);
		byte[] bytes = encryptByPublicKey(body.getBytes(StandardCharsets.UTF_8), publicKey);
		return encryptBase64(bytes);
	}

	/**
	 * 获取私钥.
	 * @return 私钥
	 */
	@SneakyThrows
	public static String getPrivateKey() {
		return PRIVATE_KEY;
	}

	/**
	 * 获取公钥.
	 * @return 公钥
	 */
	@SneakyThrows
	public static String getPublicKey() {
		return PUBLIC_KEY;
	}

	/**
	 * base64解密.
	 * @param body 数据
	 * @return 解密后的字符串
	 */
	private static byte[] decryptBase64(String body) {
		return Base64.getMimeDecoder().decode(body);
	}

	/**
	 * base64加密.
	 * @param bodyBytes 数据
	 * @return 加密后的字符串
	 */
	private static String encryptBase64(byte[] bodyBytes) {
		return Base64.getEncoder().encodeToString(bodyBytes);
	}

	/**
	 * 根据公钥加密.
	 * @param bodyBytes 加密字符
	 * @param keyBytes 公钥
	 * @return 加密后的字符串
	 */
	private static byte[] encryptByPublicKey(byte[] bodyBytes, byte[] keyBytes) throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA, SUN_RSA_SIGN_PROVIDER);
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(bodyBytes);
	}

	/**
	 * 根据私钥解密.
	 * @param bodyBytes 加密字符
	 * @param keyBytes 私钥
	 * @return 解密后的字符串
	 */
	private static byte[] decryptByPrivateKey(byte[] bodyBytes, byte[] keyBytes) throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA, SUN_RSA_SIGN_PROVIDER);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(bodyBytes);
	}

}
