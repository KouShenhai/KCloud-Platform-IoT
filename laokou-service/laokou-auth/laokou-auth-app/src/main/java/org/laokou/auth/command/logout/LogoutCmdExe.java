/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.auth.command.logout;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.dto.logout.LogoutCmd;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.security.utils.UserDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.stereotype.Component;

import java.security.Principal;

import static org.springframework.security.oauth2.server.authorization.OAuth2TokenType.ACCESS_TOKEN;

/**
 * 退出登录执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LogoutCmdExe {

	private final RedisUtil redisUtil;

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	/**
	 * 执行退出登录.
	 * @param cmd 退出登录参数
	 */
	public void executeVoid(LogoutCmd cmd) {
		String token = cmd.getToken();
		if (StringUtil.isEmpty(token)) {
			return;
		}
		OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, ACCESS_TOKEN);
		if (ObjectUtil.isNull(authorization)) {
			return;
		}
		UserDetail userDetail = (UserDetail) ((UsernamePasswordAuthenticationToken) ObjectUtil
			.requireNotNull(authorization.getAttribute(Principal.class.getName()))).getPrincipal();
		// 删除token
		removeAuthorization(authorization);
		// 删除菜单key
		removeMenuTreeKey(userDetail.getId());
		// 删除用户key
		removeUserInfoKey(token);
	}

	/**
	 * 移除令牌.
	 * @param authorization 认证对象
	 */
	private void removeAuthorization(OAuth2Authorization authorization) {
		oAuth2AuthorizationService.remove(authorization);
	}

	/**
	 * 删除用户信息Key.
	 * @param token 令牌
	 */
	private void removeUserInfoKey(String token) {
		redisUtil.delete(RedisKeyUtil.getUserInfoKey(token));
	}

	/**
	 * 删除树菜单Key.
	 * @param userId 用户ID
	 */
	private void removeMenuTreeKey(Long userId) {
		redisUtil.delete(RedisKeyUtil.getMenuTreeKey(userId));
	}

}
