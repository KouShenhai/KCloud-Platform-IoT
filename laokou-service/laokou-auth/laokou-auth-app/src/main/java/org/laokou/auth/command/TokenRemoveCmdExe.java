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
import org.laokou.common.data.cache.handler.event.RemovedCacheEvent;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.common.constant.EventTypeEnum;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.rocketmq.template.SendMessageTypeEnum;
import org.laokou.common.security.util.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.stereotype.Component;
import java.security.Principal;
import static org.laokou.common.data.cache.constant.MqConstants.LAOKOU_CACHE_TOPIC;
import static org.laokou.common.data.cache.constant.NameConstants.USER_MENU;
import static org.laokou.common.security.config.GlobalOpaqueTokenIntrospector.FULL;

/**
 * 退出登录执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TokenRemoveCmdExe {

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	private final DomainEventPublisher rocketMQDomainEventPublisher;

	/**
	 * 执行退出登录.
	 * @param cmd 退出登录参数
	 */
	@CommandLog
	public void executeVoid(TokenRemoveCmd cmd) {
		String token = cmd.getToken();
		if (StringUtils.isEmpty(token)) {
			return;
		}
		OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, FULL);
		if (ObjectUtils.isNotNull(authorization)) {
			Object obj = authorization.getAttribute(Principal.class.getName());
			if (ObjectUtils.isNotNull(obj)) {
				UserDetails userDetails = (UserDetails) ((UsernamePasswordAuthenticationToken) obj).getPrincipal();
				publishEvent(userDetails.getId());
			}
			// 删除token
			oAuth2AuthorizationService.remove(authorization);
		}
	}

	private void publishEvent(Long userId) {
		rocketMQDomainEventPublisher.publish(new DomainEvent(1L, 0L, 1L, 1L, LAOKOU_CACHE_TOPIC, null, 0,
				JacksonUtils.toJsonStr(new RemovedCacheEvent(USER_MENU, String.valueOf(userId))),
				EventTypeEnum.REMOVE_CACHE_EVENT, null), SendMessageTypeEnum.ASYNC);
	}

}
