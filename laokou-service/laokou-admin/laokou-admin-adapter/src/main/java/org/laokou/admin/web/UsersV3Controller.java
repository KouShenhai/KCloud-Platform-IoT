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
import org.laokou.admin.api.UsersServiceI;
import org.laokou.admin.dto.user.UserListQry;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.dto.user.clientobject.UserProfileCO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Option;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.laokou.common.data.cache.constant.NameConstant.USERS;
import static org.laokou.common.data.cache.constant.Type.DEL;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/users")
@Tag(name = "用户管理", description = "用户管理")
public class UsersV3Controller {

	private final UsersServiceI usersServiceI;

	@TraceLog
	@GetMapping("profile")
	@Operation(summary = "个人中心-查看个人信息", description = "个人中心-查看个人信息")
	public Result<UserProfileCO> getProfileV3() {
		return usersServiceI.getProfile();
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('user:page')")
	@Operation(summary = "用户管理", description = "分页查询用户列表")
	public Result<Datas<UserCO>> pageV3(@RequestBody UserListQry qry) {
		return null;
	}

	@PutMapping
	@PreAuthorize("hasAuthority('user:modify')")
	@OperateLog(module = "用户管理", operation = "修改用户")
	@Operation(summary = "用户管理", description = "修改用户")
	@DataCache(name = USERS, key = "#cmd.co.id", type = DEL)
	public void modifyV3() {

	}

	@TraceLog
	@GetMapping("options")
	@Operation(summary = "用户管理", description = "查询用户下拉选择项列表")
	public Result<List<Option>> listOptionV3() {
		return null;
	}

	@PutMapping("status")
	@PreAuthorize("hasAuthority('user:modify-status')")
	@OperateLog(module = "用户管理", operation = "修改用户状态")
	@Operation(summary = "用户管理", description = "修改用户状态")
	public void modifyStatusV3() {

	}

	@PutMapping("reset-password")
	@PreAuthorize("hasAuthority('user:reset-password')")
	@OperateLog(module = "用户管理", operation = "重置密码")
	@Operation(summary = "用户管理", description = "重置密码")
	public void resetPasswordV3() {

	}

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('user:save')")
	@OperateLog(module = "用户管理", operation = "新增用户")
	@Operation(summary = "用户管理", description = "新增用户")
	public void saveV3() {
	}

	@TraceLog
	@GetMapping("{id}")
	@DataCache(name = USERS, key = "#id")
	@Operation(summary = "用户管理", description = "查看用户详情")
	public Result<UserCO> getByIdV3(@PathVariable("id") Long id) {
		return null;
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('user:remove')")
	@OperateLog(module = "用户管理", operation = "删除用户")
	@Operation(summary = "用户管理", description = "删除用户")
	public void removeV3(@RequestBody Long[] ids) {

	}

}
