/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.ability.AuthDomainService;
import org.laokou.auth.convertor.LoginLogConvertor;
import org.laokou.auth.convertor.UserConvertor;
import org.laokou.auth.extensionpoint.AuthValidatorExtPt;
import org.laokou.auth.model.AuthA;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.security.utils.UserDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import static org.laokou.auth.model.AuthA.USE_CASE_AUTH;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.ERROR_URL;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.getException;

/**
 * @author laokou
 */
@RequiredArgsConstructor
@Component("authProvider")
public class OAuth2AuthenticationProvider {

	private final AuthDomainService authDomainService;

	private final DomainEventPublisher rocketMQDomainEventPublisher;

	private final ExtensionExecutor extensionExecutor;

	public UsernamePasswordAuthenticationToken authentication(AuthA auth) {
		try {
			// 校验
			extensionExecutor.executeVoid(AuthValidatorExtPt.class,
				BizScenario.valueOf(auth.getGrantType().getCode(), USE_CASE_AUTH, SCENARIO),
				extension -> extension.validate(auth));
			// 认证
			authDomainService.auth(auth);
			// 转换
			UserDetail userDetail = UserConvertor.to(auth);
			return new UsernamePasswordAuthenticationToken(userDetail, userDetail.getUsername(),
				userDetail.getAuthorities());
		} catch (AuthException | SystemException e) {
			throw getException(e.getCode(), e.getMsg(), ERROR_URL);
		} finally {
			// 清除数据源上下文
			DynamicDataSourceContextHolder.clear();
			if (auth.isHasLog()) {
				// 发布登录事件
				rocketMQDomainEventPublisher.publish(LoginLogConvertor.toEvent(auth));
			}
		}
	}

}
