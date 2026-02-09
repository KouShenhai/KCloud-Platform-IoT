/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.model.valueobject;

import lombok.Builder;

import java.util.List;

/**
 * 用户值对象.
 *
 * @author laokou
 * @param username 用户名.
 * @param mail 邮箱.
 * @param mobile 手机号.
 * @param tenantId 租户ID.
 * @param password 密码.
 * @param avatar 头像.
 * @param permissions 权限标识集合.
 * @param tenantCode 租户编码.
 * @param dataScopes 数据权限.
 */
@Builder(toBuilder = true)
public record UserV(String username, String password, String avatar, String mail, String mobile, String tenantCode,
		Long tenantId, List<String> permissions, List<String> dataScopes) {
}
