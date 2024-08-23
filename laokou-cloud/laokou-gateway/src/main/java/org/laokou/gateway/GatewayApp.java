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

package org.laokou.gateway;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.SneakyThrows;
import org.laokou.common.redis.annotation.EnableReactiveRedisRepository;
import org.laokou.gateway.annotation.EnableAuth;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import reactor.core.publisher.Hooks;

import java.net.InetAddress;

/**
 * 网关服务启动类. exposeProxy=true => 使用Cglib代理，在切面中暴露代理对象，进行方法增强
 *
 * @author laokou
 */
@EnableAuth
@EnableDiscoveryClient
@EnableEncryptableProperties
@EnableConfigurationProperties
@EnableReactiveRedisRepository
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication(scanBasePackages = "org.laokou",
	exclude = {RedisReactiveAutoConfiguration.class, ReactiveUserDetailsServiceAutoConfiguration.class})
public class GatewayApp {

	/**
	 * 启动项目.
	 */
	@SneakyThrows
	public static void main(String[] args) {
		// @formatter:off
		System.setProperty("ip", InetAddress.getLocalHost().getHostAddress());
		// 配置关闭nacos日志，因为nacos的log4j2导致本项目的日志不输出的问题
		System.setProperty("nacos.logging.default.config.enabled", "false");
		// -Dnacos.remote.client.rpc.tls.enable=true
		// -Dnacos.remote.client.rpc.tls.mutualAuth=true
		// -Dnacos.remote.client.rpc.tls.certChainFile=nacos-client-cert.pem
		// -Dnacos.remote.client.rpc.tls.certPrivateKey=nacos-client-key.pem
		// -Dnacos.remote.client.rpc.tls.trustCollectionChainPath=nacos-ca-cert.pem
		// -Dnacos.remote.client.rpc.tls.certPrivateKeyPassword=laokou123
		// -Dserver.port=5555
		// @formatter:on
		// 开启reactor的上下文传递
		Hooks.enableAutomaticContextPropagation();
		new SpringApplicationBuilder(GatewayApp.class).web(WebApplicationType.REACTIVE).run(args);
	}

}
