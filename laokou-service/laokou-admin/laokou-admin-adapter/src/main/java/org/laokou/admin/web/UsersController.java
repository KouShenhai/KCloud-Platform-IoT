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
import org.laokou.admin.client.api.UsersServiceI;
import org.laokou.admin.client.dto.common.clientobject.OptionCO;
import org.laokou.admin.client.dto.user.*;
import org.laokou.admin.client.dto.user.clientobject.UserCO;
import org.laokou.admin.client.dto.user.clientobject.UserOnlineCO;
import org.laokou.admin.client.dto.user.clientobject.UserProfileCO;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.enums.Cache;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
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
public class UsersController {

	private final UsersServiceI usersServiceI;

	@TraceLog
	@PutMapping("v1/users")
	@Operation(summary = "用户管理", description = "修改用户")
	@OperateLog(module = "用户管理", operation = "修改用户")
	@PreAuthorize("hasAuthority('users:update')")
	@DataCache(name = "users", key = "#dto.id", type = Cache.DEL)
	public Result<Boolean> update(@RequestBody UserUpdateCmd cmd) {
		return usersServiceI.update(cmd);
	}

	@TraceLog
	@PostMapping("v1/users/online-list")
	@PreAuthorize("hasAuthority('users:online-list')")
	@Operation(summary = "在线用户", description = "查询在线用户列表")
	public Result<Datas<UserOnlineCO>> onlineList() {
		return usersServiceI.onlineList(new UserOnlineListQry());
	}

	@TraceLog
	@DeleteMapping("v1/users/online-kill")
	@Operation(summary = "在线用户", description = "强踢在线用户")
	@OperateLog(module = "用户管理", operation = "强踢在线用户")
	@PreAuthorize("hasAuthority('users:online-kill')")
	public Result<Boolean> onlineKill(@RequestBody UserOnlineKillCmd cmd) {
		return usersServiceI.onlineKill(cmd);
	}

	@TraceLog
	@GetMapping("v1/users/profile")
	@Operation(summary = "个人中心", description = "查看个人信息")
	public Result<UserProfileCO> profile() {
		return usersServiceI.profile(new UserProfileGetQry());
	}

	@TraceLog
	@GetMapping("v1/users/option-list")
	@Operation(summary = "用户管理", description = "下拉列表")
	public Result<List<OptionCO>> optionList() {
		return usersServiceI.optionList(new UserOptionListQry());
	}

	@TraceLog
	@PutMapping("v1/users/profile")
	@Operation(summary = "个人中心", description = "修改个人信息")
	public Result<Boolean> profile(@RequestBody UserProfileUpdateCmd cmd) {
		return usersServiceI.profile(cmd);
	}

	@TraceLog
	@PutMapping("v1/users/status")
	@Operation(summary = "用户管理", description = "修改用户状态")
	@OperateLog(module = "用户管理", operation = "修改用户状态")
	@PreAuthorize("hasAuthority('users:status')")
	public Result<Boolean> status(@RequestBody UserStatusUpdateCmd cmd) {
		return usersServiceI.status(cmd);
	}

	@TraceLog
	@PutMapping("v1/users/reset-password")
	@Operation(summary = "用户管理", description = "重置密码")
	@OperateLog(module = "用户管理", operation = "重置密码")
	@PreAuthorize("hasAuthority('users:reset-password')")
	public Result<Boolean> resetPassword(@RequestBody UserPasswordResetCmd cmd) {
		return usersServiceI.resetPassword(cmd);
	}

	@TraceLog
	@PutMapping("v1/users/profile-password")
	@Operation(summary = "个人中心", description = "修改密码")
	public Result<Boolean> profilePassword(@RequestBody UserPasswordResetCmd cmd) {
		return usersServiceI.resetPassword(cmd);
	}

	@TraceLog
	@PostMapping("v1/users")
	@Operation(summary = "用户管理", description = "新增用户")
	@OperateLog(module = "用户管理", operation = "新增用户")
	@PreAuthorize("hasAuthority('users:insert')")
	public Result<Boolean> insert(@RequestBody UserInsertCmd cmd) {
		return usersServiceI.insert(cmd);
	}

	@TraceLog
	@GetMapping("v1/users/{id}")
	@Operation(summary = "用户管理", description = "查看用户")
	@DataCache(name = "users", key = "#id")
	public Result<UserCO> get(@PathVariable("id") Long id) {
		return usersServiceI.get(new UserGetQry(id));
	}

	@TraceLog
	@DeleteMapping("v1/users/{id}")
	@Operation(summary = "用户管理", description = "删除用户")
	@OperateLog(module = "用户管理", operation = "删除用户")
	@PreAuthorize("hasAuthority('users:delete')")
	@DataCache(name = "users", key = "#id", type = Cache.DEL)
	public Result<Boolean> delete(@PathVariable("id") Long id) {
		return usersServiceI.delete(new UserDeleteCmd(id));
	}

	@TraceLog
	@PostMapping("v1/users/list")
	@Operation(summary = "用户管理", description = "查询用户列表")
	@PreAuthorize("hasAuthority('users:list')")
	public Result<Datas<UserCO>> list(@RequestBody UserListQry qry) {
		return usersServiceI.list(qry);
	}

}
