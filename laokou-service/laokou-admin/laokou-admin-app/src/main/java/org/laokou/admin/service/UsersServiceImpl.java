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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.UsersServiceI;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.user.*;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.dto.user.clientobject.UserOnlineCO;
import org.laokou.admin.dto.user.clientobject.UserProfileCO;
import org.laokou.admin.command.user.*;
import org.laokou.admin.command.user.query.*;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersServiceI {

	private final UserUpdateCmdExe userUpdateCmdExe;

	private final UserInsertCmdExe userInsertCmdExe;

	private final OnlineUserKillCmdExe onlineUserKillCmdExe;

	private final OnlineUserListQryExe onlineUserListQryExe;

	private final UserProfileGetQryExe userProfileGetQryExe;

	private final UserOptionListQryExe userOptionListQryExe;

	private final UserProfileUpdateCmdExe userProfileUpdateCmdExe;

	private final UserStatusUpdateCmdExe userStatusUpdateCmdExe;

	private final UserPasswordResetCmdExe userPasswordResetCmdExe;

	private final UserGetQryExe userGetQryExe;

	private final UserDeleteCmdExe userDeleteCmdExe;

	private final UserListQryExe userListQryExe;

	@Override
	public Result<Boolean> update(UserUpdateCmd cmd) {
		return userUpdateCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> insert(UserInsertCmd cmd) {
		return userInsertCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> onlineKill(OnlineUserKillCmd cmd) {
		return onlineUserKillCmdExe.execute(cmd);
	}

	@Override
	public Result<Datas<UserOnlineCO>> onlineList(OnlineUserListQry qry) {
		return onlineUserListQryExe.execute(qry);
	}

	@Override
	public Result<UserProfileCO> getProfile(UserProfileGetQry qry) {
		return userProfileGetQryExe.execute(qry);
	}

	@Override
	public Result<Boolean> updateProfile(UserProfileUpdateCmd cmd) {
		return userProfileUpdateCmdExe.execute(cmd);
	}

	@Override
	public Result<List<OptionCO>> optionList(UserOptionListQry qry) {
		return userOptionListQryExe.execute(qry);
	}

	@Override
	public Result<Boolean> updateStatus(UserStatusUpdateCmd cmd) {
		return userStatusUpdateCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> resetPassword(UserPasswordResetCmd cmd) {
		return userPasswordResetCmdExe.execute(cmd);
	}

	@Override
	public Result<UserCO> getById(UserGetQry qry) {
		return userGetQryExe.execute(qry);
	}

	@Override
	public Result<Boolean> deleteById(UserDeleteCmd cmd) {
		return userDeleteCmdExe.execute(cmd);
	}

	@Override
	public Result<Datas<UserCO>> list(UserListQry qry) {
		return userListQryExe.execute(qry);
	}

}
