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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.user.api.UsersServiceI;
import org.laokou.admin.user.dto.*;
import org.laokou.admin.user.dto.clientobject.UserCO;
import org.laokou.admin.user.dto.clientobject.UserProfileCO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.laokou.common.data.cache.constant.NameConstant.USERS;
import static org.laokou.common.data.cache.constant.Type.DEL;

/**
 * 用户管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/users")
@Tag(name = "用户管理", description = "用户管理")
public class UsersControllerV3 {

	private final UsersServiceI usersServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:user:save')")
	@OperateLog(module = "用户管理", operation = "保存用户")
	@Operation(summary = "保存用户", description = "保存用户")
	public void saveV3(@RequestBody UserSaveCmd cmd) {
		usersServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:user:modify')")
	@OperateLog(module = "用户管理", operation = "修改用户")
	@Operation(summary = "修改用户", description = "修改用户")
	@DataCache(name = USERS, key = "#cmd.co.id", type = DEL)
	public void modifyV3(@RequestBody UserModifyCmd cmd) {
		usersServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:user:remove')")
	@OperateLog(module = "用户管理", operation = "删除用户")
	@Operation(summary = "删除用户", description = "删除用户")
	public void removeV3(@RequestBody Long[] ids) {
		usersServiceI.remove(new UserRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:user:import')")
	@OperateLog(module = "用户管理", operation = "导入用户")
	@Operation(summary = "导入用户", description = "导入用户")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		usersServiceI.importI(new UserImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:user:export')")
	@OperateLog(module = "用户管理", operation = "导出用户")
	@Operation(summary = "导出用户", description = "导出用户")
	public void exportV3(@RequestBody UserExportCmd cmd) {
		usersServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:user:page')")
	@Operation(summary = "分页查询用户列表", description = "分页查询用户列表")
	public Result<Page<UserCO>> pageV3(@RequestBody UserPageQry qry) {
		return usersServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@DataCache(name = USERS, key = "#id")
	@Operation(summary = "查看用户详情", description = "查看用户详情")
	public Result<UserCO> getByIdV3(@PathVariable("id") Long id) {
		return usersServiceI.getById(new UserGetQry(id));
	}

	@TraceLog
	@GetMapping("profile")
	@Operation(summary = "查看个人信息", description = "查看个人信息")
	public Result<UserProfileCO> getProfileV3() {
		return usersServiceI.getProfile();
	}

}
