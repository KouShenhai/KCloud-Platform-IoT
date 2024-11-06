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

package org.laokou.admin.loginLog.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.loginLog.convertor.LoginLogConvertor;
import org.laokou.admin.loginLog.gateway.LoginLogGateway;
import org.laokou.admin.loginLog.gatewayimpl.database.LoginLogMapper;
import org.laokou.admin.loginLog.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.admin.loginLog.model.LoginLogE;
import org.laokou.common.core.utils.ArrayUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 登录日志网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LoginLogGatewayImpl implements LoginLogGateway {

	private final LoginLogMapper loginLogMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	public void create(LoginLogE loginLogE) {
		transactionalUtil
			.executeInTransaction(() -> loginLogMapper.insert(LoginLogConvertor.toDataObject(loginLogE, true)));
	}

	@Override
	public void update(LoginLogE loginLogE) {
		LoginLogDO loginLogDO = LoginLogConvertor.toDataObject(loginLogE, false);
		loginLogDO.setVersion(loginLogMapper.selectVersion(loginLogE.getId()));
		update(loginLogDO);
	}

	@Override
	public void delete(Long[] ids) {
		transactionalUtil.executeInTransaction(() -> {
			if (ArrayUtil.isNotEmpty(ids)) {
				loginLogMapper.deleteByIds(Arrays.asList(ids));
			}
			else {
				loginLogMapper.deleteAll();
			}
		});
	}

	private void update(LoginLogDO loginLogDO) {
		transactionalUtil.executeInTransaction(() -> loginLogMapper.updateById(loginLogDO));
	}

}
