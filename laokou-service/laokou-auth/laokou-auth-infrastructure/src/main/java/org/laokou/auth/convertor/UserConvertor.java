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

package org.laokou.auth.convertor;

import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gatewayimpl.database.dataobject.UserDO;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.UserE;
import org.laokou.common.context.util.User;

/**
 * @author laokou
 */
public final class UserConvertor {

	private UserConvertor() {
	}

	public static User toUser(AuthA auth) {
		UserE userE = auth.getUser();
		return User.builder()
			.id(userE.getId())
			.username(userE.getUsername())
			.password(userE.getPassword())
			.avatar(auth.getAvatar())
			.superAdmin(userE.isSuperAdministrator())
			.tenantId(userE.getTenantId())
			.permissions(auth.getPermissions())
			.status(userE.getStatus())
			.mail(userE.getMail())
			.mobile(userE.getMobile())
			.build();
	}

	public static UserE toEntity(UserDO userDO) {
		UserE userE = DomainFactory.getUser();
		userE.setId(userDO.getId());
		userE.setUsername(userDO.getUsername());
		userE.setPassword(userDO.getPassword());
		userE.setSuperAdmin(userDO.getSuperAdmin());
		userE.setAvatar(userDO.getAvatar());
		userE.setMail(userDO.getMail());
		userE.setStatus(userDO.getStatus());
		userE.setMobile(userDO.getMobile());
		userE.setTenantId(userDO.getTenantId());
		return userE;
	}

	public static UserDO toDataObject(UserE userE) {
		UserDO userDO = new UserDO();
		userDO.setId(userE.getId());
		userDO.setTenantId(userE.getTenantId());
		userDO.setUsername(userE.getUsername());
		userDO.setPassword(userE.getPassword());
		userDO.setSuperAdmin(userE.getSuperAdmin());
		userDO.setAvatar(userE.getAvatar());
		userDO.setMail(userE.getMail());
		userDO.setStatus(userE.getStatus());
		userDO.setMobile(userE.getMobile());
		userDO.setTenantId(userE.getTenantId());
		return userDO;
	}

}
