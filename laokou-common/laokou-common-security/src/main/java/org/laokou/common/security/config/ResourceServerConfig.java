/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.security.config;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.client.exception.ForbiddenExceptionHandler;
import org.laokou.auth.client.exception.InvalidAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author laokou
 */
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class ResourceServerConfig {

    private final ForbiddenExceptionHandler forbiddenExceptionHandler;
    private final InvalidAuthenticationEntryPoint invalidAuthenticationEntryPoint;
    private final CustomOpaqueTokenIntrospector customOpaqueTokenIntrospector;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1000)
    SecurityFilterChain resourceFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement()
                // 基于token，关闭session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests().requestMatchers(
                        "/v3/api-docs/**"
                        , "/swagger-ui.html"
                        , "/swagger-ui/**"
                        , "/actuator/**").permitAll()
                .and()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                // https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/opaque-token.html
                // 大多数资源服务器支持都收集在 中。 但是，除非提供自定义的 OpaqueTokenIntrospector，否则资源服务器将回退到 NimbusOpaqueTokenIntrospector
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(token -> token.introspector(customOpaqueTokenIntrospector))
                        .accessDeniedHandler(forbiddenExceptionHandler)
                        .authenticationEntryPoint(invalidAuthenticationEntryPoint))
                .build();
    }

}
