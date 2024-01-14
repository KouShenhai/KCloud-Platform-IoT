/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
package org.laokou.common.core.holder;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * @author laokou
 */
public class UserContextHolder {

	private static final ThreadLocal<User> USER_LOCAL = new TransmittableThreadLocal<>();

	public static void clear() {
		USER_LOCAL.remove();
	}

	public static User get() {
		return Optional.ofNullable(USER_LOCAL.get()).orElse(new User());
	}

	public static void set(User user) {
		clear();
		USER_LOCAL.set(user);
	}

	@Data
	@NoArgsConstructor
	public static class User {

		private Long id;

		private Long tenantId;

		private String deptPath;

		private Long deptId;

		private String sourceName;

		public User(Long tenantId, String sourceName) {
			this.tenantId = tenantId;
			this.sourceName = sourceName;
		}

	}

}
