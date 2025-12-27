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
import org.laokou.common.context.util.User;
import org.laokou.common.data.cache.constant.NameConstants;
import org.laokou.common.data.cache.model.OperateTypeEnum;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
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

	private final CacheManager distributedCacheManager;

	private final OAuth2AuthorizationService authorizationService;

	public TokenRemoveCmdExe(@Qualifier("distributedCacheManager") CacheManager distributedCacheManager,
			OAuth2AuthorizationService authorizationService) {
		this.distributedCacheManager = distributedCacheManager;
		this.authorizationService = authorizationService;
	}

	/**
	 * 执行退出登录.
	 * @param cmd 退出登录参数
	 */
	@Async("virtualThreadExecutor")
	@CommandLog
	public void executeVoid(TokenRemoveCmd cmd) {
		String token = cmd.getToken();
		if (StringExtUtils.isEmpty(token)) {
			return;
		}
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (ObjectUtils.isNotNull(authorization)) {
			// 移除缓存
			evictCache(authorization);
			// 删除token
			authorizationService.remove(authorization);
		}
	}

	private void evictCache(OAuth2Authorization authorization) {
		if (authorization.getAttribute(Principal.class.getName()) instanceof User user) {
			OperateTypeEnum.getCache(distributedCacheManager, NameConstants.USER_MENU).evict(user.id());
		}
	}

}
