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

package org.laokou.admin.command.logout;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.logout.LogoutCmd;
import org.laokou.common.security.utils.UserDetail;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;

import java.security.Principal;

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
	 * @return 执行退出结果
	 */
	public Result<Boolean> execute(LogoutCmd cmd) {
		String token = cmd.getToken();
		if (StringUtil.isEmpty(token)) {
			return Result.of(true);
		}
		OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (ObjectUtil.isNull(authorization)) {
			return Result.of(true);
		}
		UserDetail userDetail = (UserDetail) ((UsernamePasswordAuthenticationToken) ObjectUtil
			.requireNotNull(authorization.getAttribute(Principal.class.getName()))).getPrincipal();
		if (ObjectUtil.isNull(userDetail)) {
			return Result.of(true);
		}
		Long userId = userDetail.getId();
		// 删除token
		removeToken(authorization);
		// 删除菜单key
		deleteMenuTreeKey(userId);
		// 删除用户key
		deleteUserInfoKey(token);
		// 删除强踢Key
		deleteUserKillKey(token);
		return Result.of(true);
	}

	/**
	 * 移除令牌.
	 * @param authorization 认证对象
	 */
	private void removeToken(OAuth2Authorization authorization) {
		oAuth2AuthorizationService.remove(authorization);
	}

	/**
	 * 删除用户信息Key.
	 * @param token 令牌
	 */
	private void deleteUserInfoKey(String token) {
		String userInfoKey = RedisKeyUtil.getUserInfoKey(token);
		redisUtil.delete(userInfoKey);
	}

	/**
	 * 删除用户强踢Key.
	 * @param token 令牌
	 */
	private void deleteUserKillKey(String token) {
		String userKillKey = RedisKeyUtil.getUserKillKey(token);
		redisUtil.delete(userKillKey);
	}

	/**
	 * 删除树菜单Key.
	 * @param userId 用户ID
	 */
	private void deleteMenuTreeKey(Long userId) {
		String resourceTreeKey = RedisKeyUtil.getMenuTreeKey(userId);
		redisUtil.delete(resourceTreeKey);
	}

}
