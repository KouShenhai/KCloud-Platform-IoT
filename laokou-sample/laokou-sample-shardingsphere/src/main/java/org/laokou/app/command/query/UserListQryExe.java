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

package org.laokou.app.command.query;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.client.dto.clientobject.UserCO;
import org.laokou.common.i18n.dto.Result;
import org.laokou.infrastructure.gatewayimpl.database.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserListQryExe {

	private final UserMapper userMapper;

	public Result<List<UserCO>> execute() {
		return Result.ok(userMapper.selectList(Wrappers.emptyWrapper())
			.stream()
			.map(item -> new UserCO(item.getId(), item.getName()))
			.toList());
	}

}
