/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.user.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.gateway.UserGateway;
import org.laokou.admin.user.gatewayimpl.database.UserMapper;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserDO;
import org.laokou.admin.user.model.UserA;
import org.springframework.stereotype.Component;
import java.util.Arrays;

/**
 * 用户网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component("adminUserGateway")
@RequiredArgsConstructor
public class UserGatewayImpl implements UserGateway {

	private final UserMapper userMapper;

	@Override
	public void createUser(UserA userA) {
		UserDO userDO = UserConvertor.toDataObject(userA);
		userMapper.insert(userDO);
	}

	@Override
	public void updateUser(UserA userA) {
		UserDO userDO = UserConvertor.toDataObject(userA);
		userDO.setVersion(userMapper.selectVersion(userA.getId()));
		userMapper.updateById(userDO);
	}

	@Override
	public void deleteUser(Long[] ids) {
		userMapper.deleteByIds(Arrays.asList(ids));
	}

}
