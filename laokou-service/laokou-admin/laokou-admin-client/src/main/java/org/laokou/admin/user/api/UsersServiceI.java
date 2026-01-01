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

package org.laokou.admin.user.api;

import org.laokou.admin.oss.dto.clientobject.OssUploadCO;
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
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * 用户接口.
 *
 * @author laokou
 */
public interface UsersServiceI {

	/**
	 * 保存用户.
	 * @param cmd 保存命令
	 */
	void saveUser(UserSaveCmd cmd) throws Exception;

	/**
	 * 修改用户.
	 * @param cmd 修改命令
	 */
	void modifyUser(UserModifyCmd cmd) throws Exception;

	/**
	 * 删除用户.
	 * @param cmd 删除命令
	 */
	void removeUser(UserRemoveCmd cmd) throws InterruptedException;

	/**
	 * 导入用户.
	 * @param cmd 导入命令
	 */
	void importUser(UserImportCmd cmd);

	/**
	 * 导出用户.
	 * @param cmd 导出命令
	 */
	void exportUser(UserExportCmd cmd);

	/**
	 * 重置密码.
	 * @param cmd 重置密码命令
	 */
	void resetUserPwd(UserResetPwdCmd cmd) throws Exception;

	/**
	 * 修改权限.
	 * @param cmd 修改权限命令
	 */
	void modifyUserAuthority(UserModifyAuthorityCmd cmd) throws Exception;

	/**
	 * 分页查询用户.
	 * @param qry 分页查询请求
	 */
	Result<Page<UserCO>> pageUser(UserPageQry qry);

	/**
	 * 查看用户.
	 * @param qry 查看请求
	 */
	Result<UserCO> getUserById(UserGetQry qry) throws Exception;

	/**
	 * 查看个人信息.
	 */
	Result<UserProfileCO> getUserProfile();

	/**
	 * 上传用户头像.
	 * @param cmd 上传命令
	 */
	Result<OssUploadCO> uploadUserAvatar(UserUploadAvatarCmd cmd) throws Exception;

}
