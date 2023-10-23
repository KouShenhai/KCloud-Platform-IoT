/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.user.query;

import org.laokou.admin.dto.user.UserProfileGetQry;
import org.laokou.admin.dto.user.clientobject.UserProfileCO;
import org.laokou.auth.domain.user.User;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
public class UserProfileGetQryExe {

	public Result<UserProfileCO> execute(UserProfileGetQry qry) {
		return Result.of(convert());
	}

	private UserProfileCO convert() {
		User user = UserUtil.user();
		UserProfileCO co = new UserProfileCO();
		co.setId(user.getId());
		co.setAvatar(user.getAvatar());
		co.setUsername(user.getUsername());
		co.setMobile(user.getMobile());
		co.setMail(user.getMail());
		co.setPermissionList(user.getPermissionList());
		co.setTenantId(user.getTenantId());
		co.setSuperAdmin(user.getSuperAdmin());
		return co;
	}

}
