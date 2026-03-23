/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.config.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.convertor.LoginLogConvertor;
import org.laokou.auth.convertor.UserConvertor;
import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.enums.MqTopic;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * 认证授权.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
final class OAuth2UsernamePasswordAuthentication {

	private final DomainService domainService;

	private final DomainEventPublisher kafkaDomainEventPublisher;

	public AuthA authentication(@NonNull AuthA authA, @NonNull HttpServletRequest request) {
		LoginEvent evt = null;
		try {
			// 认证授权
			domainService.auth(authA);
			// 记录日志【登录成功】
			evt = LoginLogConvertor.toDomainEvent(request, authA, null);
			return authA;
		}
		catch (GlobalException gex) {
			// 记录日志【登录失败】
			evt = LoginLogConvertor.toDomainEvent(request, authA, gex);
			throw gex;
		}
		finally {
			// 发布领域事件
			kafkaDomainEventPublisher.publish(MqTopic.LOGIN_LOG.getTopic(), evt);
		}
	}

	public Authentication authentication(AuthA authA) {
		return UserConvertor.toOAuth2Authentication(authA);
	}

}
