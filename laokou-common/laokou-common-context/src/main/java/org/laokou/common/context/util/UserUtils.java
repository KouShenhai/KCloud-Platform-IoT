/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author laokou
 */
public final class UserUtils {

	private UserUtils() {
	}

	public static UserExtDetails userDetail() {
		return Optional.ofNullable(getAuthentication()).map(authentication -> {
			if (authentication.getPrincipal() instanceof UserExtDetails userExtDetails) {
				return userExtDetails;
			}
			return DomainFactory.getUserDetails();
		}).orElse(DomainFactory.getUserDetails());
	}

	/**
	 * 用户ID.
	 * @return Long
	 */
	public static Long getUserId() {
		return userDetail().getId();
	}

	/**
	 * 用户名.
	 * @return String
	 */
	public static String getUserName() {
		return userDetail().getUsername();
	}

	/**
	 * 租户ID.
	 * @return Long
	 */
	public static Long getTenantId() {
		return userDetail().getTenantId();
	}

	/**
	 * 是否是超级管理员.
	 * @return Boolean
	 */
	public static Boolean isSuperAdmin() {
		return userDetail().getSuperAdmin();
	}

	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

}
