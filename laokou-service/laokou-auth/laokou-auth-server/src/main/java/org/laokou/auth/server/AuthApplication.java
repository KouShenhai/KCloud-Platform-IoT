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
package org.laokou.auth.server;

import com.alibaba.nacos.common.tls.TlsSystemConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.RequiredArgsConstructor;
import org.laokou.common.dynamic.router.utils.RouterUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.laokou.common.core.constant.Constant.TRUE;

/**
 * 架构演变
 * 单机架构（两层架构）
 * 三层架构（集中式架构）
 * DDD分层架构(分布式微服务架构) > 表现层 应用层 领域层 基础层
 * @author laokou
 */
@SpringBootApplication(exclude = {OAuth2AuthorizationServerAutoConfiguration.class})
@EnableConfigurationProperties
@EnableAspectJAutoProxy
@EnableEncryptableProperties
@EnableAsync
@EnableDiscoveryClient
@RequiredArgsConstructor
public class AuthApplication implements CommandLineRunner {
    private final RouterUtil routerUtil;
    public static void main(String[] args) {
        // SpringSecurity 子线程读取父线程的上下文
        System.setProperty(SecurityContextHolder.SYSTEM_PROPERTY,SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        // https://github.com/alibaba/nacos/pull/3654
        // 请查看 HttpLoginProcessor
        System.setProperty(TlsSystemConfig.TLS_ENABLE, TRUE);
        System.setProperty(TlsSystemConfig.CLIENT_AUTH, TRUE);
        System.setProperty(TlsSystemConfig.CLIENT_TRUST_CERT, "tls/nacos.cer");
        new SpringApplicationBuilder(AuthApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        routerUtil.initRouter();
    }
}
