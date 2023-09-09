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

	private final UserOnlineKillCmdExe userOnlineKillCmdExe;

	private final UserOnlineListQryExe userOnlineListQryExe;

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
		return null;
	}

	@Override
	public Result<Boolean> insert(UserInsertCmd cmd) {
		return null;
	}

	@Override
	public Result<Boolean> onlineKill(UserOnlineKillCmd cmd) {
		return null;
	}

	@Override
	public Result<Datas<UserOnlineCO>> onlineList(UserOnlineListQry qry) {
		return null;
	}

	@Override
	public Result<UserProfileCO> profile(UserProfileGetQry qry) {
		return userProfileGetQryExe.execute(qry);
	}

	@Override
	public Result<Boolean> profile(UserProfileUpdateCmd cmd) {
		return null;
	}

	@Override
	public Result<List<OptionCO>> optionList(UserOptionListQry qry) {
		return null;
	}

	@Override
	public Result<Boolean> status(UserStatusUpdateCmd cmd) {
		return null;
	}

	@Override
	public Result<Boolean> resetPassword(UserPasswordResetCmd cmd) {
		return null;
	}

	@Override
	public Result<UserCO> get(UserGetQry qry) {
		return null;
	}

	@Override
	public Result<Boolean> delete(UserDeleteCmd cmd) {
		return null;
	}

	@Override
	public Result<Datas<UserCO>> list(UserListQry qry) {
		return null;
	}

}
