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

package org.laokou.admin.user.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.oss.dto.clientobject.OssUploadCO;
import org.laokou.admin.user.api.UsersServiceI;
import org.laokou.admin.user.command.UserExportCmdExe;
import org.laokou.admin.user.command.UserImportCmdExe;
import org.laokou.admin.user.command.UserModifyAuthorityCmdExe;
import org.laokou.admin.user.command.UserModifyCmdExe;
import org.laokou.admin.user.command.UserRemoveCmdExe;
import org.laokou.admin.user.command.UserResetPwdCmdExe;
import org.laokou.admin.user.command.UserSaveCmdExe;
import org.laokou.admin.user.command.UserUploadCmdExe;
import org.laokou.admin.user.command.query.UserGetQryExe;
import org.laokou.admin.user.command.query.UserPageQryExe;
import org.laokou.admin.user.command.query.UserProfileGetQryExe;
import org.laokou.admin.user.dto.UserExportCmd;
import org.laokou.admin.user.dto.UserGetQry;
import org.laokou.admin.user.dto.UserImportCmd;
import org.laokou.admin.user.dto.UserModifyAuthorityCmd;
import org.laokou.admin.user.dto.UserModifyCmd;
import org.laokou.admin.user.dto.UserPageQry;
import org.laokou.admin.user.dto.UserRemoveCmd;
import org.laokou.admin.user.dto.UserResetPwdCmd;
import org.laokou.admin.user.dto.UserSaveCmd;
import org.laokou.admin.user.dto.UserUploadAvatarCmd;
import org.laokou.admin.user.dto.clientobject.UserCO;
import org.laokou.admin.user.dto.clientobject.UserProfileCO;
import org.laokou.common.fory.config.ForyFactory;
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

	private final UserResetPwdCmdExe userResetPwdCmdExe;

	private final UserModifyAuthorityCmdExe userModifyAuthorityCmdExe;

	private final UserPageQryExe userPageQryExe;

	private final UserGetQryExe userGetQryExe;

	private final UserProfileGetQryExe userProfileGetQryExe;

	private final UserUploadCmdExe userUploadCmdEx;

	static {
		ForyFactory.INSTANCE.register(org.laokou.common.i18n.dto.Result.class);
		ForyFactory.INSTANCE.register(org.laokou.admin.user.dto.clientobject.UserCO.class);
	}

	@Override
	public void saveUser(UserSaveCmd cmd) throws Exception {
		userSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyUser(UserModifyCmd cmd) throws Exception {
		userModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeUser(UserRemoveCmd cmd) throws InterruptedException {
		userRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importUser(UserImportCmd cmd) {
		userImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportUser(UserExportCmd cmd) {
		userExportCmdExe.executeVoid(cmd);
	}

	@Override
	public void resetUserPwd(UserResetPwdCmd cmd) throws Exception {
		userResetPwdCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyUserAuthority(UserModifyAuthorityCmd cmd) throws Exception {
		userModifyAuthorityCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<UserCO>> pageUser(UserPageQry qry) {
		return userPageQryExe.execute(qry);
	}

	@Override
	public Result<UserCO> getUserById(UserGetQry qry) throws Exception {
		return userGetQryExe.execute(qry);
	}

	@Override
	public Result<UserProfileCO> getUserProfile() {
		return userProfileGetQryExe.execute();
	}

	@Override
	public Result<OssUploadCO> uploadUserAvatar(UserUploadAvatarCmd cmd) throws Exception {
		return userUploadCmdEx.execute(cmd);
	}

}
