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

package org.laokou.common.shardingsphere.utils;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.laokou.common.i18n.common.SysConstants.*;

/**
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * <p>
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">...</a>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * @author alibaba
 */
@Slf4j
public class CryptoUtil {

	/**
	 * 默认公钥配置.
	 */
	public static final String DEFAULT_PUBLIC_KEY_STRING = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJ4o6sn4WoPmbs7DR9mGQzuuUQM9erQTVPpwxIzB0ETYkyKffO097qXVRLA6KPmaV+/siWewR7vpfYYjWajw5KkCAwEAAQ==";

	/**
	 * 默认私钥配置.
	 */
	private static final String DEFAULT_PRIVATE_KEY_STRING = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAnijqyfhag+ZuzsNH2YZDO65RAz16tBNU+nDEjMHQRNiTIp987T3updVEsDoo+ZpX7+yJZ7BHu+l9hiNZqPDkqQIDAQABAkBgErbczRIewWFaE+GXTymUHUV01Gmu7XdXUhzy6+CZkIcEnyTpUgPilGUydiIyeiY8usvWKGjFWxLoKeJDY1wBAiEA5M9uqc9XpL5uitLWHiiq7pRxhnJb/B+wZyHqLVhCLekCIQCw9D/Fsx7vHRgymWYExHvCka7w5SyWUmNzQOOKjZUIwQIhAMqbo7JaF5GZzui+qTsrZ7C7YYtb2Hf414t7TJG6hV+BAiBXuZ7r+fL6A+h9HUNQVcAtI2AhGNxT4aBgAOlNRQd/gQIgCGqaZsOdnL9624SI1DwhBt4x24q3350pWwzgfl4Kbbo=";

	public static String decrypt(String cipherText) throws Exception {
		return decrypt((String) null, cipherText);
	}

	public static String decrypt(String publicKeyText, String cipherText) throws Exception {
		PublicKey publicKey = getPublicKey(publicKeyText);
		return decrypt(publicKey, cipherText);
	}

	public static PublicKey getPublicKeyByX509(String x509File) {
		if (ObjectUtil.isNull(x509File) || x509File.isEmpty()) {
			return getPublicKey(null);
		}
		try (FileInputStream in = new FileInputStream(x509File)) {
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			Certificate cer = factory.generateCertificate(in);
			return cer.getPublicKey();
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Failed to get public key", e);
		}
	}

	public static PublicKey getPublicKey(String publicKeyText) {
		if (ObjectUtil.isNull(publicKeyText) || publicKeyText.isEmpty()) {
			publicKeyText = DEFAULT_PUBLIC_KEY_STRING;
		}

		try {
			byte[] publicKeyBytes = Base64Util.base64ToByteArray(publicKeyText);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);

			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA, "SunRsaSign");
			return keyFactory.generatePublic(x509KeySpec);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Failed to get public key", e);
		}
	}

	public static PublicKey getPublicKeyByPublicKeyFile(String publicKeyFile) {
		if (ObjectUtil.isNull(publicKeyFile) || publicKeyFile.isEmpty()) {
			return getPublicKey(null);
		}
		try (FileInputStream in = new FileInputStream(publicKeyFile)) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int len;
			byte[] b = new byte[512 / 8];
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}

			byte[] publicKeyBytes = out.toByteArray();
			X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
			KeyFactory factory = KeyFactory.getInstance(ALGORITHM_RSA, "SunRsaSign");
			return factory.generatePublic(spec);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Failed to get public key", e);
		}
	}

	public static String decrypt(PublicKey publicKey, String cipherText) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		try {
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
		}
		catch (InvalidKeyException e) {
			// 因为 IBM JDK 不支持私钥加密, 公钥解密, 所以要反转公私钥
			// 也就是说对于解密, 可以通过公钥的参数伪造一个私钥对象欺骗 IBM JDK
			RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
			RSAPrivateKeySpec spec = new RSAPrivateKeySpec(rsaPublicKey.getModulus(), rsaPublicKey.getPublicExponent());
			Key fakePrivateKey = KeyFactory.getInstance(ALGORITHM_RSA).generatePrivate(spec);
			// It is a stateful object. so we need to get new one.
			cipher = Cipher.getInstance(ALGORITHM_RSA);
			cipher.init(Cipher.DECRYPT_MODE, fakePrivateKey);
		}

		if (ObjectUtil.isNull(cipherText) || cipherText.isEmpty()) {
			return cipherText;
		}

		byte[] cipherBytes = Base64Util.base64ToByteArray(cipherText);
		byte[] plainBytes = cipher.doFinal(cipherBytes);

		return new String(plainBytes);
	}

	public static String encrypt(String plainText) throws Exception {
		return encrypt((String) null, plainText);
	}

	public static String encrypt(String key, String plainText) throws Exception {
		if (StringUtil.isEmpty(key)) {
			key = DEFAULT_PRIVATE_KEY_STRING;
		}

		byte[] keyBytes = Base64Util.base64ToByteArray(key);
		return encrypt(keyBytes, plainText);
	}

	public static String encrypt(byte[] keyBytes, String plainText) throws Exception {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory factory = KeyFactory.getInstance(ALGORITHM_RSA, "SunRsaSign");
		PrivateKey privateKey = factory.generatePrivate(spec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		try {
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		}
		catch (InvalidKeyException e) {
			// For IBM JDK, 原因请看解密方法中的说明
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(rsaPrivateKey.getModulus(),
					rsaPrivateKey.getPrivateExponent());
			Key fakePublicKey = KeyFactory.getInstance(ALGORITHM_RSA).generatePublic(publicKeySpec);
			cipher = Cipher.getInstance(ALGORITHM_RSA);
			cipher.init(Cipher.ENCRYPT_MODE, fakePublicKey);
		}
		return Base64Util.byteArrayToBase64(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
	}

	public static byte[][] genKeyPairBytes(int keySize) {
		byte[][] keyPairBytes = new byte[2][];
		try {
			KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM_RSA, "SunRsaSign");
			gen.initialize(keySize, new SecureRandom());
			KeyPair pair = gen.generateKeyPair();

			keyPairBytes[0] = pair.getPrivate().getEncoded();
			keyPairBytes[1] = pair.getPublic().getEncoded();

		}
		catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
		}
		return keyPairBytes;
	}

	public static String[] genKeyPair(int keySize) {
		byte[][] keyPairBytes = genKeyPairBytes(keySize);
		String[] keyPairs = new String[2];
		keyPairs[0] = Base64Util.byteArrayToBase64(keyPairBytes[0]);
		keyPairs[1] = Base64Util.byteArrayToBase64(keyPairBytes[1]);
		return keyPairs;
	}

	public static void main(String[] args) {
		// String password = encrypt("laokou123");
		// String root = encrypt("root");
		// System.out.println(CRYPTO_PREFIX + root + CRYPTO_SUFFIX);
		// System.out.println(CRYPTO_PREFIX + password + CRYPTO_SUFFIX);
	}

}
