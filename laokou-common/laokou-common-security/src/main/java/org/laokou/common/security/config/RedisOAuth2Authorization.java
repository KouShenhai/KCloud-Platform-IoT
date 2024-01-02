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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.laokou.common.i18n.dto.Authorization;

import java.time.Instant;

/**
 * 仿照 数据库表 oauth2_authorization
 *
 * @author laokou
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class RedisOAuth2Authorization extends Authorization {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 客户端ID
	 */
	private String registeredClientId;

	/**
	 * 认证用户名
	 */
	private String principalName;

	/**
	 * 认证类型
	 */
	private String authorizationGrantType;

	/**
	 * 认证范围
	 */
	private String authorizedScopes;

	/**
	 * 认证信息
	 */
	private String attributes;

	/**
	 * 认证状态
	 */
	private String state;

	/**
	 * 授权码-值
	 */
	private String authorizationCodeValue;

	/**
	 * 授权码-签发时间
	 */
	private Instant authorizationCodeIssuedAt;

	/**
	 * 授权码-过期时间
	 */
	private Instant authorizationCodeExpiresAt;

	/**
	 * 授权码-元数据
	 */
	private String authorizationCodeMetadata;

	/**
	 * 令牌-值
	 */
	private String accessTokenValue;

	/**
	 * 令牌-签发时间
	 */
	private Instant accessTokenIssuedAt;

	/**
	 * 令牌-过期时间
	 */
	private Instant accessTokenExpiresAt;

	/**
	 * 令牌-元数据
	 */
	private String accessTokenMetadata;

	/**
	 * 令牌-类型
	 */
	private String accessTokenType;

	/**
	 * 令牌-范围
	 */
	private String accessTokenScopes;

	/**
	 * OID-值
	 */
	private String oidcIdTokenValue;

	/**
	 * OID-签发时间
	 */
	private Instant oidcIdTokenIssuedAt;

	/**
	 * OID-过期时间
	 */
	private Instant oidcIdTokenExpiresAt;

	/**
	 * OID-元数据
	 */
	private String oidcIdTokenMetadata;

	/**
	 * OID-属性
	 */
	private String oidcIdTokenClaims;

	/**
	 * 刷新令牌-值
	 */
	private String refreshTokenValue;

	/**
	 * 刷新令牌-签发时间
	 */
	private Instant refreshTokenIssuedAt;

	/**
	 * 刷新令牌-过期时间
	 */
	private Instant refreshTokenExpiresAt;

	/**
	 * 刷新令牌-元数据
	 */
	private String refreshTokenMetadata;

	/**
	 * 用户码-值
	 */
	private String userCodeValue;

	/**
	 * 用户码-签发时间
	 */
	private Instant userCodeIssuedAt;

	/**
	 * 用户码-过期时间
	 */
	private Instant userCodeExpiresAt;

	/**
	 * 用户码-元数据
	 */
	private String userCodeMetadata;

	/**
	 * 设备码-值
	 */
	private String deviceCodeValue;

	/**
	 * 设备码-签发时间
	 */
	private Instant deviceCodeIssuedAt;

	/**
	 * 设备码-过期时间
	 */
	private Instant deviceCodeExpiresAt;

	/**
	 * 设备码-元数据
	 */
	private String deviceCodeMetadata;

}
