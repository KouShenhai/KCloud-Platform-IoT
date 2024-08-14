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

package org.laokou.admin.command.user.query;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.dto.user.UserListQry;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 查询用户列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserListQryExe {

	private final UserMapper userMapper;

	private final Executor executor;

	/**
	 * 执行查询用户列表.
	 * @param qry 查询用户列表参数
	 * @return 用户列表
	 */
	@SneakyThrows
	// @DataFilter(tableAlias = BOOT_SYS_USER)
	public Result<Page<UserCO>> execute(UserListQry qry) {
		UserDO userDO = new UserDO(qry.getUsername());
		CompletableFuture<List<UserDO>> c1 = CompletableFuture
			.supplyAsync(() -> userMapper.selectListByCondition(userDO, qry), executor);
		CompletableFuture<Long> c2 = CompletableFuture.supplyAsync(() -> userMapper.selectCountByCondition(userDO, qry),
				executor);
		// return Result
		// .ok(Datas.create(c1.get(30,
		// TimeUnit.SECONDS).stream().map(userConvertor::convertClientObj).toList(),
		// c2.get(30, TimeUnit.SECONDS)));
		return null;
	}

}
