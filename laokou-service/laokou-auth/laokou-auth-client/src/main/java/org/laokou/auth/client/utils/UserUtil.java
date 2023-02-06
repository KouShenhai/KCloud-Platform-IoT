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
package org.laokou.auth.client.utils;

import org.laokou.auth.client.user.UserDetail;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author laokou
 */
public class UserUtil {

    public static UserDetail userDetail() {
        return (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        return userDetail().getUserId();
    }

    /**
     * 获取用户名
     * @return
     */
    public static String getUsername() {
        return userDetail().getUsername();
    }

    /**
     * 部门id
     * @return
     */
    public static Long getDeptId() {
        return userDetail().getDeptId();
    }

    public static Long getTenantId() {
        return userDetail().getTenantId();
    }

    public static String getSourceName() {
        return userDetail().getSourceName();
    }

}
