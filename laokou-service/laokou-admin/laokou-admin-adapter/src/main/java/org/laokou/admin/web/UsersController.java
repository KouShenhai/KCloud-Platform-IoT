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
package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.UsersServiceI;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.user.*;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.dto.user.clientobject.UserOnlineCO;
import org.laokou.admin.dto.user.clientobject.UserProfileCO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.aspect.Type;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author laokou
 */
@RestController
@Tag(name = "UsersController", description = "用户管理")
@RequiredArgsConstructor
@RequestMapping("v1/users")
public class UsersController {

	private final UsersServiceI usersServiceI;

	@TraceLog
	@PutMapping
	@Operation(summary = "用户管理", description = "修改用户")
	@OperateLog(module = "用户管理", operation = "修改用户")
	@PreAuthorize("hasAuthority('users:update')")
	@DataCache(name = "users", key = "#cmd.userCO.id", type = Type.DEL)
	public Result<Boolean> update(@RequestBody UserUpdateCmd cmd) {
		return usersServiceI.update(cmd);
	}

	@TraceLog
	@PostMapping("online-list")
	@PreAuthorize("hasAuthority('users:online-list')")
	@Operation(summary = "在线用户", description = "查询在线用户列表")
	public Result<Datas<UserOnlineCO>> onlineList(@RequestBody OnlineUserListQry qry) {
		return usersServiceI.onlineList(qry);
	}

	@TraceLog
	@DeleteMapping("online-kill")
	@Operation(summary = "在线用户", description = "强踢在线用户")
	@OperateLog(module = "用户管理", operation = "强踢在线用户")
	@PreAuthorize("hasAuthority('users:online-kill')")
	public Result<Boolean> onlineKill(@RequestBody OnlineUserKillCmd cmd) {
		return usersServiceI.onlineKill(cmd);
	}

	@TraceLog
	@GetMapping("profile")
	@Operation(summary = "个人中心", description = "查看个人信息")
	public Result<UserProfileCO> getProfile() {
		return usersServiceI.getProfile(new UserProfileGetQry());
	}

	@TraceLog
	@GetMapping("option-list")
	@Operation(summary = "用户管理", description = "下拉列表")
	public Result<List<OptionCO>> optionList() {
		return usersServiceI.optionList(new UserOptionListQry());
	}

	@TraceLog
	@PutMapping("profile")
	@Operation(summary = "个人中心", description = "修改个人信息")
	public Result<Boolean> updateProfile(@RequestBody UserProfileUpdateCmd cmd) {
		return usersServiceI.updateProfile(cmd);
	}

	@TraceLog
	@PutMapping("status")
	@Operation(summary = "用户管理", description = "修改用户状态")
	@OperateLog(module = "用户管理", operation = "修改用户状态")
	@PreAuthorize("hasAuthority('users:status')")
	public Result<Boolean> updateStatus(@RequestBody UserStatusUpdateCmd cmd) {
		return usersServiceI.updateStatus(cmd);
	}

	@TraceLog
	@PutMapping("reset-password")
	@Operation(summary = "用户管理", description = "重置密码")
	@OperateLog(module = "用户管理", operation = "重置密码")
	@PreAuthorize("hasAuthority('users:reset-password')")
	public Result<Boolean> resetPassword(@RequestBody UserPasswordResetCmd cmd) {
		return usersServiceI.resetPassword(cmd);
	}

	@TraceLog
	@PutMapping("password")
	@Operation(summary = "个人中心", description = "修改密码")
	public Result<Boolean> updatePassword(@RequestBody UserPasswordResetCmd cmd) {
		return usersServiceI.resetPassword(cmd);
	}

	@Idempotent
	@TraceLog
	@PostMapping
	@Operation(summary = "用户管理", description = "新增用户")
	@OperateLog(module = "用户管理", operation = "新增用户")
	@PreAuthorize("hasAuthority('users:insert')")
	public Result<Boolean> insert(@RequestBody UserInsertCmd cmd) {
		return usersServiceI.insert(cmd);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "用户管理", description = "查看用户")
	@DataCache(name = "users", key = "#id")
	public Result<UserCO> getById(@PathVariable("id") Long id) {
		return usersServiceI.getById(new UserGetQry(id));
	}

	@TraceLog
	@DeleteMapping("{id}")
	@Operation(summary = "用户管理", description = "删除用户")
	@OperateLog(module = "用户管理", operation = "删除用户")
	@PreAuthorize("hasAuthority('users:delete')")
	@DataCache(name = "users", key = "#id", type = Type.DEL)
	public Result<Boolean> deleteById(@PathVariable("id") Long id) {
		return usersServiceI.deleteById(new UserDeleteCmd(id));
	}

	@TraceLog
	@PostMapping("list")
	@Operation(summary = "用户管理", description = "查询用户列表")
	@PreAuthorize("hasAuthority('users:list')")
	public Result<Datas<UserCO>> list(@RequestBody UserListQry qry) {
		return usersServiceI.list(qry);
	}

}
