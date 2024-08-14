/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.command.user.*;
import org.laokou.admin.command.user.query.UserGetQryExe;
import org.laokou.admin.command.user.query.UserListQryExe;
import org.laokou.admin.command.user.query.UserOptionListQryExe;
import org.laokou.admin.command.user.query.UserProfileGetQryExe;
import org.laokou.common.i18n.dto.Option;
import org.laokou.admin.dto.user.*;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.dto.user.clientobject.UserProfileCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户管理.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersServiceI {

	private final UserModifyCmdExe userModifyCmdExe;

	private final UserCreateCmdExe userCreateCmdExe;

	private final UserProfileGetQryExe userProfileGetQryExe;

	private final UserOptionListQryExe userOptionListQryExe;

	private final UserProfileModifyCmdExe userProfileModifyCmdExe;

	private final UserStatusModifyCmdExe userStatusModifyCmdExe;

	private final UserPasswordResetCmdExe userPasswordResetCmdExe;

	private final UserGetQryExe userGetQryExe;

	private final UserRemoveCmdExe userRemoveCmdExe;

	private final UserListQryExe userListQryExe;

	/**
	 * 修改用户.
	 * @param cmd 修改用户参数
	 */
	@Override
	public void modify(UserModifyCmd cmd) {
		userModifyCmdExe.executeVoid(cmd);
	}

	/**
	 * 新增用户.
	 * @param cmd 新增用户参数
	 */
	@Override
	public void create(UserCreateCmd cmd) {
		userCreateCmdExe.executeVoid(cmd);
	}

	/**
	 * 查看用户信息.
	 * @return 用户信息
	 */
	@Override
	public Result<UserProfileCO> getProfile() {
		return userProfileGetQryExe.execute();
	}

	/**
	 * 修改用户信息.
	 * @param cmd 修改用户信息参数
	 */
	@Override
	public void modifyProfile(UserProfileModifyCmd cmd) {
		userProfileModifyCmdExe.executeVoid(cmd);
	}

	/**
	 * 查询用户下拉框选择项列表.
	 * @param qry 查询用户下拉框选择项列表参数
	 * @return 用户下拉框选择项列表
	 */
	@Override
	public Result<List<Option>> findOptionList(UserOptionListQry qry) {
		return userOptionListQryExe.execute(qry);
	}

	/**
	 * 修改用户状态.
	 * @param cmd 修改用户状态参数
	 */
	@Override
	public void modifyStatus(UserStatusModifyCmd cmd) {
		userStatusModifyCmdExe.executeVoid(cmd);
	}

	/**
	 * 重置密码.
	 * @param cmd 重置密码参数
	 */
	@Override
	public void resetPassword(UserPasswordResetCmd cmd) {
		userPasswordResetCmdExe.executeVoid(cmd);
	}

	/**
	 * 根据ID查看用户.
	 * @param qry 根据ID查看用户参数
	 * @return 用户
	 */
	@Override
	public Result<UserCO> findById(UserGetQry qry) {
		return userGetQryExe.execute(qry);
	}

	/**
	 * 根据ID删除用户.
	 * @param cmd 根据ID删除用户参数
	 */
	@Override
	public void remove(UserRemoveCmd cmd) {
		userRemoveCmdExe.executeVoid(cmd);
	}

	/**
	 * 分页查询用户列表.
	 * @param qry 查询用户列表参数
	 * @return 用户列表
	 */
	@Override
	public Result<Page<UserCO>> page(UserListQry qry) {
		return userListQryExe.execute(qry);
	}

}
