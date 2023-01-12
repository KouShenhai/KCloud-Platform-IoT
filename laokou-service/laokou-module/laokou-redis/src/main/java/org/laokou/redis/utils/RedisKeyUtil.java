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
package org.laokou.redis.utils;
/**
 * @author laokou
 * @since 1.0.0
 */
public final class RedisKeyUtil {

    /**
     * 验证码Key
     */
    public static String getUserCaptchaKey(String uuid) {
        return "sys:user:captcha:" + uuid;
    }

    public static String getResourceTreeKey(Long userId) {
        return "sys:resource:tree:" + userId;
    }

    /**
     * 用户信息Key
     */
    public static String getUserInfoKey(String token) {
        return "sys:user:info:" + token;
    }

    /**
     * 二级缓存Key
     */
    public static String getDoubleCacheKey(String name,Long id) {
        return "sys:" + name + ":cache:" + id;
    }

    /**
     * 布隆过滤器Key
     */
    public static String getBloomFilterKey() {
        return "sys:bloom:filter";
    }

    /**
     * OSS配置Key
     */
    public static String getOssConfigKey() {
        return "sys:oss:config";
    }

    /**
     * 系统部门Key
     */
    public static String getDeptAllKey() {
        return "sys:dept:all";
    }

    /**
     * 消息消费Key
     * @return
     */
    public static String getMessageConsumeKey() {
        return "sys:message:consume";
    }

    /**
     * 创建索引Key
     * @return
     */
    public static String getCreateIndexKey() {
        return "sys:es:create";
    }

    /**
     * 删除索引Key
     * @return
     */
    public static String getDeleteIndexKey() {
        return "sys:es:delete";
    }

    /**
     * 同步索引Key
     * @return
     */
    public static String getSyncIndexKey() {
        return "sys:es:sync";
    }

    /**
     * 亚马逊S3Key
     */
    public static String getAmazonS3Key(Long id) {
        return "sys:amazon:s3:" + id;
    }
}
