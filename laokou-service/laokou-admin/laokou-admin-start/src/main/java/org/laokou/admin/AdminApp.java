/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin;

import com.aizuda.snailjob.client.starter.EnableSnailJob;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.SneakyThrows;
import org.laokou.common.core.annotation.EnableTaskExecutor;
import org.laokou.common.core.annotation.EnableWarmUp;
import org.laokou.common.i18n.utils.SslUtil;
import org.laokou.common.nacos.annotation.EnableRouter;
import org.laokou.common.nacos.filter.ShutdownFilter;
import org.laokou.common.openfeign.annotation.EnableOAuth2Feign;
import org.laokou.common.redis.annotation.EnableRedisRepository;
import org.laokou.common.secret.annotation.EnableApiSecret;
import org.laokou.common.security.annotation.EnableSecurity;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.InetAddress;

/**
 * 系统服务启动类. exposeProxy=true => 使用Cglib代理，在切面中暴露代理对象，进行方法增强
 *
 * @author laokou
 */
@EnableWarmUp
@EnableRouter
@EnableSecurity
@EnableSnailJob
@EnableApiSecret
@EnableOAuth2Feign
@EnableTaskExecutor
@EnableRedisRepository
@EnableDiscoveryClient
@EnableEncryptableProperties
@EnableConfigurationProperties
@EnableAspectJAutoProxy(exposeProxy = true)
@ServletComponentScan(basePackageClasses = { ShutdownFilter.class })
@SpringBootApplication(exclude = { SecurityFilterAutoConfiguration.class }, scanBasePackages = "org.laokou")
public class AdminApp {

	// @formatter:off
    /// ```properties
    /// -Dnacos.remote.client.rpc.tls.enable=true
    /// -Dnacos.remote.client.rpc.tls.mutualAuth=true
    /// -Dnacos.remote.client.rpc.tls.certChainFile=nacos-client-cert.pem
    /// -Dnacos.remote.client.rpc.tls.certPrivateKey=nacos-client-key.pem
    /// -Dnacos.remote.client.rpc.tls.trustCollectionChainPath=nacos-ca-cert.pem
    /// -Dnacos.remote.client.rpc.tls.certPrivateKeyPassword=laokou123
    /// -Dcsp.sentinel.api.port=8723
    /// -Dserver.port=9990
    /// ```
    /// ```properties
    /// client_id => 95TxSsTPFA3tF12TBSMmUVK0da
    /// client_secret => FpHwIfw4wY92dO
    /// ```
	@SneakyThrows
	public static void main(String[] args) {
		System.setProperty("ip", InetAddress.getLocalHost().getHostAddress());
		// SpringSecurity 子线程读取父线程的上下文
		System.setProperty(SecurityContextHolder.SYSTEM_PROPERTY, SecurityContextHolder.TTL_MODE_INHERITABLETHREADLOCAL);
		// 配置关闭nacos日志，因为nacos的log4j2导致本项目的日志不输出的问题
		System.setProperty("nacos.logging.default.config.enabled", "false");
		// 忽略SSL认证
		SslUtil.ignoreSSLTrust();
		new SpringApplicationBuilder(AdminApp.class).web(WebApplicationType.SERVLET).run(args);
	}
    // @formatter:on

}
