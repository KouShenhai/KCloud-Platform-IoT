/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.gateway.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author laokou
 */
public class RsaCoder {
    private static final Logger log = LoggerFactory.getLogger(RsaCoder.class);
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    public RsaCoder() {
    }

    public static byte[] decryptBase64(String key) {
        return Base64.decodeBase64(key);
    }

    public static String encryptBase64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = decryptBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(priKey);
        signature.update(data);
        return encryptBase64(signature.sign());
    }

    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = decryptBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(decryptBase64(sign));
    }

    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = decryptBase64(key);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPrivateKey(String data, String key) throws Exception {
        return decryptByPrivateKey(decryptBase64(data), key);
    }

    public static String decryptByPrivateKey(String data) throws Exception {
        byte[] bytes = decryptByPrivateKey(decryptBase64(data), getPrivateKey());
        return new String(bytes);
    }

    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = decryptBase64(key);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] encryptByPublicKey(String data, String key) throws Exception {
        byte[] keyBytes = decryptBase64(key);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicKey);
        return cipher.doFinal(data.getBytes());
    }

    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = decryptBase64(key);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, privateKey);
        return cipher.doFinal(data);
    }

    public static String getPrivateKey(Map<String, Key> keyMap) {
        Key key = keyMap.get("RSAPrivateKey");
        return encryptBase64(key.getEncoded());
    }

    public static String getPublicKey(Map<String, Key> keyMap) {
        Key key = keyMap.get("RSAPublicKey");
        return encryptBase64(key.getEncoded());
    }

    public static String getPrivateKey() {
        InputStream in = RsaCoder.class.getResourceAsStream("/conf/privateKey.scr");
        return new String(Objects.requireNonNull(readByte(in)));
    }

    public static String getPublicKey() {
        InputStream in = RsaCoder.class.getResourceAsStream("/conf/publicKey.scr");
        return new String(Objects.requireNonNull(readByte(in)));
    }

    public static Map<String, Key> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        Map<String, Key> keyMap = new HashMap(2);
        keyMap.put("RSAPublicKey", keyPair.getPublic());
        keyMap.put("RSAPrivateKey", keyPair.getPrivate());
        return keyMap;
    }

    public static byte[] readByte(InputStream is) {
        try {
            byte[] r = new byte[is.available()];
            is.read(r);
            return r;
        } catch (Exception var2) {
            log.error("context", var2);
            return null;
        }
    }
}
