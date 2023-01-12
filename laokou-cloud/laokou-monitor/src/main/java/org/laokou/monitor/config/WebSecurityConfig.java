/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
/**
 * @author laokou
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AdminServerProperties adminServerProperties;

    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminServerProperties.path("/"));
        return http.authorizeHttpRequests()
                .requestMatchers(adminServerProperties.path("/assets/**")
                        , adminServerProperties.path("/variables.css")
                        , adminServerProperties.path("/actuator/**")
                        , adminServerProperties.path("/instances/**")
                        , adminServerProperties.path("/login")).permitAll()
                .dispatcherTypeMatchers(DispatcherType.ASYNC)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage(adminServerProperties.path("/login"))
                .successHandler(successHandler)
                .and()
                .logout()
                .logoutUrl(adminServerProperties.path("/logout"))
                .and()
                .httpBasic(Customizer.withDefaults())
                .csrf().disable()
                .build();
    }
}
