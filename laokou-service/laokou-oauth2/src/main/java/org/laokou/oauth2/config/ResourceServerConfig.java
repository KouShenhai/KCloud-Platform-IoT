/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.oauth2.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
/**
 * 资源配置
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/4/16 0016 下午 12:50
 */
@Configuration()
@EnableResourceServer()
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

     @Override
     public void configure(HttpSecurity http) throws Exception {
         http.requestMatchers()
                 .antMatchers("/oauth2/userInfo")
                 .and()
                 .authorizeRequests().antMatchers().authenticated()
                 .and()
                 .authorizeRequests().antMatchers("/oauth2/userInfo")
                 .access("#oauth2.hasScope('all')");
     }
}
