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

package org.laokou.admin.command.user.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.user.UserListQry;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.jasypt.utils.AesUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserListQryExe {

	private final UserGateway userGateway;

	public Result<Datas<UserCO>> execute(UserListQry qry) {
		User user = new User(qry.getUsername(), UserUtil.getTenantId());
		Datas<User> newPage = userGateway.list(user, qry);
		Datas<UserCO> datas = new Datas<>();
		List<User> records = newPage.getRecords();
		if (CollectionUtil.isNotEmpty(records)) {
			List<UserCO> coList = ConvertUtil.sourceToTarget(records, UserCO.class);
			coList.forEach(item -> item.setUsername(AesUtil.decrypt(item.getUsername())));
			datas.setRecords(coList);
		}
		datas.setLastId(newPage.getLastId());
		datas.setTotal(newPage.getTotal());
		return Result.of(datas);
	}

}
