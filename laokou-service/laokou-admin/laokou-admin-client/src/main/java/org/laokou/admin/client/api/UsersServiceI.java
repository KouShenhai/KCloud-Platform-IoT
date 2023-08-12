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
package org.laokou.admin.client.api;

import org.laokou.admin.client.dto.UserInsertCmd;
import org.laokou.admin.client.dto.UserOnlineKillCmd;
import org.laokou.admin.client.dto.UserOnlineListQry;
import org.laokou.admin.client.dto.UserUpdateCmd;
import org.laokou.admin.client.dto.clientobject.UserOnlineCO;
import org.laokou.admin.client.dto.clientobject.UserProfileCO;
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
	Result<Boolean> onlineKill(UserOnlineKillCmd cmd);

	/**
	 * 在线用户查询
	 * @param qry 查询
	 * @return Result<Datas<UserOnlineCO>>
	 */
	Result<Datas<UserOnlineCO>> onlineList(UserOnlineListQry qry);

	/**
	 * 用户基本信息
	 * @return Result<UserProfileCO>
	 */
	Result<UserProfileCO> profile();

	Result<List<>>

	// /**
	// * 修改密码
	// * @param id
	// * @param newPassword
	// * @return
	// */
	// Boolean updatePassword(Long id, String newPassword);
	//
	// /**
	// * 修改状态
	// * @param id
	// * @param status
	// * @return
	// */
	// Boolean updateStatus(Long id, Integer status);
	//
	// /**
	// * 修改个人信息
	// * @param dto
	// * @return
	// */
	// Boolean updateInfo(SysUserDTO dto);
	//
	// /**
	// * 分页查询用户
	// * @param qo
	// * @return
	// */
	// IPage<SysUserVO> queryUserPage(SysUserQo qo);
	//
	// /**
	// * 根据id查询用户
	// * @param id
	// * @return
	// */
	// SysUserVO getUserById(Long id);
	//
	// /**
	// * 根据id删除用户
	// * @param id
	// * @return
	// */
	// Boolean deleteUser(Long id);
	//
	// /**
	// * 用户下拉选择列表
	// * @return
	// */
	// List<OptionVO> getOptionList();
	//
	// /**
	// * 获取用户信息
	// * @return
	// */
	// UserInfoVO getUserInfo();
	//
	// /**
	// * 在线用户分页
	// * @param qo
	// * @return
	// */
	// Datas<SysUserOnlineVO> onlineQueryPage(SysUserOnlineQo qo);
	//
	// /**
	// * 账号踢出
	// * @param token
	// * @return
	// */
	// Boolean onlineKill(String token);

}
