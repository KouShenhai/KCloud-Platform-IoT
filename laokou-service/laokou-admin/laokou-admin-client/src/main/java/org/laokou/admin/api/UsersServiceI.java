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

package org.laokou.admin.api;

import org.laokou.common.i18n.dto.Option;
import org.laokou.admin.dto.user.*;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.dto.user.clientobject.UserProfileCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * 用户管理.
 *
 * @author laokou
 */
public interface UsersServiceI {

	/**
	 * 修改用户.
	 * @param cmd 修改用户参数
	 */
	void modify(UserModifyCmd cmd);

	/**
	 * 新增用户.
	 * @param cmd 新增用户参数
	 */

	void create(UserCreateCmd cmd);

	/**
	 * 查看用户信息.
	 * @return 用户信息
	 */
	Result<UserProfileCO> getProfile();

	/**
	 * 修改用户信息.
	 * @param cmd 修改用户信息参数
	 */
	void modifyProfile(UserProfileModifyCmd cmd);

	/**
	 * 查询用户下拉框选择项列表.
	 * @param qry 查询用户下拉框选择项列表参数
	 * @return 用户下拉框选择项列表
	 */
	Result<List<Option>> findOptionList(UserOptionListQry qry);

	/**
	 * 修改用户状态.
	 * @param cmd 修改用户状态参数
	 */
	void modifyStatus(UserStatusModifyCmd cmd);

	/**
	 * 重置密码.
	 * @param cmd 重置密码参数
	 */
	void resetPassword(UserPasswordResetCmd cmd);

	/**
	 * 根据ID查看用户.
	 * @param qry 根据ID查看用户参数
	 * @return 用户
	 */
	Result<UserCO> findById(UserGetQry qry);

	/**
	 * 根据IDS删除用户.
	 * @param cmd 根据IDS删除用户参数
	 */
	void remove(UserRemoveCmd cmd);

	/**
	 * 分页查询用户列表.
	 * @param qry 查询用户列表参数
	 * @return 用户列表
	 */
	Result<Datas<UserCO>> page(UserListQry qry);

}
