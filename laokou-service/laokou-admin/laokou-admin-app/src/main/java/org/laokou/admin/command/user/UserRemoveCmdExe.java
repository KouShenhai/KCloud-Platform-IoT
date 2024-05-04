/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.dto.user.UserRemoveCmd;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DSConstant.TENANT;

/**
 * 删除用户执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserRemoveCmdExe {

	private final UserGateway userGateway;

	/**
	 * 执行删除用户.
	 * @param cmd 删除用户参数
	 */
	@DS(TENANT)
	public void executeVoid(UserRemoveCmd cmd) {
		userGateway.remove(cmd.getIds());
	}

}
