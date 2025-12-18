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
import org.laokou.auth.model.UserV;
import org.laokou.common.context.util.User;

/**
 * @author laokou
 */
public final class UserConvertor {

	private UserConvertor() {
	}

	public static User toUser(AuthA auth) {
		UserE userE = auth.getUserE();
		UserV userV = auth.getUserV();
		return User.builder()
			.id(userE.getId())
			.username(userE.getUsername())
			.password(userE.getPassword())
			.avatar(userV.avatar())
			.superAdmin(userE.isSuperAdministrator())
			.tenantId(userE.getTenantId())
			.deptId(userE.getDeptId())
			.permissions(userV.permissions())
			.status(userE.getStatus())
			.mail(userE.getMail())
			.mobile(userE.getMobile())
			.build();
	}

	public static UserE toEntity(UserDO userDO) {
		return DomainFactory.getUser()
			.toBuilder()
			.id(userDO.getId())
			.username(userDO.getUsername())
			.password(userDO.getPassword())
			.superAdmin(userDO.getSuperAdmin())
			.avatar(userDO.getAvatar())
			.mail(userDO.getMail())
			.status(userDO.getStatus())
			.mobile(userDO.getMobile())
			.tenantId(userDO.getTenantId())
			.build();
	}

	public static UserDO toDataObject(UserV userV) {
		UserDO userDO = new UserDO();
		userDO.setUsername(userV.username());
		userDO.setMail(userV.mail());
		userDO.setMobile(userV.mobile());
		userDO.setTenantId(userV.tenantId());
		return userDO;
	}

}
