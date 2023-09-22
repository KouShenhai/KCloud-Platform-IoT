/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
 * @author laokou
 */
public interface UsersServiceI {

	/**
	 * 修改
	 * @param cmd 指定
	 * @return Result<Boolean>
	 */
	Result<Boolean> update(UserUpdateCmd cmd);

	/**
	 * 新增
	 * @param cmd 指令
	 * @return Result<Boolean>
	 */
	Result<Boolean> insert(UserInsertCmd cmd);

	/**
	 * 在线用户强踢
	 * @param cmd 指令
	 * @return Result<Boolean>
	 */
	Result<Boolean> onlineKill(OnlineUserKillCmd cmd);

	/**
	 * 在线用户查询
	 * @param qry 查询
	 * @return Result<Datas<UserOnlineCO>>
	 */
	Result<Datas<UserOnlineCO>> onlineList(OnlineUserListQry qry);

	/**
	 * 用户基本信息
	 * @return Result<UserProfileCO>
	 */
	Result<UserProfileCO> getProfile(UserProfileGetQry qry);

	Result<Boolean> updateProfile(UserProfileUpdateCmd cmd);

	/**
	 * 用户下拉列表
	 * @return Result<List<OptionCO>>
	 */
	Result<List<OptionCO>> optionList(UserOptionListQry qry);

	Result<Boolean> updateStatus(UserStatusUpdateCmd cmd);

	Result<Boolean> resetPassword(UserPasswordResetCmd cmd);

	Result<UserCO> getById(UserGetQry qry);

	Result<Boolean> deleteById(UserDeleteCmd cmd);

	Result<Datas<UserCO>> list(UserListQry qry);

}
