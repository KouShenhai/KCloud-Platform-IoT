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
import org.laokou.auth.convertor.UserConvertor;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.model.AuthA;
import org.laokou.common.context.util.UserExtDetails;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 用户认证.
 *
 * @author laokou
 */
@Slf4j
@Component
record UserDetailsServiceImpl(@NonNull OAuth2UsernamePasswordAuthentication oAuth2UsernamePasswordAuthentication,
		RedisUtils redisUtils, SystemSettingsProperties systemSettingsProperties) implements UserDetailsService {

	/**
	 * 获取用户信息.
	 * @param username 用户名
	 * @return 用户信息
	 */
	@NonNull
	@Override
	public UserDetails loadUserByUsername(@NonNull String username) {
		AuthA authA = oAuth2UsernamePasswordAuthentication.authentication(
				DomainFactory.createAuth().createAuthorizationCodeAuth(), RequestUtils.getHttpServletRequest());
		UserExtDetails userDetails = UserConvertor.toUserDetails(authA);
		redisUtils.set(RedisKeyUtils.getUserDetailKey(username), userDetails,
				systemSettingsProperties.getProfileExpire().toSeconds());
		return new User(userDetails.username(), userDetails.password(),
				AuthorityUtils.createAuthorityList(userDetails.permissions()));
	}

}
