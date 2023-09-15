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
package org.laokou.common.core.context;

import lombok.Data;

import java.util.Optional;

/**
 * @author laokou
 */
public class UserContextHolder {

	@Data
	public static class User {

		private Long id;

		private Long tenantId;

		private String deptPath;

		private Long deptId;

	}

	private static final ThreadLocal<User> USER_CONTEXT_HOLDER = new InheritableThreadLocal<>();

	public static void set(User user) {
		USER_CONTEXT_HOLDER.remove();
		USER_CONTEXT_HOLDER.set(user);
	}

	public static User get() {
		return Optional.ofNullable(USER_CONTEXT_HOLDER.get()).orElse(new User());
	}

}
