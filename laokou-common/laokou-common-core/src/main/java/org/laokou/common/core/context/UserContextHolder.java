/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Optional;

/**
 * 用户上下文.
 *
 * @author laokou
 */
public class UserContextHolder {

	/**
	 * 用户上下文本地线程变量.
	 */
	private static final ThreadLocal<User> USER_LOCAL = new TransmittableThreadLocal<>();

	/**
	 * 注销本地线程变量.
	 */
	public static void clear() {
		USER_LOCAL.remove();
	}

	/**
	 * 获取本地线程变量.
	 * @return 用户
	 */
	public static User get() {
		return Optional.ofNullable(USER_LOCAL.get()).orElse(new User());
	}

	/**
	 * 往本地线程变量写入.
	 * @param user 用户
	 */
	public static void set(User user) {
		USER_LOCAL.set(user);
	}

	/**
	 * 用户.
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class User {

		@Schema(name = "id", description = "ID")
		private Long id;

		@Schema(name = "username", description = "用户名")
		private String username;

		@Schema(name = "tenantId", description = "租户ID")
		private Long tenantId;

		@Schema(name = "deptPath", description = "部门PATH")
		private String deptPath;

		@Schema(name = "deptId", description = "部门ID")
		private Long deptId;

		@Schema(name = "sourceName", description = "数据源名称")
		private String sourceName;

		/**
		 * 令牌.
		 */
		private String token;

	}

}
