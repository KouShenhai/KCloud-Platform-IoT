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
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laokou
 */
@RestController
@Tag(name = "UsersController", description = "用户管理")
@RequiredArgsConstructor
public class UsersController {

	private final UsersServiceI usersServiceI;

	@TraceLog
	@GetMapping("v1/users/profile")
	@Operation(summary = "个人中心", description = "查看个人信息")
	public Result<UserProfileCO> getProfileV1() {
		return usersServiceI.getProfile();
	}

	@TraceLog
	@PostMapping("v1/users/page")
	@Operation(summary = "用户管理", description = "分页查询用户列表")
	@PreAuthorize("hasAuthority('user:page')")
	public Result<Datas<UserCO>> pageV1(@RequestBody UserListQry qry) {
		return usersServiceI.page(qry);
	}

}
