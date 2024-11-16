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

package org.laokou.start;

import lombok.SneakyThrows;
import org.laokou.common.i18n.utils.SslUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.net.InetAddress;

/**
 * @author laokou
 */
@EnableConfigurationProperties
@EnableDiscoveryClient(autoRegister = false)
@SpringBootApplication(scanBasePackages = { "org.laokou" })
@MapperScan(basePackages = "org.laokou.infrastructure.gatewayimpl.database")
public class ShardingTestApp {

	/// ```properties
	/// -Dnacos.remote.client.rpc.tls.enable=true
	/// -Dnacos.remote.client.rpc.tls.mutualAuth=true
	/// -Dnacos.remote.client.rpc.tls.certChainFile=nacos-client-cert.pem
	/// -Dnacos.remote.client.rpc.tls.certPrivateKey=nacos-client-key.pem
	/// -Dnacos.remote.client.rpc.tls.trustCollectionChainPath=nacos-ca-cert.pem
	/// -Dnacos.remote.client.rpc.tls.certPrivateKeyPassword=laokou123
	/// ```
	@SneakyThrows
	public static void main(String[] args) {
		// 忽略SSL认证
		SslUtil.ignoreSSLTrust();
		System.setProperty("address", String.format("%s:%s", InetAddress.getLocalHost().getHostAddress(), System.getProperty("server.port", "9033")));
		new SpringApplicationBuilder(ShardingTestApp.class).web(WebApplicationType.SERVLET).run(args);
	}

}
