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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.Authorization;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;

import static org.laokou.common.security.config.RedisOAuth2Authorization.REDIS_OAUTH2_AUTHORIZATION_KEY;

// @formatter:off
/**
 * 仿照 数据库表 oauth2_authorization
 * <a href="https://docs.spring.io/spring-data/redis/reference/redis/redis-repositories/indexes.html">二级索引</a>
 * <a href="https://www.jdon.com/57902.html">用法</a>.
 *
 * @author laokou
 */
// @formatter:on

@Data
@RedisHash(REDIS_OAUTH2_AUTHORIZATION_KEY)
@Schema(name = "RedisOAuth2Authorization", description = "Redis认证")
public class RedisOAuth2Authorization extends Authorization {

	@Schema(name = "REDIS_OAUTH2_AUTHORIZATION_KEY", description = "存入Redis的Hash键")
	protected static final String REDIS_OAUTH2_AUTHORIZATION_KEY = "OAuth2:Authorization";

	@Id
	@Schema(name = "id", description = "ID")
	private String id;

	@Schema(name = "registeredClientId", description = "客户端ID")
	private String registeredClientId;

	@Schema(name = "principalName", description = "认证用户名")
	private String principalName;

	@Schema(name = "authorizationGrantType", description = "认证类型")
	private String authorizationGrantType;

	@Schema(name = "authorizedScopes", description = "认证范围")
	private String authorizedScopes;

	@Schema(name = "attributes", description = "认证信息")
	private String attributes;

	@Indexed
	@Schema(name = "state", description = "认证状态")
	private String state;

	@Indexed
	@Schema(name = "authorizationCodeValue", description = "授权码")
	private String authorizationCodeValue;

	@Schema(name = "authorizationCodeIssuedAt", description = "授权码签发时间")
	private Instant authorizationCodeIssuedAt;

	@Schema(name = "authorizationCodeExpiresAt", description = "授权码过期时间")
	private Instant authorizationCodeExpiresAt;

	@Schema(name = "authorizationCodeMetadata", description = "授权码元数据")
	private String authorizationCodeMetadata;

	@Indexed
	@Schema(name = "accessTokenValue", description = "令牌")
	private String accessTokenValue;

	@Schema(name = "accessTokenIssuedAt", description = "令牌签发时间")
	private Instant accessTokenIssuedAt;

	@Schema(name = "accessTokenExpiresAt", description = "令牌过期时间")
	private Instant accessTokenExpiresAt;

	@Schema(name = "accessTokenMetadata", description = "令牌元数据")
	private String accessTokenMetadata;

	@Schema(name = "accessTokenType", description = "令牌类型")
	private String accessTokenType;

	@Schema(name = "accessTokenScopes", description = "令牌范围")
	private String accessTokenScopes;

	@Indexed
	@Schema(name = "oidcIdTokenValue", description = "OID")
	private String oidcIdTokenValue;

	@Schema(name = "oidcIdTokenIssuedAt", description = "OID签发时间")
	private Instant oidcIdTokenIssuedAt;

	@Schema(name = "oidcIdTokenExpiresAt", description = "OID过期时间")
	private Instant oidcIdTokenExpiresAt;

	@Schema(name = "oidcIdTokenMetadata", description = "OID元数据")
	private String oidcIdTokenMetadata;

	@Schema(name = "oidcIdTokenClaims", description = "OID属性")
	private String oidcIdTokenClaims;

	@Indexed
	@Schema(name = "refreshTokenValue", description = "刷新令牌")
	private String refreshTokenValue;

	@Schema(name = "refreshTokenIssuedAt", description = "刷新令牌签发时间")
	private Instant refreshTokenIssuedAt;

	@Schema(name = "refreshTokenExpiresAt", description = "刷新令牌过期时间")
	private Instant refreshTokenExpiresAt;

	@Schema(name = "refreshTokenMetadata", description = "刷新令牌元数据")
	private String refreshTokenMetadata;

	@Indexed
	@Schema(name = "userCodeValue", description = "用户码")
	private String userCodeValue;

	@Schema(name = "userCodeIssuedAt", description = "用户码签发时间")
	private Instant userCodeIssuedAt;

	@Schema(name = "userCodeExpiresAt", description = "用户码过期时间")
	private Instant userCodeExpiresAt;

	@Schema(name = "userCodeMetadata", description = "用户码元数据")
	private String userCodeMetadata;

	@Indexed
	@Schema(name = "deviceCodeValue", description = "设备码")
	private String deviceCodeValue;

	@Schema(name = "deviceCodeIssuedAt", description = "设备码签发时间")
	private Instant deviceCodeIssuedAt;

	@Schema(name = "deviceCodeExpiresAt", description = "设备码过期时间")
	private Instant deviceCodeExpiresAt;

	@Schema(name = "deviceCodeMetadata", description = "设备码元数据")
	private String deviceCodeMetadata;

	@TimeToLive
	@Schema(name = "ttl", description = "过期时间（秒）")
	private Long ttl;

}
