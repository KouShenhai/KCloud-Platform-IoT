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
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.user.model.UserE;
import org.laokou.common.crypto.utils.AESUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.security.utils.UserDetail;
import org.laokou.common.sensitive.utils.SensitiveUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

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

	public static UserDO toDataObject(PasswordEncoder passwordEncoder, UserE userE, boolean isInsert) {
		UserDO userDO = new UserDO();
		if (isInsert) {
			userDO.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
			userDO.setUsername(userE.getUsername());
			userDO.setUsernamePhrase(userE.getUsernamePhrase());
		}
		userDO.setId(userE.getId());
		userDO.setSuperAdmin(userE.getSuperAdmin());
		userDO.setStatus(userE.getStatus());
		userDO.setAvatar(userE.getAvatar());
		userDO.setMail(userE.getMail());
		userDO.setMailPhrase(userE.getMailPhrase());
		userDO.setMobile(userE.getMobile());
		userDO.setMobilePhrase(userE.getMobilePhrase());
		return userDO;
	}

	public static UserProfileCO toClientObject(UserDetail userDetail) {
		UserProfileCO userProfileCO = new UserProfileCO();
		userProfileCO.setId(userDetail.getId());
		userProfileCO.setUsername(userDetail.getUsername());
		userProfileCO.setAvatar(userDetail.getAvatar());
		userProfileCO.setPermissions(userDetail.getPermissions());
		return userProfileCO;
	}

	public static UserCO toClientObject(UserDO userDO) {
		String mail = userDO.getMail();
		String mobile = userDO.getMobile();
		UserCO userCO = new UserCO();
		userCO.setId(userDO.getId());
		userCO.setSuperAdmin(userDO.getSuperAdmin());
		userCO.setStatus(userDO.getStatus());
		userCO.setAvatar(userDO.getAvatar());
		userCO.setCreateTime(userDO.getCreateTime());
		userCO.setUsername(AESUtil.decrypt(userDO.getUsername()));
		if (StringUtil.isNotEmpty(mail)) {
			userCO.setMail(AESUtil.decrypt(mail));
		}
		if (StringUtil.isNotEmpty(mobile)) {
			userCO.setMobile(AESUtil.decrypt(mobile));
		}
		return userCO;
	}

	public static List<UserCO> toClientObjects(List<UserDO> userDOList) {
		return userDOList.stream().map(item -> {
			UserCO userCO = toClientObject(item);
			String mail = userCO.getMail();
			String mobile = userCO.getMobile();
			if (StringUtil.isNotEmpty(mail)) {
				userCO.setMail(SensitiveUtil.formatMail(mail));
			}
			if (StringUtil.isNotEmpty(mobile)) {
				userCO.setMobile(SensitiveUtil.formatMobile(mobile));
			}
			return userCO;
		}).toList();
	}

	public static UserE toEntity(UserCO userCO) {
		UserE userE = new UserE();
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

}
