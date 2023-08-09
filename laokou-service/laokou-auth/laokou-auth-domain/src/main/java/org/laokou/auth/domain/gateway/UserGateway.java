/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.auth.domain.gateway;

import org.laokou.auth.domain.user.User;

/**
 * @author laokou
 */
public interface UserGateway {

	/**
	 * 查询用户
	 * @param username 用户名
	 * @param tenantId 租户ID
	 * @param type 类型（password、mail、mobile）
	 * @return User
	 */
	User getUserByUsername(String username, Long tenantId, String type);

}
