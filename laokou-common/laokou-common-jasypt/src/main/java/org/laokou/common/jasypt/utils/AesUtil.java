/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
import org.apache.hc.client5.http.utils.Base64;
import org.laokou.common.core.utils.ResourceUtil;
import org.laokou.common.jasypt.annotation.Aes;
import org.springframework.util.Assert;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public class AesUtil {

	private static byte[] getSecretKey() {
		String secretKey = getKey();
		byte[] bytes = secretKey.getBytes(StandardCharsets.UTF_8);
		Assert.isTrue(bytes.length == 16, "密钥长度必须16位");
		return bytes;
	}

	@SneakyThrows
	private static String getKey() {
		byte[] bytes = ResourceUtil.getResource("conf/secretKey.b16").getInputStream().readAllBytes();
		return new String(bytes, StandardCharsets.UTF_8);
	}

	@SneakyThrows
	public static String encrypt(String data) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(getSecretKey(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[16]));
		byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
		return Base64.encodeBase64String(bytes);
	}

	@SneakyThrows
	public static String decrypt(String data) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(getSecretKey(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[16]));
		byte[] bytes = Base64.decodeBase64(data);
		return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
	}

	public static void transform(Object obj) throws IllegalAccessException {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			boolean annotationPresent = field.isAnnotationPresent(Aes.class);
			if (annotationPresent) {
				// 私有属性
				field.setAccessible(true);
				Object o = field.get(obj);
				if (o == null) {
					continue;
				}
				String data = o.toString();
				Aes aes = field.getAnnotation(Aes.class);
				switch (aes.type()) {
					case DECRYPT -> data = decrypt(data);
					case ENCRYPT -> data = encrypt(data);
					default -> {
					}
				}
				// 属性赋值
				field.set(obj, data);
			}
		}
	}

}
