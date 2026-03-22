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

package org.laokou.admin.user.convertor;

import org.laokou.admin.user.dto.clientobject.UserCO;
import org.laokou.admin.user.dto.clientobject.UserProfileCO;
import org.laokou.admin.user.factory.UserDomainFactory;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserRoleDO;
import org.laokou.admin.user.model.UserA;
import org.laokou.admin.user.model.entity.UserE;
import org.laokou.common.context.util.OAuth2AuthenticatedExtPrincipal;
import org.laokou.common.context.util.UserUtils;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.sensitive.util.SensitiveUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户转换器.
 *
 * @author laokou
 */
public final class UserConvertor {

	private UserConvertor() {
	}

	public static List<UserRoleDO> toDataObjects(UserA userA) {
		List<String> roleIds = userA.getUserE().getRoleIds();
		Long userId = userA.getId();
		int num = roleIds.size();
		List<Long> userRoleIds = userA.createBatchUserRoleIds(num);
		List<UserRoleDO> list = new ArrayList<>(num);
		for (int i = 0; i < num; i++) {
			UserRoleDO userRoleDO = new UserRoleDO();
			userRoleDO.setId(userRoleIds.get(i));
			userRoleDO.setRoleId(Long.valueOf(roleIds.get(i)));
			userRoleDO.setUserId(userId);
			list.add(userRoleDO);
		}
		return list;
	}

	public static List<UserRoleDO> toDataObjects(List<Long> userRoleIds) {
		return userRoleIds.stream().map(id -> {
			UserRoleDO userRoleDO = new UserRoleDO();
			userRoleDO.setId(id);
			return userRoleDO;
		}).toList();
	}

	public static UserDO toDataObject(UserA userA) {
		UserDO userDO = new UserDO();
		UserE userE = userA.getUserE();
		userDO.setId(userA.getId());
		userDO.setUsername(userE.getUsername());
		userDO.setUsernamePhrase(userE.getUsernamePhrase());
		userDO.setPassword(userE.getPassword());
		userDO.setSuperAdmin(userE.getSuperAdmin());
		userDO.setStatus(userE.getStatus());
		userDO.setAvatar(userE.getAvatar());
		userDO.setMail(userE.getMail());
		userDO.setMailPhrase(userE.getMailPhrase());
		userDO.setMobile(userE.getMobile());
		userDO.setDeptId(userE.getDeptId());
		userDO.setMobilePhrase(userE.getMobilePhrase());
		userDO.setCreateTime(userA.getCreateTime());
		return userDO;
	}

	public static UserProfileCO toClientObject() {
		OAuth2AuthenticatedExtPrincipal principal = UserUtils.principal();
		UserProfileCO userProfileCO = new UserProfileCO();
		userProfileCO.setId(principal.getId());
		userProfileCO.setUsername(principal.getUsername());
		userProfileCO.setAvatar(principal.getAvatar());
		userProfileCO.setPermissions(principal.getPermissions());
		userProfileCO.setScopes(principal.getScopes());
		return userProfileCO;
	}

	public static UserCO toClientObject(UserDO userDO) throws Exception {
		String mail = userDO.getMail();
		String mobile = userDO.getMobile();
		UserCO userCO = new UserCO();
		userCO.setId(userDO.getId());
		userCO.setSuperAdmin(userDO.getSuperAdmin());
		userCO.setStatus(userDO.getStatus());
		userCO.setAvatar(userDO.getAvatar());
		userCO.setCreateTime(userDO.getCreateTime());
		userCO.setUsername(AESUtils.decrypt(userDO.getUsername()));
		userCO.setMail(AESUtils.decrypt(mail));
		userCO.setMobile(AESUtils.decrypt(mobile));
		userCO.setDeptId(userDO.getDeptId());
		return userCO;
	}

	public static List<UserCO> toClientObjects(List<UserDO> userDOList) {
		return userDOList.stream().map(item -> {
			try {
				UserCO userCO = toClientObject(item);
				String mail = userCO.getMail();
				String mobile = userCO.getMobile();
				userCO.setMail(SensitiveUtils.formatMail(mail));
				userCO.setMobile(SensitiveUtils.formatMobile(mobile));
				return userCO;
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).toList();
	}

	public static UserE toEntity(UserCO userCO) {
		return UserDomainFactory.createUserE()
			.toBuilder()
			.id(userCO.getId())
			.username(userCO.getUsername())
			.superAdmin(userCO.getSuperAdmin())
			.mail(userCO.getMail())
			.mobile(userCO.getMobile())
			.status(userCO.getStatus())
			.avatar(userCO.getAvatar())
			.userIds(Collections.singletonList(userCO.getId()))
			.roleIds(userCO.getRoleIds())
			.deptId(userCO.getDeptId())
			.build();
	}

	public static UserE toEntity(Long id, String password) {
		return UserDomainFactory.createUserE().toBuilder().id(id).password(password).build();
	}

}
