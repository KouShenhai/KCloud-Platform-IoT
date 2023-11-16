/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
package org.laokou.common.security.config;

import com.baomidou.dynamic.datasource.annotation.Master;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.i18n.common.StatusCode;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.jasypt.utils.AesUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.security.handler.OAuth2ExceptionHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.laokou.common.i18n.common.BizCode.ACCOUNT_FORCE_KILL;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	private final RedisUtil redisUtil;

	@Override
	@Master
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		String userKillKey = RedisKeyUtil.getUserKillKey(token);
		if (Objects.nonNull(redisUtil.get(userKillKey))) {
			throw OAuth2ExceptionHandler.getException(ACCOUNT_FORCE_KILL, MessageUtil.getMessage(ACCOUNT_FORCE_KILL));
		}
		// 用户相关数据，低命中率且数据庞大放redis稳妥，分布式集群需要通过redis实现数据共享
		String userInfoKey = RedisKeyUtil.getUserInfoKey(token);
		Object obj = redisUtil.get(userInfoKey);
		if (Objects.nonNull(obj)) {
			// 解密
			return decryptInfo((User) obj);
		}
		OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token,
				OAuth2TokenType.ACCESS_TOKEN);
		if (Objects.isNull(oAuth2Authorization)) {
			throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED,
					MessageUtil.getMessage(StatusCode.UNAUTHORIZED));
		}
		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = oAuth2Authorization.getAccessToken();
		if (Objects.isNull(accessToken) || !accessToken.isActive()) {
			throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED,
					MessageUtil.getMessage(StatusCode.UNAUTHORIZED));
		}
		Instant expiresAt = oAuth2Authorization.getAccessToken().getToken().getExpiresAt();
		Instant nowAt = Instant.now();
		long expireTime = ChronoUnit.SECONDS.between(nowAt, expiresAt);
		long minTime = 10;
		if (expireTime > minTime) {
			Object principal = ((UsernamePasswordAuthenticationToken) Objects
				.requireNonNull(oAuth2Authorization.getAttribute(Principal.class.getName()))).getPrincipal();
			User user = (User) principal;
			redisUtil.set(userInfoKey, user, expireTime);
			// 解密
			return decryptInfo(user);
		}
		throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED,
				MessageUtil.getMessage(StatusCode.UNAUTHORIZED));
	}

	/**
	 * 解密字段
	 * @param user 用户信息
	 * @return UserDetail
	 */
	private User decryptInfo(User user) {
		String username = user.getUsername();
		if (StringUtil.isNotEmpty(username)) {
			try {
				user.setUsername(AesUtil.decrypt(username));
			}
			catch (Exception e) {
				log.error("用户名解密失败，请使用AES加密");
			}
		}
		String mail = user.getMail();
		if (StringUtil.isNotEmpty(mail)) {
			try {
				user.setMail(AesUtil.decrypt(mail));
			}
			catch (Exception e) {
				log.error("邮箱解密失败，请使用AES加密");
			}
		}
		String mobile = user.getMobile();
		if (StringUtil.isNotEmpty(mail)) {
			try {
				user.setMobile(AesUtil.decrypt(mobile));
			}
			catch (Exception e) {
				log.error("手机号解密失败，请使用AES加密");
			}
		}
		// 写入当前线程
		UserContextHolder.set(convert(user));
		return user;
	}

	private UserContextHolder.User convert(User user) {
		UserContextHolder.User u = new UserContextHolder.User();
		u.setId(user.getId());
		u.setDeptPath(user.getDeptPath());
		u.setDeptId(user.getDeptId());
		u.setTenantId(user.getTenantId());
		return u;
	}

}
