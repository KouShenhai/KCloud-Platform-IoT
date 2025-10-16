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

package org.laokou.common.crypto.util;

import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES加密工具类.
 *
 * @author laokou
 */
public final class AESUtils {

	private static final String AES = "AES";

	private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";

	private static final int GCM_TAG_LENGTH = 16;

	private static final int GCM_IV_LENGTH = 12;

	private final static SecretKey SECRET_KEY;

	private final static byte[] SECRET_IV;

	static {
		try {
			String key = ResourceExtUtils.getResource("conf/secretKey.b256")
				.getContentAsString(StandardCharsets.UTF_8)
				.trim();
			Assert.isTrue(key.length() == 32, "密钥长度必须32位");
			String iv = ResourceExtUtils.getResource("conf/secretIV.b12")
				.getContentAsString(StandardCharsets.UTF_8)
				.trim();
			SECRET_KEY = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES);
			SECRET_IV = iv.getBytes(StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private AESUtils() {
	}

	public static String encrypt(String plainText) throws Exception {
		return encrypt(plainText, SECRET_KEY, SECRET_IV);
	}

	public static String encrypt(String plainText, SecretKey key, byte[] iv) throws Exception {
		if (StringExtUtils.isEmpty(plainText)) {
			return StringConstants.EMPTY;
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

	public static String decrypt(String encryptedText) throws Exception {
		return decrypt(encryptedText, SECRET_KEY, SECRET_IV);
	}

	public static String decrypt(String encryptedText, SecretKey key, byte[] iv) throws Exception {
		if (StringExtUtils.isEmpty(encryptedText)) {
			return StringConstants.EMPTY;
		}
		byte[] decoded = Base64.getDecoder().decode(encryptedText);
		Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
		GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
		cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
		byte[] decryptedBytes = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);
		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}

}
