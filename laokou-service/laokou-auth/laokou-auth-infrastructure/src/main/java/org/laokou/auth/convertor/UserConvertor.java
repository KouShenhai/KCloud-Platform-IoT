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

package org.laokou.auth.convertor;

import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gatewayimpl.database.dataobject.UserDO;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.entity.UserE;
import org.laokou.auth.model.valueobject.DataFilterV;
import org.laokou.auth.model.valueobject.UserV;
import org.laokou.common.context.util.UserExtDetails;
import org.laokou.common.crypto.util.AESUtils;

/**
 * @author laokou
 */
@Slf4j
public final class UserConvertor {

	private UserConvertor() {
	}

	public static UserExtDetails toUserDetails(AuthA auth) {
		try {
			UserE userE = auth.getUserE();
			UserV userV = auth.getUserV();
			DataFilterV dataFilterV = auth.getDataFilterV();
			String username = AESUtils.decrypt(userE.getUsername());
			String mail = AESUtils.decrypt(userE.getMail());
			String mobile = AESUtils.decrypt(userE.getMobile());
			return new UserExtDetails(userE.getId(), username, userE.getPassword(), userV.avatar(), userE.isSuperAdministrator(),
				userE.getStatus(), mail, mobile, userE.getTenantId(), userE.getDeptId(), userV.permissions(), dataFilterV.deptIds(), dataFilterV.creator());
		} catch (Exception ex) {
			log.error("解密失败：{}", ex.getMessage());
			throw new IllegalArgumentException(ex);
		}
	}

	public static UserE toEntity(UserDO userDO) {
		return DomainFactory.createUser()
			.toBuilder()
			.id(userDO.getId())
			.username(userDO.getUsername())
			.password(userDO.getPassword())
			.superAdmin(userDO.getSuperAdmin())
			.avatar(userDO.getAvatar())
			.mail(userDO.getMail())
			.status(userDO.getStatus())
			.deptId(userDO.getDeptId())
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

	public static UserDO toDataObject(UserE userE) {
		UserDO userDO = new UserDO();
		userDO.setId(userE.getId());
		userDO.setDeptId(userE.getDeptId());
		userDO.setTenantId(userE.getTenantId());
		return userDO;
	}

}
