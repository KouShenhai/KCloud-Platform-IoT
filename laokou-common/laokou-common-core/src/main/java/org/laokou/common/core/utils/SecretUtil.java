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

package org.laokou.common.core.utils;

import org.laokou.common.i18n.core.CustomException;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public class SecretUtil {

    private static final long TIMEOUT_MILLIS = 60 * 1000L;

    public static boolean verification(String sign,String appKey,String appSecret,long timestamp,Long userId,String username,Long tenantId) {
        // 判断时间戳
        long nowTimestamp = System.currentTimeMillis();
        long maxTimestamp = nowTimestamp + TIMEOUT_MILLIS;
        long minTimestamp = nowTimestamp - TIMEOUT_MILLIS;
        if (timestamp > maxTimestamp || timestamp < minTimestamp) {
            throw new CustomException("验签失败，请检查配置");
        }
        String newSign = sign(appKey,appSecret,timestamp,userId,username,tenantId);
        if (!sign.equals(newSign)) {
            throw new CustomException("验签失败，请检查配置");
        }
        return true;
    }

    /**
     * MD5(appKey+appSecret+timestamp+userId+username+tenantId)转为小写
     */
    private static String sign(String appKey,String appSecret,long timestamp,Long userId,String username,Long tenantId) {
        String str = appKey
                + appSecret
                + timestamp
                + userId
                + username
                + tenantId;
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }

}
