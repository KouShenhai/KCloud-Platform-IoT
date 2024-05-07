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
import org.laokou.admin.convertor.UserConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.dto.user.UserListQry;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.DSConstant.BOOT_SYS_USER;

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

	private final UserConvertor userConvertor;

	/**
	 * 执行查询用户列表.
	 * @param qry 查询用户列表参数
	 * @return 用户列表
	 */
	@SneakyThrows
	@DataFilter(tableAlias = BOOT_SYS_USER)
	public Result<Datas<UserCO>> execute(UserListQry qry) {
		UserDO userDO = new UserDO(qry.getUsername());
		String secretKey = AesUtil.getSecretKeyStr();
		PageQuery page = qry;
		CompletableFuture<List<UserDO>> c1 = CompletableFuture
			.supplyAsync(() -> userMapper.selectListByCondition(userDO, page, secretKey), executor);
		CompletableFuture<Long> c2 = CompletableFuture
			.supplyAsync(() -> userMapper.selectCountByCondition(userDO, page, secretKey), executor);
		CompletableFuture.allOf(List.of(c1, c2).toArray(CompletableFuture[]::new)).join();
		return Result.ok(Datas.create(c1.get().stream().map(userConvertor::convertClientObj).toList(), c2.get()));
	}

}
