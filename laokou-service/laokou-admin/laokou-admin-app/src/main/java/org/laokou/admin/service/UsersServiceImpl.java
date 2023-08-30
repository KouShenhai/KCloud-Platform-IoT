package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.api.UsersServiceI;
import org.laokou.admin.client.dto.*;
import org.laokou.admin.client.dto.clientobject.OptionCO;
import org.laokou.admin.client.dto.clientobject.UserOnlineCO;
import org.laokou.admin.client.dto.clientobject.UserProfileCO;
import org.laokou.admin.command.UserInsertCmdExe;
import org.laokou.admin.command.UserOnlineKillCmdExe;
import org.laokou.admin.command.UserUpdateCmdExe;
import org.laokou.admin.command.query.UserOnlineListQryExe;
import org.laokou.admin.command.query.UserOptionListQryExe;
import org.laokou.admin.command.query.UserProfileGetQryExe;
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

	@Override
	public Result<Boolean> update(UserUpdateCmd cmd) {
		return userUpdateCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> insert(UserInsertCmd cmd) {
		return userInsertCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> onlineKill(UserOnlineKillCmd cmd) {
		return userOnlineKillCmdExe.execute(cmd);
	}

	@Override
	public Result<Datas<UserOnlineCO>> onlineList(UserOnlineListQry qry) {
		return userOnlineListQryExe.execute(qry);
	}

	@Override
	public Result<UserProfileCO> profile(UserProfileGetQry qry) {
		return userProfileGetQryExe.execute(qry);
	}

	@Override
	public Result<List<OptionCO>> optionList(UserOptionListQry qry) {
		return userOptionListQryExe.execute(qry);
	}

}
