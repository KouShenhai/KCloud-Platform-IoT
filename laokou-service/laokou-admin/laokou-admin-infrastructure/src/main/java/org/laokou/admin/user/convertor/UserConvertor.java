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

package org.laokou.admin.user.convertor;

import org.laokou.admin.user.dto.clientobject.UserCO;
import org.laokou.admin.user.dto.clientobject.UserProfileCO;
import org.laokou.admin.user.factory.UserDomainFactory;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserDeptDO;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserRoleDO;
import org.laokou.admin.user.model.UserE;
import org.laokou.admin.user.model.UserOperateTypeEnum;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.mybatisplus.util.UserDetails;
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

	public static List<UserRoleDO> toDataObjects(UserE userE) {
		List<String> roleIds = userE.getRoleIds();
		Long userId = userE.getId();
		int size = roleIds.size();
		List<Long> primaryKeys = userE.getPrimaryKeys(size);
		List<UserRoleDO> list = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			UserRoleDO userRoleDO = new UserRoleDO();
			userRoleDO.setId(primaryKeys.get(i));
			userRoleDO.setRoleId(Long.valueOf(roleIds.get(i)));
			userRoleDO.setUserId(userId);
			list.add(userRoleDO);
		}
		return list;
	}

	public static List<UserDeptDO> toDataObjs(UserE userE) {
		List<String> deptIds = userE.getDeptIds();
		Long userId = userE.getId();
		int size = deptIds.size();
		List<Long> primaryKeys = userE.getPrimaryKeys(size);
		List<UserDeptDO> list = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			UserDeptDO userDeptDO = new UserDeptDO();
			userDeptDO.setId(primaryKeys.get(i));
			userDeptDO.setDeptId(Long.valueOf(deptIds.get(i)));
			userDeptDO.setUserId(userId);
			list.add(userDeptDO);
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

	public static List<UserDeptDO> toDataObjs(List<Long> userDeptIds) {
		return userDeptIds.stream().map(id -> {
			UserDeptDO userDeptDO = new UserDeptDO();
			userDeptDO.setId(id);
			return userDeptDO;
		}).toList();
	}

	public static UserDO toDataObject(UserE userE) {
		UserDO userDO = new UserDO();
		switch (userE.getUserOperateTypeEnum()) {
			case SAVE -> {
				userDO.setId(userE.getPrimaryKey());
				userDO.setPassword(userE.getDefaultEncodedPassword());
				userDO.setUsername(userE.getUsername());
				userDO.setUsernamePhrase(userE.getUsernamePhrase());
			}
			case MODIFY, MODIFY_AUTHORITY -> userDO.setId(userE.getId());
			case RESET_PWD -> {
				userDO.setId(userE.getId());
				String password = userE.getPassword();
				if (StringUtils.isNotEmpty(password)) {
					userDO.setPassword(userE.getEncodedPassword());
				}
			}
		}
		String mobile = userE.getMobile();
		String mail = userE.getMail();
		userDO.setSuperAdmin(userE.getSuperAdmin());
		userDO.setStatus(userE.getStatus());
		userDO.setAvatar(userE.getAvatar());
		userDO.setMail(StringUtils.isEmpty(mail) ? null : mail);
		userDO.setMailPhrase(userE.getMailPhrase());
		userDO.setMobile(StringUtils.isEmpty(mobile) ? null : mobile);
		userDO.setMobilePhrase(userE.getMobilePhrase());
		return userDO;
	}

	public static UserProfileCO toClientObject(UserDetails userDetails) {
		UserProfileCO userProfileCO = new UserProfileCO();
		userProfileCO.setId(userDetails.getId());
		userProfileCO.setUsername(userDetails.getUsername());
		userProfileCO.setAvatar(userDetails.getAvatar());
		userProfileCO.setPermissions(userDetails.getPermissions());
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
		if (StringUtils.isNotEmpty(mail)) {
			userCO.setMail(AESUtils.decrypt(mail));
		}
		if (StringUtils.isNotEmpty(mobile)) {
			userCO.setMobile(AESUtils.decrypt(mobile));
		}
		return userCO;
	}

	public static List<UserCO> toClientObjects(List<UserDO> userDOList) {
		return userDOList.stream().map(item -> {
			try {
				UserCO userCO = toClientObject(item);
				String mail = userCO.getMail();
				String mobile = userCO.getMobile();
				if (StringUtils.isNotEmpty(mail)) {
					userCO.setMail(SensitiveUtils.formatMail(mail));
				}
				if (StringUtils.isNotEmpty(mobile)) {
					userCO.setMobile(SensitiveUtils.formatMobile(mobile));
				}
				return userCO;
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).toList();
	}

	public static UserE toEntity(UserCO userCO, boolean isInsert) {
		UserE user = UserDomainFactory.getUser();
		user.setId(userCO.getId());
		user.setUsername(userCO.getUsername());
		user.setSuperAdmin(userCO.getSuperAdmin());
		user.setMail(userCO.getMail());
		user.setMobile(userCO.getMobile());
		user.setStatus(userCO.getStatus());
		user.setAvatar(userCO.getAvatar());
		user.setUserOperateTypeEnum(isInsert ? UserOperateTypeEnum.SAVE : UserOperateTypeEnum.MODIFY);
		return user;
	}

	public static UserE toEntity(UserCO userCO) {
		UserE userE = UserDomainFactory.getUser();
		userE.setId(userCO.getId());
		userE.setUserIds(Collections.singletonList(userCO.getId()));
		userE.setRoleIds(userCO.getRoleIds());
		userE.setDeptIds(userCO.getDeptIds());
		userE.setUserOperateTypeEnum(UserOperateTypeEnum.MODIFY_AUTHORITY);
		return userE;
	}

	public static UserE toEntity(Long id, String password) {
		UserE userE = UserDomainFactory.getUser();
		userE.setId(id);
		userE.setPassword(password);
		userE.setUserOperateTypeEnum(UserOperateTypeEnum.RESET_PWD);
		return userE;
	}

}
