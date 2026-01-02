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

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.common.context.util.User;
import org.laokou.common.context.util.UserConvertor;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 用户认证.
 *
 * @author laokou
 */
@Slf4j
@Component
record UserDetailsServiceImpl(
		@NonNull OAuth2AuthenticationProcessor authenticationProcessor) implements UserDetailsService {

	/**
	 * 获取用户信息.
	 * @param username 用户名
	 * @return 用户信息
	 * @throws UsernameNotFoundException 异常
	 */
	@NonNull
	@Override
	public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
		try {
			Object principal = authenticationProcessor
				.authentication(DomainFactory.getAuth().createUserVByAuthorizationCodeAuth(),
						RequestUtils.getHttpServletRequest())
				.getPrincipal();
			if (principal instanceof User user) {
				return UserConvertor.toUserDetails(user);
			}
			throw new BizException("B_OAuth2_UserNotExist", "用户不存在");
		}
		catch (GlobalException e) {
			throw new UsernameNotFoundException(e.getMsg(), e);
		}
		catch (Exception e) {
			log.error("用户认证失败，错误信息：{}", e.getMessage(), e);
			throw new BizException("B_OAuth2_UserAuthFailed", "用户认证失败", e);
		}
	}

}
