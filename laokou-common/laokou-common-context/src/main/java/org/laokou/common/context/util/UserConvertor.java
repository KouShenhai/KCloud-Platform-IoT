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

import java.util.Set;

/**
 * @author laokou
 */
public final class UserConvertor {

	private UserConvertor() {
	}

	public static OAuth2AuthenticatedExtPrincipal toPrincipal(OAuth2Authentication authentication, Set<String> scopes) {
		return DomainFactory.createPrincipal()
			.toBuilder()
			.id(authentication.id())
			.username(authentication.username())
			.avatar(authentication.avatar())
			.superAdmin(authentication.superAdmin())
			.tenantId(authentication.tenantId())
			.permissions(authentication.permissions())
			.scopes(scopes)
			.status(authentication.status())
			.mail(authentication.mail())
			.mobile(authentication.mobile())
			.deptId(authentication.deptId())
			.deptIds(authentication.deptIds())
			.creator(authentication.creator())
			.build();
	}

	public static OAuth2AuthenticatedExtPrincipal toPrincipal(UserExtDetails userExtDetails, Set<String> scopes) {
		return DomainFactory.createPrincipal()
			.toBuilder()
			.id(userExtDetails.id())
			.username(userExtDetails.username())
			.avatar(userExtDetails.avatar())
			.superAdmin(userExtDetails.superAdmin())
			.tenantId(userExtDetails.tenantId())
			.permissions(userExtDetails.permissions())
			.scopes(scopes)
			.status(userExtDetails.status())
			.mail(userExtDetails.mail())
			.mobile(userExtDetails.mobile())
			.deptId(userExtDetails.deptId())
			.deptIds(userExtDetails.deptIds())
			.creator(userExtDetails.creator())
			.build();
	}

}
