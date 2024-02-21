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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.UsersServiceI;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.user.*;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.dto.user.clientobject.UserProfileCO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.common.CacheOperatorTypeEnums;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.laokou.common.i18n.common.CacheNameConstants.USERS;

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
	@PreAuthorize("hasAuthority('users:modify')")
	@DataCache(name = USERS, key = "#cmd.userCO.id", type = CacheOperatorTypeEnums.DEL)
	public void modify(@RequestBody UserModifyCmd cmd) {
		usersServiceI.modify(cmd);
	}

	@TraceLog
	@GetMapping("profile")
	@Operation(summary = "个人中心", description = "查看个人信息")
	public Result<UserProfileCO> findProfile() {
		return usersServiceI.findProfile();
	}

	@TraceLog
	@GetMapping("option-list")
	@Operation(summary = "用户管理", description = "下拉列表")
	public Result<List<OptionCO>> findOptionList() {
		return usersServiceI.findOptionList(new UserOptionListQry());
	}

	@TraceLog
	@PutMapping("profile")
	@Operation(summary = "个人中心", description = "修改个人信息")
	public void modifyProfile(@RequestBody UserProfileModifyCmd cmd) {
		usersServiceI.modifyProfile(cmd);
	}

	@TraceLog
	@PutMapping("status")
	@Operation(summary = "用户管理", description = "修改用户状态")
	@OperateLog(module = "用户管理", operation = "修改用户状态")
	@PreAuthorize("hasAuthority('users:modify-status')")
	public void modifyStatus(@RequestBody UserStatusModifyCmd cmd) {
		usersServiceI.modifyStatus(cmd);
	}

	@TraceLog
	@PutMapping("reset-password")
	@Operation(summary = "用户管理", description = "重置密码")
	@OperateLog(module = "用户管理", operation = "重置密码")
	@PreAuthorize("hasAuthority('users:reset-password')")
	public void resetPassword(@RequestBody UserPasswordResetCmd cmd) {
		usersServiceI.resetPassword(cmd);
	}

	@TraceLog
	@PutMapping("password")
	@Operation(summary = "个人中心", description = "修改密码")
	public void modifyPassword(@RequestBody UserPasswordResetCmd cmd) {
		usersServiceI.resetPassword(cmd);
	}

	@Idempotent
	@TraceLog
	@PostMapping
	@Operation(summary = "用户管理", description = "新增用户")
	@OperateLog(module = "用户管理", operation = "新增用户")
	@PreAuthorize("hasAuthority('users:create')")
	public void create(@RequestBody UserCreateCmd cmd) {
		usersServiceI.create(cmd);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "用户管理", description = "查看用户")
	@DataCache(name = USERS, key = "#id")
	public Result<UserCO> findById(@PathVariable("id") Long id) {
		return usersServiceI.findById(new UserGetQry(id));
	}

	@TraceLog
	@DeleteMapping
	@Operation(summary = "用户管理", description = "删除用户")
	@OperateLog(module = "用户管理", operation = "删除用户")
	@PreAuthorize("hasAuthority('users:remove')")
	public void remove(@RequestBody Long[] ids) {
		usersServiceI.remove(new UserRemoveCmd(ids));
	}

	@TraceLog
	@PostMapping("list")
	@Operation(summary = "用户管理", description = "查询用户列表")
	@PreAuthorize("hasAuthority('users:list')")
	public Result<Datas<UserCO>> findList(@RequestBody UserListQry qry) {
		return usersServiceI.findList(qry);
	}

}
