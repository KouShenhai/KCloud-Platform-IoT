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
import org.laokou.common.core.util.IdGenerator;
import org.laokou.common.crypto.utils.AESUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.security.util.UserDetails;
import org.laokou.common.sensitive.utils.SensitiveUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.List;

/**
 * 用户转换器.
 *
 * @author laokou
 */
public final class UserConvertor {

	private static final String DEFAULT_PASSWORD = "laokou123";

	private UserConvertor() {
	}

	public static List<UserRoleDO> toDataObjects(UserE userE, Long userId) {
		return userE.getRoleIds().stream().map(roleId -> {
			UserRoleDO userRoleDO = new UserRoleDO();
			userRoleDO.setId(IdGenerator.defaultSnowflakeId());
			userRoleDO.setRoleId(Long.valueOf(roleId));
			userRoleDO.setUserId(userId);
			return userRoleDO;
		}).toList();
	}

	public static List<UserDeptDO> toDataObjs(UserE userE, Long userId) {
		return userE.getDeptIds().stream().map(deptId -> {
			UserDeptDO userDeptDO = new UserDeptDO();
			userDeptDO.setId(IdGenerator.defaultSnowflakeId());
			userDeptDO.setDeptId(Long.valueOf(deptId));
			userDeptDO.setUserId(userId);
			return userDeptDO;
		}).toList();
	}

	public static List<UserRoleDO> toDataObjects(UserE userE) {
		return userE.getUserRoleIds().stream().map(id -> {
			UserRoleDO userRoleDO = new UserRoleDO();
			userRoleDO.setId(id);
			return userRoleDO;
		}).toList();
	}

	public static List<UserDeptDO> toDataObjs(UserE userE) {
		return userE.getUserDeptIds().stream().map(id -> {
			UserDeptDO userDeptDO = new UserDeptDO();
			userDeptDO.setId(id);
			return userDeptDO;
		}).toList();
	}

	public static UserDO toDataObject(PasswordEncoder passwordEncoder, UserE userE, boolean isInsert) {
		UserDO userDO = new UserDO();
		if (isInsert) {
			userDO.setId(IdGenerator.defaultSnowflakeId());
			userDO.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
			userDO.setUsername(userE.getUsername());
			userDO.setUsernamePhrase(userE.getUsernamePhrase());
		}
		else {
			userDO.setId(userE.getId());
			String password = userE.getPassword();
			if (StringUtils.isNotEmpty(password)) {
				userDO.setPassword(passwordEncoder.encode(password));
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
					userCO.setMail(SensitiveUtil.formatMail(mail));
				}
				if (StringUtils.isNotEmpty(mobile)) {
					userCO.setMobile(SensitiveUtil.formatMobile(mobile));
				}
				return userCO;
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).toList();
	}

	public static UserE toEntity(UserCO userCO) {
		UserE userE = UserDomainFactory.getUser();
		userE.setId(userCO.getId());
		userE.setUsername(userCO.getUsername());
		userE.setSuperAdmin(userCO.getSuperAdmin());
		userE.setPassword(userCO.getPassword());
		userE.setMail(userCO.getMail());
		userE.setMobile(userCO.getMobile());
		userE.setStatus(userCO.getStatus());
		userE.setAvatar(userCO.getAvatar());
		return userE;
	}

	public static UserE toEntity(UserCO userCO, Long id) {
		UserE userE = UserDomainFactory.getUser();
		userE.setId(id);
		userE.setUserIds(Collections.singletonList(id));
		userE.setRoleIds(userCO.getRoleIds());
		userE.setDeptIds(userCO.getDeptIds());
		return userE;
	}

	public static UserE toEntity(Long id, String password) {
		UserE userE = UserDomainFactory.getUser();
		userE.setId(id);
		userE.setPassword(password);
		return userE;
	}

}
