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

package org.laokou.admin.command.log.query;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.convertor.LoginLogConvertor;
import org.laokou.admin.dto.log.LoginLogPageQry;
import org.laokou.admin.dto.log.clientobject.LoginLogCO;
import org.laokou.admin.gatewayimpl.database.LoginLogMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 查询登录日志列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LoginLogPageQryExe {

	private final LoginLogMapper loginLogMapper;

	private final Executor executor;

	private final LoginLogConvertor loginLogConvertor;

	/**
	 * 执行查询登录日志列表.
	 * @param qry 查询登录日志列表参数
	 * @return 登录日志列表
	 */
	// @DS(TENANT)
	@SneakyThrows
	// @DataFilter(tableAlias = BOOT_SYS_LOGIN_LOG)
	public Result<Datas<LoginLogCO>> execute(LoginLogPageQry qry) {
		List<LoginLogDO> page = loginLogMapper.selectList(Wrappers.emptyWrapper());
		return Result.ok(Datas.create(page.stream().map(loginLogConvertor::convertClientObj).toList(), 0));
		// LoginLogDO loginLogDO = null;
		// CompletableFuture<List<LoginLogDO>> c1 = CompletableFuture
		// .supplyAsync(() -> loginLogMapper.selectListByCondition(loginLogDO, qry),
		// executor);
		// CompletableFuture<Long> c2 = CompletableFuture
		// .supplyAsync(() -> loginLogMapper.selectObjCount(loginLogDO, qry), executor);
		// CompletableFuture.allOf(List.of(c1,
		// c2).toArray(CompletableFuture[]::new)).join();
		// return
		// Result.ok(Datas.create(c1.get().stream().map(loginLogConvertor::convertClientObj).toList(),
		// c2.get()));
	}

	// private LoginLogDO convert(LoginLogListQry qry) {
	// LoginLogDO loginLogDO = new LoginLogDO();
	// loginLogDO.setUsername(qry.getUsername());
	// loginLogDO.setTenantId(UserUtil.getTenantId());
	// loginLogDO.setStatus(qry.getStatus());
	// return loginLogDO;
	// }

}
