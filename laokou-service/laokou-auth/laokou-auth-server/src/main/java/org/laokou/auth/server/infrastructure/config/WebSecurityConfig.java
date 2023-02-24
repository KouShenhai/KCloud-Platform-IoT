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

package org.laokou.auth.server.infrastructure.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
/**
 * @author laokou
 */
@Configuration
public class WebSecurityConfig {

    /**
     * 不拦截拦截静态资源
     * 如果您不想要警告消息并且需要性能优化，则可以为静态资源引入第二个过滤器链
     * https://github.com/spring-projects/spring-security/issues/10938
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests().requestMatchers(
                 "/v3/api-docs/**"
                        , "/swagger-ui.html"
                        , "/swagger-ui/**"
                        , "/oauth2/captcha"
                        , "/oauth2/logout"
                        , "/oauth2/tenant"
                        , "/actuator/**")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                // 自定义登录页面
                // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
                // 登录页面 -> DefaultLoginPageGeneratingFilter
                .formLogin(Customizer.withDefaults())
                .logout()
                // 清除session
                .invalidateHttpSession(true)
                .and()
                .build();
    }

}
