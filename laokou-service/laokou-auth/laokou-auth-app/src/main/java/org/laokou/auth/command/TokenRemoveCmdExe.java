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

package org.laokou.auth.command;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.dto.TokenRemoveCmd;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.stereotype.Component;

import static org.laokou.common.security.config.GlobalOpaqueTokenIntrospector.FULL;

/**
 * 退出登录执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TokenRemoveCmdExe {

	private static final String MENU_TREE = "menu_tree";

	private final RedisUtil redisUtil;

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	/**
	 * 执行退出登录.
	 * @param cmd 退出登录参数
	 */
	public void executeVoid(TokenRemoveCmd cmd) {
		String token = cmd.getToken();
		if (StringUtil.isEmpty(token)) {
			return;
		}
		// 删除树形菜单key
		redisUtil.hDel(MENU_TREE, token);
		OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, FULL);
		if (ObjectUtil.isNotNull(authorization)) {
			// 删除token
			oAuth2AuthorizationService.remove(authorization);
		}
	}

}
