/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.oss.dto.clientobject.OssUploadCO;
import org.laokou.admin.user.api.UsersServiceI;
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
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.constant.NameConstants;
import org.laokou.common.data.cache.model.OperateTypeEnum;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户管理控制器.
 *
 * @author laokou
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户管理")
public class UsersController {

	private final UsersServiceI usersServiceI;

	@Idempotent
	@PostMapping("/v1/users")
	@PreAuthorize("hasAuthority('sys:user:save')")
	@OperateLog(module = "用户管理", operation = "保存用户")
	@Operation(summary = "保存用户", description = "保存用户")
	public void saveUser(@RequestBody UserSaveCmd cmd) throws Exception {
		usersServiceI.saveUser(cmd);
	}

	@PutMapping("/v1/users")
	@PreAuthorize("hasAuthority('sys:user:modify')")
	@OperateLog(module = "用户管理", operation = "修改用户")
	@Operation(summary = "修改用户", description = "修改用户")
	@DataCache(name = NameConstants.USERS, key = "#cmd.co.id", operateType = OperateTypeEnum.DEL)
	public void modifyUser(@RequestBody UserModifyCmd cmd) throws Exception {
		usersServiceI.modifyUser(cmd);
	}

	@DeleteMapping("/v1/users")
	@PreAuthorize("hasAuthority('sys:user:remove')")
	@OperateLog(module = "用户管理", operation = "删除用户")
	@Operation(summary = "删除用户", description = "删除用户")
	public void removeUser(@RequestBody Long[] ids) throws InterruptedException {
		usersServiceI.removeUser(new UserRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/users/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:user:import')")
	@OperateLog(module = "用户管理", operation = "导入用户")
	@Operation(summary = "导入用户", description = "导入用户")
	public void importUser(@RequestPart("files") MultipartFile[] files) {
		usersServiceI.importUser(new UserImportCmd(files));
	}

	@PostMapping("/v1/users/export")
	@PreAuthorize("hasAuthority('sys:user:export')")
	@OperateLog(module = "用户管理", operation = "导出用户")
	@Operation(summary = "导出用户", description = "导出用户")
	public void exportUser(@RequestBody UserExportCmd cmd) {
		usersServiceI.exportUser(cmd);
	}

	@PutMapping("/v1/users/reset-pwd")
	@PreAuthorize("hasAuthority('sys:user:modify')")
	@OperateLog(module = "用户管理", operation = "重置用户密码")
	@Operation(summary = "重置用户密码", description = "重置用户密码")
	public void resetUserPwd(@RequestBody UserResetPwdCmd cmd) throws Exception {
		usersServiceI.resetUserPwd(cmd);
	}

	@PutMapping("/v1/users/authority")
	@PreAuthorize("hasAuthority('sys:user:modify')")
	@DataCache(name = NameConstants.USERS, key = "#cmd.co.id", operateType = OperateTypeEnum.DEL)
	@OperateLog(module = "用户管理", operation = "修改用户权限")
	@Operation(summary = "修改用户权限", description = "修改用户权限")
	public void modifyUserAuthority(@RequestBody UserModifyAuthorityCmd cmd) throws Exception {
		usersServiceI.modifyUserAuthority(cmd);
	}

	@TraceLog
	@PostMapping("/v1/users/page")
	@PreAuthorize("hasAuthority('sys:user:page')")
	@Operation(summary = "分页查询用户列表", description = "分页查询用户列表")
	public Result<Page<UserCO>> pageUser(@Validated @RequestBody UserPageQry qry) {
		return usersServiceI.pageUser(qry);
	}

	@TraceLog
	@GetMapping("/v1/users/{id}")
	@DataCache(name = NameConstants.USERS, key = "#id")
	@PreAuthorize("hasAuthority('sys:user:detail')")
	@Operation(summary = "查看用户详情", description = "查看用户详情")
	public Result<UserCO> getUserById(@PathVariable("id") Long id) throws Exception {
		return usersServiceI.getUserById(new UserGetQry(id));
	}

	@TraceLog
	@GetMapping("/v1/users/profile")
	@Operation(summary = "查看个人信息", description = "查看个人信息")
	public Result<UserProfileCO> getUserProfile() {
		return usersServiceI.getUserProfile();
	}

	@TraceLog
	@PostMapping(value = "/v1/users/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("(hasAuthority('sys:oss:upload') or hasAuthority('sys:oss:save')) and hasAuthority('sys:user:modify')")
	@Operation(summary = "上传用户头像", description = "上传用户头像")
	@OperateLog(module = "用户管理", operation = "上传用户头像")
	public Result<OssUploadCO> uploadUserAvatar(@RequestPart("file") MultipartFile file) throws Exception {
		return usersServiceI.uploadUserAvatar(new UserUploadAvatarCmd(file));
	}

}
