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

package org.laokou.admin.user.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.user.api.UsersServiceI;
import org.laokou.admin.user.command.*;
import org.laokou.admin.user.command.query.UserGetQryExe;
import org.laokou.admin.user.command.query.UserPageQryExe;
import org.laokou.admin.user.command.query.UserProfileGetQryExe;
import org.laokou.admin.user.dto.*;
import org.laokou.admin.user.dto.clientobject.UserCO;
import org.laokou.admin.user.dto.clientobject.UserProfileCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersServiceI {

	private final UserSaveCmdExe userSaveCmdExe;

	private final UserModifyCmdExe userModifyCmdExe;

	private final UserRemoveCmdExe userRemoveCmdExe;

	private final UserImportCmdExe userImportCmdExe;

	private final UserExportCmdExe userExportCmdExe;

	private final UserPageQryExe userPageQryExe;

	private final UserGetQryExe userGetQryExe;

	private final UserProfileGetQryExe userProfileGetQryExe;

	@Override
	public void save(UserSaveCmd cmd) {
		userSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(UserModifyCmd cmd) {
		userModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(UserRemoveCmd cmd) {
		userRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(UserImportCmd cmd) {
		userImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(UserExportCmd cmd) {
		userExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<UserCO>> page(UserPageQry qry) {
		return userPageQryExe.execute(qry);
	}

	@Override
	public Result<UserCO> getById(UserGetQry qry) {
		return userGetQryExe.execute(qry);
	}

	@Override
	public Result<UserProfileCO> getProfile() {
		return userProfileGetQryExe.execute();
	}

}
