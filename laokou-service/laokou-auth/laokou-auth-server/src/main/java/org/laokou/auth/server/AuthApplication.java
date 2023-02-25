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
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.RequiredArgsConstructor;
import org.laokou.common.swagger.config.CorsConfig;
import org.laokou.dynamic.router.utils.RouterUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 架构演变
 * 单机架构（两层架构）
 * 三层架构（集中式架构）
 * DDD分层架构(分布式微服务架构) > 表现层 应用层 领域层 基础层
 * @author laokou
 */
@SpringBootApplication(scanBasePackages = {
         "org.laokou.common.i18n"
        ,"org.laokou.tenant"
        ,"org.laokou.common.log"
        ,"org.laokou.sentinel"
        ,"org.laokou.dynamic.router"
        ,"org.laokou.common.swagger"
        ,"org.laokou.common.mybatisplus"
        ,"org.laokou.common.core"
        ,"org.laokou.redis"
        ,"org.laokou.auth"})
@EnableConfigurationProperties
@EnableAspectJAutoProxy
@EnableEncryptableProperties
@EnableAsync
@EnableDiscoveryClient
@Import(CorsConfig.class)
@MapperScan(value = {"org.laokou.auth.server.domain.sys.repository.mapper"
        , "org.laokou.tenant.mapper"
        ,"org.laokou.common.log.mapper"})
@RequiredArgsConstructor
public class AuthApplication implements CommandLineRunner {

    private final RouterUtil routerUtil;

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        routerUtil.initRouter();
    }
}
