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

import org.laokou.auth.dto.TokenRemoveCmd;
import org.laokou.common.context.util.UserDetails;
import org.laokou.common.data.cache.constant.NameConstants;
import org.laokou.common.data.cache.model.OperateTypeEnum;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
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
public class TokenRemoveCmdExe {

	private final CacheManager redissonCacheManager;

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	public TokenRemoveCmdExe(@Qualifier("redissonCacheManager") CacheManager redissonCacheManager,
			OAuth2AuthorizationService oAuth2AuthorizationService) {
		this.redissonCacheManager = redissonCacheManager;
		this.oAuth2AuthorizationService = oAuth2AuthorizationService;
	}

	/**
	 * 执行退出登录.
	 * @param cmd 退出登录参数
	 */
	@Async
	@CommandLog
	public void executeVoid(TokenRemoveCmd cmd) {
		String token = cmd.getToken();
		if (StringUtils.isEmpty(token)) {
			return;
		}
		OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (ObjectUtils.isNotNull(authorization)) {
			// 移除缓存
			evictCache(authorization);
			// 删除token
			oAuth2AuthorizationService.remove(authorization);
		}
	}

	private void evictCache(OAuth2Authorization authorization) {
		Object obj = authorization.getAttribute(Principal.class.getName());
		if (ObjectUtils.isNotNull(obj)) {
			UserDetails userDetails = (UserDetails) ((UsernamePasswordAuthenticationToken) obj).getPrincipal();
			OperateTypeEnum.getCache(redissonCacheManager, NameConstants.USER_MENU).evict(userDetails.getId());
		}
	}

}
