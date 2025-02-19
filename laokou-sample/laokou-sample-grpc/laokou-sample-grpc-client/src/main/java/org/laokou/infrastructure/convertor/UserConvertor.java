/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.infrastructure.convertor;

import org.laokou.client.dto.clientobject.UserCO;
import org.laokou.grpc.user.User;
import org.laokou.infrastructure.gatewayimpl.rpc.dataobject.UserDO;

/**
 * @author laokou
 */
public class UserConvertor {

	public static UserDO toDataObject(User user) {
		UserDO userDO = new UserDO();
		userDO.setId(user.getId());
		userDO.setUsername(user.getUsername());
		userDO.setPassword(user.getPassword());
		return userDO;
	}

	public static UserCO toClientObject(UserDO userDO) {
		UserCO userCO = new UserCO();
		userCO.setId(userDO.getId());
		userCO.setUsername(userDO.getUsername());
		userCO.setPassword(userDO.getPassword());
		return userCO;
	}

}
