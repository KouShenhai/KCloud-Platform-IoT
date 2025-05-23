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

package org.laokou.auth.service;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.api.TokensServiceI;
import org.laokou.auth.command.TokenRemoveCmdExe;
import org.laokou.auth.dto.TokenRemoveCmd;
import org.springframework.stereotype.Service;

/**
 * 退出登录.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class TokensServiceImpl implements TokensServiceI {

	private final TokenRemoveCmdExe tokenRemoveCmdExe;

	/**
	 * 移除Token.
	 * @param cmd 退出登录参数
	 */
	@Override
	public void removeToken(TokenRemoveCmd cmd) {
		tokenRemoveCmdExe.executeVoid(cmd);
	}

}
