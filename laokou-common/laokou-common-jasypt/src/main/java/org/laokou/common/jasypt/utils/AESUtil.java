/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.jasypt.utils;

import lombok.SneakyThrows;
import org.apache.hc.client5.http.utils.Base64;
import org.laokou.common.core.utils.ResourceUtil;
import org.laokou.common.core.utils.StringUtil;
import org.springframework.util.Assert;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
/**
 * @author laokou
 */
public class AESUtil {


    private static final ThreadLocal<String> SECRET_KEY_LOCAL;

    static {
        try {
            byte[] bytes = ResourceUtil.getResource("secret_key.b64").getInputStream().readAllBytes();
            SECRET_KEY_LOCAL = ThreadLocal.withInitial(() -> new String(bytes,StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getSecretKey() {
        String secretKey = getKey();
        Assert.notNull(StringUtil.isEmpty(secretKey),"密钥不能为空，请填写密钥");
        byte[] bytes = secretKey.getBytes(StandardCharsets.UTF_8);
        Assert.isTrue(bytes.length == 16,"密钥长度必须16位");
        return bytes;
    }

    public static String getKey() {
        return SECRET_KEY_LOCAL.get();
    }

    public static void setKey(String secretKey) {
        SECRET_KEY_LOCAL.remove();
        SECRET_KEY_LOCAL.set(secretKey);
    }

    @SneakyThrows
    public static String encrypt(String data) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(getSecretKey(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,new IvParameterSpec(new byte[16]));
        byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(bytes);
    }

    @SneakyThrows
    public static String decrypt(String data) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(getSecretKey(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,new IvParameterSpec(new byte[16]));
        byte[] bytes = Base64.decodeBase64(data);
        return new String(cipher.doFinal(bytes),StandardCharsets.UTF_8);
    }

}
