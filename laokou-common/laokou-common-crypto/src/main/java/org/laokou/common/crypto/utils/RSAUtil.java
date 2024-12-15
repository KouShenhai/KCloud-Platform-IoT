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
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;

import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.laokou.common.core.utils.Base64Util.decodeOfMime;
import static org.laokou.common.core.utils.Base64Util.encodeToString;

/**
 * RSA加密与解密.
 *
 * @author laokou
 */
@Slf4j
public final class RSAUtil {

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
		try {
			PUBLIC_KEY = ResourceUtil.getResource("/conf/publicKey.scr")
				.getContentAsString(StandardCharsets.UTF_8)
				.trim();
			PRIVATE_KEY = ResourceUtil.getResource("/conf/privateKey.scr")
				.getContentAsString(StandardCharsets.UTF_8)
				.trim();
		}
		catch (IOException e) {
			log.error("读取私钥或密钥失败，错误信息：{}", e.getMessage(), e);
			throw new SystemException("S_UnKnow_Error", e.getMessage());
		}
	}

	/**
	 * 根据私钥解密.
	 * @param str 字符串
	 * @param key 私钥
	 * @return 解密后的字符串
	 */
	public static String decryptByPrivateKey(String str, String key) {
		try {
			byte[] privateKey = StringUtil.isNotEmpty(key) ? decryptBase64(key) : decryptBase64(PRIVATE_KEY);
			byte[] bytes = decryptByPrivateKey(decryptBase64(str), privateKey);
			return new String(ObjectUtil.requireNotNull(bytes), StandardCharsets.UTF_8);
		}
		catch (Exception e) {
			return str;
		}
	}

	/**
	 * 根据私钥解密.
	 * @param str 字符串
	 * @return 解密后的字符串
	 */
	public static String decryptByPrivateKey(String str) {
		return decryptByPrivateKey(str, PRIVATE_KEY);
	}

	/**
	 * 根据公钥加密.
	 * @param str 字符串
	 * @param key 公钥
	 * @return 加密后的字符串
	 */
	public static String encryptByPublicKey(String str, String key) {
		try {
			byte[] publicKey = StringUtil.isNotEmpty(key) ? decryptBase64(key) : decryptBase64(PUBLIC_KEY);
			byte[] bytes = encryptByPublicKey(str.getBytes(StandardCharsets.UTF_8), publicKey);
			return encryptBase64(bytes);
		}
		catch (Exception e) {
			return str;
		}
	}

	/**
	 * 根据公钥加密.
	 * @param str 字符串
	 * @return 加密后的字符串
	 */
	public static String encryptByPublicKey(String str) {
		return encryptByPublicKey(str, PUBLIC_KEY);
	}

	/**
	 * 获取公钥.
	 * @return 公钥
	 */
	public static String getPublicKey() {
		return PUBLIC_KEY;
	}

	/**
	 * base64解密.
	 * @param str 字符串
	 * @return 解密后的字符串
	 */
	private static byte[] decryptBase64(String str) {
		return decodeOfMime(str);
	}

	/**
	 * base64加密.
	 * @param strBytes 字符串
	 * @return 加密后的字符串
	 */
	private static String encryptBase64(byte[] strBytes) {
		return encodeToString(strBytes);
	}

	/**
	 * 根据公钥加密.
	 * @param strBytes 字符串
	 * @param keyBytes 公钥
	 * @return 加密后的字符串
	 */
	@SneakyThrows
	private static byte[] encryptByPublicKey(byte[] strBytes, byte[] keyBytes) {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA, SUN_RSA_SIGN_PROVIDER);
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(strBytes);
	}

	/**
	 * 根据私钥解密.
	 * @param strBytes 字符串
	 * @param keyBytes 私钥
	 * @return 解密后的字符串
	 */
	@SneakyThrows
	private static byte[] decryptByPrivateKey(byte[] strBytes, byte[] keyBytes) {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA, SUN_RSA_SIGN_PROVIDER);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(strBytes);
	}

}
