package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.api.UsersServiceI;
import org.laokou.admin.client.dto.UserInsertCmd;
import org.laokou.admin.client.dto.UserOnlineKillCmd;
import org.laokou.admin.client.dto.UserOnlineListQry;
import org.laokou.admin.client.dto.UserUpdateCmd;
import org.laokou.admin.client.dto.clientobject.UserOnlineCO;
import org.laokou.admin.client.dto.clientobject.UserProfileCO;
import org.laokou.admin.command.UserInsertCmdExe;
import org.laokou.admin.command.UserOnlineKillCmdExe;
import org.laokou.admin.command.UserUpdateCmdExe;
import org.laokou.admin.command.query.UserOnlineListQryExe;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Service;

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
	public Result<UserProfileCO> profile() {
		return Result.of(ConvertUtil.sourceToTarget(UserUtil.user(), UserProfileCO.class));
	}

}
