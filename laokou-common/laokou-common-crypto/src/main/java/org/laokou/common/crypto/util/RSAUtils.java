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

package org.laokou.common.crypto.util;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.laokou.common.i18n.util.StringExtUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密与解密.
 *
 * @author laokou
 */
@Slf4j
public final class RSAUtils {

	private static final String PUBLIC_KEY;

	private static final String PRIVATE_KEY;

	static {
		try {
			PUBLIC_KEY = getKey("/conf/publicKey.scr");
			PRIVATE_KEY = getKey("/conf/privateKey.scr");
		}
		catch (IOException ex) {
			log.error("读取私钥或密钥失败，错误信息：{}", ex.getMessage(), ex);
			throw new IllegalArgumentException(ex);
		}
	}

	private RSAUtils() {

	}

	public static String getKey(String path) throws IOException {
		return ResourceExtUtils.getResource(path).getContentAsString(StandardCharsets.UTF_8).trim();
	}

	public static PublicKey getRSAPublicKey(String publicKey, KeyFactory keyFactory) throws InvalidKeySpecException {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(getPublicKey(publicKey));
		return keyFactory.generatePublic(x509KeySpec);
	}

	public static PrivateKey getRSAPrivateKey(String privateKey, KeyFactory keyFactory) throws InvalidKeySpecException {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(getPrivateKey(privateKey));
		return keyFactory.generatePrivate(pkcs8KeySpec);
	}

	public static KeyFactory getKeyFactory() throws NoSuchAlgorithmException, NoSuchProviderException {
		return KeyFactory.getInstance("RSA", "SunRsaSign");
	}

	/**
	 * 根据私钥解密.
	 * @param str 字符串
	 * @param privateKey 私钥
	 * @return 解密后的字符串
	 */
	public static String decryptByPrivateKey(String str, String privateKey) {
		if (StringExtUtils.isNotEmpty(str)) {
			try {
				return new String(decryptByRSAPrivateKey(str, privateKey), StandardCharsets.UTF_8);
			}
			catch (Exception ex) {
				log.error("RSA解密失败【私钥】，错误信息：{}", ex.getMessage(), ex);
				throw new SystemException("S_RSA_DecryptFailedByPrivateKey", "RSA解密失败，请检查私钥是否正确", ex);
			}
		}
		return str;
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
	 * @param publicKey 公钥
	 * @return 加密后的字符串
	 */
	public static String encryptByPublicKey(String str, String publicKey) {
		if (StringExtUtils.isNotEmpty(str)) {
			try {
				return encryptBase64(encryptByRSAPublicKey(str, publicKey));
			}
			catch (Exception e) {
				log.error("RSA加密失败【公钥】，错误信息：{}", e.getMessage(), e);
				throw new SystemException("S_RSA_EncryptFailedByPublicKey", "RSA加密失败，请检查公钥是否正确", e);
			}
		}
		return str;
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
		return Base64.getMimeDecoder().decode(str);
	}

	/**
	 * base64加密.
	 * @param strBytes 字符串
	 * @return 加密后的字符串
	 */
	private static String encryptBase64(byte[] strBytes) {
		return Base64.getEncoder().encodeToString(strBytes);
	}

	/**
	 * 根据公钥加密.
	 * @param str 字符串
	 * @param key 公钥
	 * @return 加密后的字符串
	 */
	private static byte[] encryptByRSAPublicKey(String str, String key)
			throws InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		KeyFactory keyFactory = getKeyFactory();
		PublicKey publicKey = getRSAPublicKey(key, keyFactory);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 根据私钥解密.
	 * @param str 字符串
	 * @param key 私钥
	 * @return 解密后的字符串
	 */
	private static byte[] decryptByRSAPrivateKey(String str, String key)
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		KeyFactory keyFactory = getKeyFactory();
		PrivateKey privateKey = getRSAPrivateKey(key, keyFactory);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(decryptBase64(str));
	}

	private static byte[] getPublicKey(String publicKey) {
		return StringExtUtils.isNotEmpty(publicKey) ? decryptBase64(publicKey) : decryptBase64(PUBLIC_KEY);
	}

	private static byte[] getPrivateKey(String privateKey) {
		return StringExtUtils.isNotEmpty(privateKey) ? decryptBase64(privateKey) : decryptBase64(PRIVATE_KEY);
	}

}
