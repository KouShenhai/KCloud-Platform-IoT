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
package org.laokou.common.security.config.auto;
import org.laokou.auth.client.handler.ForbiddenExceptionHandler;
import org.laokou.auth.client.handler.InvalidAuthenticationEntryPoint;
import org.laokou.common.security.config.CustomOpaqueTokenIntrospector;
import org.laokou.common.security.config.OAuth2ResourceServerProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author laokou
 */
@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@AutoConfigureAfter({OAuth2AuthorizationAutoConfig.class})
@Import(value = { CustomOpaqueTokenIntrospector.class
        , ForbiddenExceptionHandler.class
        , OAuth2ResourceServerProperties.class
        , InvalidAuthenticationEntryPoint.class
})
@ConditionalOnProperty(havingValue = "true",matchIfMissing = true,prefix = OAuth2ResourceServerProperties.PREFIX,name = "enabled")
public class OAuth2ResourceServerAutoConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1000)
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    SecurityFilterChain resourceFilterChain(CustomOpaqueTokenIntrospector customOpaqueTokenIntrospector
            , InvalidAuthenticationEntryPoint invalidAuthenticationEntryPoint
            , ForbiddenExceptionHandler forbiddenExceptionHandler
            , OAuth2ResourceServerProperties properties
            , HttpSecurity http) throws Exception {
        Set<String> patterns = Optional.ofNullable(properties.getRequestMatcher().getPatterns()).orElseGet(HashSet::new);
        return http.csrf().disable()
                .cors().disable()
                // 基于token，关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests().requestMatchers(patterns.toArray(String[]::new)).permitAll()
                .and().authorizeHttpRequests()
                .anyRequest().authenticated()
                // https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/opaque-token.html
                // 除非提供自定义的 OpaqueTokenIntrospector，否则资源服务器将回退到 NimbusOpaqueTokenIntrospector
                .and().oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(token -> token.introspector(customOpaqueTokenIntrospector))
                        .accessDeniedHandler(forbiddenExceptionHandler).authenticationEntryPoint(invalidAuthenticationEntryPoint))
                .build();
    }

}
