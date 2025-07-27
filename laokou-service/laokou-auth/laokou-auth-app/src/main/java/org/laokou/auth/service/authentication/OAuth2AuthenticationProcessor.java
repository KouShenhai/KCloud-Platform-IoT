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

package org.laokou.auth.service.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.convertor.LoginLogConvertor;
import org.laokou.auth.convertor.UserConvertor;
import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.MqEnum;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.mybatisplus.util.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * 认证授权处理器.
 *
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
@Component("authProcessor")
final class OAuth2AuthenticationProcessor {

	private final DomainService domainService;

	private final DomainEventPublisher kafkaDomainEventPublisher;

	public UsernamePasswordAuthenticationToken authenticationToken(AuthA authA, HttpServletRequest request)
			throws Exception {
		LoginEvent evt = null;
		try {
			// 认证授权
			domainService.auth(authA);
			// 记录日志【登录成功】
			evt = LoginLogConvertor.toDomainEvent(request, authA, null);
			// 登录成功，转换成用户对象【业务】
			UserDetails userDetails = UserConvertor.toUserDetails(authA);
			// 认证成功，转换成认证对象【系统】
			return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getUsername(),
					userDetails.getAuthorities());
		}
		catch (GlobalException e) {
			// 记录日志【业务异常】
			if (e instanceof BizException ex) {
				evt = LoginLogConvertor.toDomainEvent(request, authA, ex);
			}
			throw e;
		}
		catch (Exception e) {
			log.error("未知错误，错误信息：{}", e.getMessage(), e);
			throw new SystemException("S_UnKnow_Error", e.getMessage(), e);
		}
		finally {
			// 发布领域事件
			if (ObjectUtils.isNotNull(evt)) {
				kafkaDomainEventPublisher.publish(MqEnum.LOGIN_LOG.getTopic(), evt);
			}
		}
	}

}
