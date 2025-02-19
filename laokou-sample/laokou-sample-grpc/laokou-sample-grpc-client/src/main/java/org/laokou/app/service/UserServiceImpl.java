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

package org.laokou.app.service;

import lombok.RequiredArgsConstructor;
import org.laokou.app.command.query.UserGetQryExe;
import org.laokou.client.api.UserServiceI;
import org.laokou.client.dto.UserGetQry;
import org.laokou.client.dto.clientobject.UserCO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceI {

	private final UserGetQryExe userGetQryExe;

	@Override
	public Result<UserCO> getById(UserGetQry qry) {
		return userGetQryExe.execute(qry);
	}

}
