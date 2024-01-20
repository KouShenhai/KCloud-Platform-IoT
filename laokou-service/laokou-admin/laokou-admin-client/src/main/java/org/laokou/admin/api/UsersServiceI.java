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

package org.laokou.admin.api;

import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.user.*;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.dto.user.clientobject.UserOnlineCO;
import org.laokou.admin.dto.user.clientobject.UserProfileCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * 用户管理.
 * @author laokou
 */
public interface UsersServiceI {

	/**
	 * 修改用户.
	 * @param cmd 修改用户参数
	 * @return 修改结果
	 */
	Result<Boolean> update(UserUpdateCmd cmd);

	/**
	 * 新增用户.
	 * @param cmd 新增用户参数
	 * @return 新增结果
	 */
	Result<Boolean> insert(UserInsertCmd cmd);

	/**
	 * 强踢在线用户.
	 * @param cmd 强踢在线用户参数
	 * @return 强踢结果
	 */
	Result<Boolean> onlineKill(OnlineUserKillCmd cmd);

	/**
	 * 查询在线用户列表.
	 * @param qry 查询在线用户列表参数
	 * @return 在线用户列表
	 */
	Result<Datas<UserOnlineCO>> onlineList(OnlineUserListQry qry);

	/**
	 * 查看用户信息.
	 * @param qry 查看用户信息参数
	 * @return 用户信息
	 */
	Result<UserProfileCO> getProfile(UserProfileGetQry qry);

	/**
	 * 修改用户信息.
	 * @param cmd 修改用户信息参数
	 * @return 修改结果
	 */
	Result<Boolean> updateProfile(UserProfileUpdateCmd cmd);

	/**
	 * 查询用户下拉框选择项列表.
	 * @param qry 查询用户下拉框选择项列表参数
	 * @return 用户下拉框选择项列表
	 */
	Result<List<OptionCO>> optionList(UserOptionListQry qry);

	/**
	 * 修改用户状态
	 * @param cmd 修改用户状态参数
	 * @return 修改结果
	 */
	Result<Boolean> updateStatus(UserStatusUpdateCmd cmd);

	/**
	 * 重置密码
	 * @param cmd 重置密码参数
	 * @return 重置结果
	 */
	Result<Boolean> resetPassword(UserPasswordResetCmd cmd);

	/**
	 * 根据ID查看用户
	 * @param qry 根据ID查看用户参数
	 * @return 用户
	 */
	Result<UserCO> getById(UserGetQry qry);

	/**
	 * 根据ID删除用户
	 * @param cmd 根据ID删除用户参数
	 * @return 删除结果
	 */
	Result<Boolean> deleteById(UserDeleteCmd cmd);

	/**
	 * 查询用户列表
	 * @param qry 查询用户列表参数
	 * @return 用户列表
	 */
	Result<Datas<UserCO>> list(UserListQry qry);

}
