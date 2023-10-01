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

package org.laokou.admin.command.log.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.LogGateway;
import org.laokou.admin.domain.log.LoginLog;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.log.LoginLogListQry;
import org.laokou.admin.dto.log.clientobject.LoginLogCO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LoginLogListQryExe {

	private final LogGateway logGateway;

	public Result<Datas<LoginLogCO>> execute(LoginLogListQry qry) {
		LoginLog loginLog = ConvertUtil.sourceToTarget(qry, LoginLog.class);
		Datas<LoginLog> newPage = logGateway.loginList(loginLog, new User(UserUtil.getTenantId()), qry);
		Datas<LoginLogCO> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), LoginLogCO.class));
		return Result.of(datas);
	}

}
