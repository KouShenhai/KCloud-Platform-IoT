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
import org.laokou.common.crypto.annotation.Aes;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.ResourceUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.laokou.common.i18n.common.constants.StringConstant.EMPTY;

/**
 * AES加密工具类.
 *
 * @author laokou
 */
public class AESUtil {

	private static final String AES = "AES";

	private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";

	private static final int GCM_TAG_LENGTH = 16;

	private static final int GCM_IV_LENGTH = 12;

	private final static SecretKey SECRET_KEY;

	private final static byte[] SECRET_IV;

	static {
		try (InputStream inputStream1 = ResourceUtil.getResource("conf/secretKey.b256").getInputStream();
			 InputStream inputStream2 = ResourceUtil.getResource("conf/secretIV.b12").getInputStream()) {
			String key = new String(inputStream1.readAllBytes(), StandardCharsets.UTF_8).trim();
			Assert.isTrue(key.length() == 32, "密钥长度必须32位");
			String iv = new String(inputStream2.readAllBytes(), StandardCharsets.UTF_8).trim();
			SECRET_KEY = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES);
			SECRET_IV = iv.getBytes(StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static SecretKey getSecretKey() {
		return SECRET_KEY;
	}

	public static SecretKey getSecretKey(String key) {
		return new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES);
	}

	public static byte[] getSecretIV() {
		return SECRET_IV;
	}

	@SneakyThrows
	public static String encrypt(String plainText) {
		return encrypt(plainText, SECRET_KEY, SECRET_IV);
	}

	public static String encrypt(String plainText, SecretKey key, byte[] iv) throws Exception {
		if (StringUtil.isEmpty(plainText)) {
			return EMPTY;
		}
		Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
		GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
		byte[] encryptedIVAndText = new byte[GCM_IV_LENGTH + encryptedBytes.length];
		System.arraycopy(iv, 0, encryptedIVAndText, 0, GCM_IV_LENGTH);
		System.arraycopy(encryptedBytes, 0, encryptedIVAndText, GCM_IV_LENGTH, encryptedBytes.length);
		return Base64.getEncoder().encodeToString(encryptedIVAndText);
	}

	@SneakyThrows
	public static String decrypt(String encryptedText) {
		return decrypt(encryptedText, SECRET_KEY, SECRET_IV);
	}

	public static String decrypt(String encryptedText, SecretKey key, byte[] iv) throws Exception {
		if (StringUtil.isEmpty(encryptedText)) {
			return EMPTY;
		}
		byte[] decoded = Base64.getDecoder().decode(encryptedText);
		Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
		GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
		cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
		byte[] decryptedBytes = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);
		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}

	/**
	 * 对象属性加密/解密.
	 * @param obj 对象
	 * @throws IllegalAccessException 异常
	 */
	public static void transform(Object obj) throws IllegalAccessException {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			boolean annotationPresent = field.isAnnotationPresent(Aes.class);
			if (annotationPresent) {
				// 私有属性
				field.setAccessible(true);
				Object o = field.get(obj);
				if (ObjectUtil.isNull(o)) {
					continue;
				}
				String data = o.toString();
				Aes aes = field.getAnnotation(Aes.class);
				data = switch (aes.type()) {
					case DECRYPT -> decrypt(data);
					case ENCRYPT -> encrypt(data);
				};
				// 属性赋值
				field.set(obj, data);
			}
		}
	}

}
