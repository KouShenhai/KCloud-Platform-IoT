/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import org.laokou.admin.dto.user.clientobject.UserProfileCO;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserDetail;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

/**
 * 查看用户信息执行器.
 *
 * @author laokou
 */
@Component
public class UserProfileGetQryExe {

	/**
	 * 执行查看用户信息.
	 * @return 用户信息
	 */
	public Result<UserProfileCO> execute() {
		return Result.ok(convert());
	}

	private UserProfileCO convert() {
		UserDetail userDetail = UserUtil.user();
		return new UserProfileCO(userDetail.getDeptId(), userDetail.getAvatar(), userDetail.getUsername(),
			userDetail.getPermissions(), userDetail.getTenantId()
		);
	}

}
