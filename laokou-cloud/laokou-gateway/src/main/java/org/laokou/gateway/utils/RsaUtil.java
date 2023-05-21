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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.laokou.common.core.utils.ResourceUtil;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Objects;
/**
 * @author laokou
 */
@Slf4j
public class RsaUtil {

    /**
     * base64解密
     * @param key 私钥
     */
    public static byte[] decryptBase64(String key) {
        return Base64.decodeBase64(key);
    }

    /**
     * 通过私钥解密
     * @param data 加密字符串
     * @param key 私钥
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = decryptBase64(key);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, privateKey);
        return cipher.doFinal(data);
    }

    public static String decryptByPrivateKey(String data) throws Exception {
        byte[] bytes = decryptByPrivateKey(decryptBase64(data), getPrivateKey());
        return new String(bytes);
    }

    @SneakyThrows
    public static String getPrivateKey() {
        InputStream in = ResourceUtil.getResource("/conf/privateKey.scr").getInputStream();
        return new String(Objects.requireNonNull(readByte(in)));
    }

    @SneakyThrows
    public static byte[] readByte(InputStream inputStream) {
       return inputStream.readAllBytes();
    }
}
