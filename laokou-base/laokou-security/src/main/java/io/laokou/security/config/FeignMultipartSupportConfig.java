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
package io.laokou.security.config;
import feign.RequestInterceptor;
import io.laokou.common.constant.Constant;
import io.laokou.common.utils.HttpContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.http.HttpServletRequest;
/**
 * @author  Kou Shenhai
 */
@Configuration
public class FeignMultipartSupportConfig {

    @Bean
    public feign.Logger.Level multipartLoggerLevel() {
        return feign.Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor getRequestInterceptor() {
        return requestTemplate -> {
            HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
            requestTemplate.header(Constant.AUTHORIZATION_HEAD,request.getHeader(Constant.AUTHORIZATION_HEAD));
        };
    }

}
