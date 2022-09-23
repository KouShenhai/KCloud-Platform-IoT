/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.common.utils;
/**
 * @author  Kou Shenhai
 * @since 1.0.0
 */
public final class RedisKeyUtil {

    /**
     * 验证码Key
     */
    public final static String getUserCaptchaKey(String uuid) {
        return "sys:user:captcha:" + uuid;
    }

    /**
     * 用户资源Key
     */
    public final static String getUserResourceKey(Long userId) {
        return "sys:user:resource:" + userId;
    }

    /**
     * 用户信息Key
     */
    public final static String getUserInfoKey(Long userId) {
        return "sys:user:info:" + userId;
    }
}
