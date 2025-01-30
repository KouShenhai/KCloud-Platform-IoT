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
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.crypto.utils.AESUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.security.utils.UserDetail;

import java.util.List;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;

/**
 * 用户转换器.
 *
 * @author laokou
 */
public class UserConvertor {

	public static UserDO toDataObject(UserE userE, boolean isInsert) {
		UserDO userDO = new UserDO();
		if (isInsert) {
			userDO.setId(IdGenerator.defaultSnowflakeId());
		}
		else {
			userDO.setId(userE.getId());
		}
		userDO.setPassword(userE.getPassword());
		userDO.setSuperAdmin(userE.getSuperAdmin());
		userDO.setMail(userE.getMail());
		userDO.setMobile(userE.getMobile());
		userDO.setStatus(userE.getStatus());
		userDO.setAvatar(userE.getAvatar());
		userDO.setUsernamePhrase(userE.getUsernamePhrase());
		userDO.setMailPhrase(userE.getMailPhrase());
		userDO.setMobilePhrase(userE.getMobilePhrase());
		userDO.setUsername(userE.getUsername());
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
		String username = userDO.getUsername();
		String mail = userDO.getMail();
		String mobile = userDO.getMobile();
		UserCO userCO = new UserCO();
		userCO.setId(userDO.getId());
		userCO.setSuperAdmin(userDO.getSuperAdmin());
		userCO.setStatus(userDO.getStatus());
		userCO.setAvatar(userDO.getAvatar());
		if (StringUtil.isNotEmpty(username)) {
			userCO.setUsername(AESUtil.decrypt(username));
		}
		if (StringUtil.isNotEmpty(mail)) {
			userCO.setMail(AESUtil.decrypt(mail));
		}
		if (StringUtil.isNotEmpty(mobile)) {
			userCO.setMobile(AESUtil.decrypt(mobile));
		}
		return userCO;
	}

	public static List<UserCO> toClientObjects(List<UserDO> userDOList) {
		return userDOList.stream().map(UserConvertor::toClientObject).toList();
	}

	public static UserE toEntity(UserCO userCO, boolean isInsert) {
		UserE userE = new UserE();
		String username = userCO.getUsername();
		String mail = userCO.getMail();
		String mobile = userCO.getMobile();
		userE.setId(userCO.getId());
		userE.setUsername(username);
		userE.setPassword(userCO.getPassword());
		userE.setSuperAdmin(userCO.getSuperAdmin());
		userE.setMail(mail);
		userE.setMobile(mobile);
		userE.setStatus(userCO.getStatus());
		userE.setAvatar(userCO.getAvatar());
		if (StringUtil.isNotEmpty(username) && isInsert) {
			userE.setUsername(AESUtil.decrypt(username));
			userE.setUsernamePhrase(EMPTY);
		}
		if (StringUtil.isNotEmpty(mail)) {
			userE.setMailPhrase(EMPTY);
		}
		if (StringUtil.isNotEmpty(mobile)) {
			userE.setMobilePhrase(EMPTY);
		}
		return userE;
	}

}
