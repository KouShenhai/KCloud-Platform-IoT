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

package org.laokou.common.context.util;

import org.springframework.security.jackson.CoreJacksonModule;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @param id 用户ID.
 * @param username 用户名.
 * @param password 密码.
 * @param avatar 头像.
 * @param superAdmin 超级管理员标识.
 * @param status 用户状态 0启用 1禁用.
 * @param mail 邮箱.
 * @param mobile 手机号.
 * @param tenantId 租户ID.
 * @param deptId 部门ID.
 * @param permissions 菜单权限标识集合.
 * @param deptIds 部门IDS.
 * @param creator 创建者.
 * @author laokou
 * @see CoreJacksonModule
 */
public record UserExtDetails(Long id, String username, String password, String avatar, Boolean superAdmin,
		Integer status, String mail, String mobile, Long tenantId, Long deptId, Set<String> permissions,
		Set<Long> deptIds, Long creator) implements Serializable {
	@Serial
	private static final long serialVersionUID = 3319752558160144611L;

}
