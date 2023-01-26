/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.laokou.common.mybatisplus.config.BaseDetaObjectHander;
import org.laokou.common.security.config.AuthorizationConfig;
import org.laokou.common.security.config.ResourceServerConfig;
import org.laokou.common.swagger.config.CorsConfig;
import org.laokou.common.swagger.config.OpenApiMvcConfig;
import org.laokou.common.swagger.exception.CustomExceptionHandler;
import org.laokou.redis.config.RedisSessionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
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
@SpringBootApplication(scanBasePackages = {"org.laokou.common.security","org.laokou.sentinel","org.laokou.common.swagger","org.laokou.common.core", "org.laokou.admin", "org.laokou.redis", "org.laokou.common.mybatisplus", "org.laokou.auth.client"})
@EnableDiscoveryClient
@EnableConfigurationProperties
@EnableAspectJAutoProxy
@EnableEncryptableProperties
@EnableFeignClients
@EnableAsync
@Import({RedisSessionConfig.class
        , CorsConfig.class
        , BaseDetaObjectHander.class
        , AuthorizationConfig.class
        , ResourceServerConfig.class
        , CustomExceptionHandler.class
        , OpenApiMvcConfig.class})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}