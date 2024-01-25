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

package org.laokou.common.security.config;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.holder.UserContextHolder;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.security.handler.OAuth2ExceptionHandler;
import org.laokou.common.security.utils.UserDetail;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.laokou.common.i18n.common.BizCodes.ACCOUNT_FORCE_KILL;
import static org.laokou.common.i18n.common.OAuth2Constants.FULL;
import static org.laokou.common.i18n.common.StatusCodes.UNAUTHORIZED;

/**
 * @author laokou
 */
@Slf4j
@NonNullApi
@AutoConfiguration
@RequiredArgsConstructor
public class GlobalOpaqueTokenIntrospector implements OpaqueTokenIntrospector, HandlerInterceptor {

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	private final RedisUtil redisUtil;

	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
			String userKillKey = RedisKeyUtil.getUserKillKey(token);
			if (ObjectUtil.isNotNull(redisUtil.get(userKillKey))) {
				throw OAuth2ExceptionHandler.getException(ACCOUNT_FORCE_KILL,
						MessageUtil.getMessage(ACCOUNT_FORCE_KILL));
			}
			// 用户相关数据，低命中率且数据庞大放redis稳妥，分布式集群需要通过redis实现数据共享
			String userInfoKey = RedisKeyUtil.getUserInfoKey(token);
			Object obj = redisUtil.get(userInfoKey);
			if (ObjectUtil.isNotNull(obj)) {
				// 解密
				return decryptInfo((UserDetail) obj);
			}
			OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token,
					new OAuth2TokenType(FULL));
			if (ObjectUtil.isNull(authorization)) {
				throw OAuth2ExceptionHandler.getException(UNAUTHORIZED, MessageUtil.getMessage(UNAUTHORIZED));
			}
			OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
			Instant expiresAt = accessToken.getToken().getExpiresAt();
			Instant nowAt = Instant.now();
			long expireTime = ChronoUnit.SECONDS.between(nowAt, expiresAt);
			// 5秒后过期
			long minTime = 5;
			if (expireTime > minTime) {
				Object principal = ((UsernamePasswordAuthenticationToken) Objects
					.requireNonNull(authorization.getAttribute(Principal.class.getName()))).getPrincipal();
				UserDetail userDetail = (UserDetail) principal;
				redisUtil.set(userInfoKey, userDetail, expireTime - 1);
				// 解密
				return decryptInfo(userDetail);
			}
			throw OAuth2ExceptionHandler.getException(UNAUTHORIZED, MessageUtil.getMessage(UNAUTHORIZED));
	}

	/**
	 * 解密字段.
	 * @param userDetail 用户信息
	 * @return UserDetail
	 */
	private UserDetail decryptInfo(UserDetail userDetail) {
		String mail = userDetail.getMail();
		if (StringUtil.isNotEmpty(mail)) {
			try {
				userDetail.setMail(AesUtil.decrypt(mail));
			}
			catch (Exception e) {
				log.error("邮箱解密失败，请使用AES加密");
			}
		}
		String mobile = userDetail.getMobile();
		if (StringUtil.isNotEmpty(mail)) {
			try {
				userDetail.setMobile(AesUtil.decrypt(mobile));
			}
			catch (Exception e) {
				log.error("手机号解密失败，请使用AES加密");
			}
		}
		// 写入当前线程
		UserContextHolder.set(convert(userDetail));
		return userDetail;
	}

	private UserContextHolder.User convert(UserDetail userDetail) {
		return UserContextHolder.User.builder()
			.id(userDetail.getId())
			.deptId(userDetail.getDeptId())
			.deptPath(userDetail.getDeptPath())
			.tenantId(userDetail.getTenantId())
			.sourceName(userDetail.getSourceName())
			.build();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
		UserContextHolder.clear();
	}
}
