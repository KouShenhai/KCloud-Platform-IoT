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

package org.laokou.common.grpc.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.grpc.server.security.AuthenticationProcessInterceptor;
import org.springframework.grpc.server.security.GrpcSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 *
 * <a href=
 * "https://docs.springframework.org.cn/spring-security/reference/reactive/oauth2/resource-server/opaque-token.html">OAuth
 * 2.0 资源服务器不透明令牌</a>.
 * <p>
 * <a href="https://docs.spring.io/spring-grpc/reference/server.html">Spring gRPC 安全</a>
 * <p>
 * <a href=
 * "https://github.com/spring-projects/spring-grpc/tree/main/samples/grpc-oauth2">Spring
 * gRPC 安全例子</a>
 *
 * @author laokou
 */
@Configuration
@EnableMethodSecurity
class GrpcOAuth2Config {

	@Bean
	@GlobalServerInterceptor
	AuthenticationProcessInterceptor jwtAuthenticationProcessInterceptor(GrpcSecurity grpc) throws Exception {
		return grpc.authorizeRequests(requests -> requests.allRequests().authenticated())
			.oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()))
			.build();
	}

}
