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
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.laokou.common.core.utils.ResourceUtil;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Objects;

import static org.laokou.common.i18n.common.Constant.ALGORITHM_RSA;

/**
 * @author laokou
 */
@Slf4j
public class RsaUtil {

	/**
	 * base64解密
	 * @param key 密钥
	 */
	public static byte[] decryptBase64(String key) {
		return Base64.decodeBase64(key);
	}

	/**
	 * 通过私钥解密
	 * @param bytes 加密字符串
	 * @param keyBytes 私钥
	 */
	public static byte[] decryptByPrivateKey(byte[] bytes, byte[] keyBytes) throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(2, privateKey);
		return cipher.doFinal(bytes);
	}

	public static String decryptByPrivateKey(String data) throws Exception {
		byte[] bytes = decryptByPrivateKey(decryptBase64(data), decryptBase64(getPrivateKey()));
		return new String(Objects.requireNonNull(bytes), StandardCharsets.UTF_8);
	}

	@SneakyThrows
	public static String getPrivateKey() {
		byte[] bytes = ResourceUtil.getResource("/conf/privateKey.scr").getInputStream().readAllBytes();
		return new String(Objects.requireNonNull(bytes), StandardCharsets.UTF_8);
	}

	@SneakyThrows
	public static String getPublicKey() {
		byte[] bytes = ResourceUtil.getResource("/conf/publicKey.scr").getInputStream().readAllBytes();
		return new String(Objects.requireNonNull(bytes), StandardCharsets.UTF_8);
	}

}
