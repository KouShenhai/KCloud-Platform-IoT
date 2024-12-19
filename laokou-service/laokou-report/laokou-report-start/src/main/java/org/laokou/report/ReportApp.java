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

package org.laokou.report;

import lombok.SneakyThrows;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.net.InetAddress;

/**
 * @author laokou
 */
@SpringBootApplication(scanBasePackages = "org.laokou")
@EnableConfigurationProperties
public class ReportApp {

	// @formatter:off
    /// ```properties
    /// -Dnacos.remote.client.rpc.tls.enable=true
    /// -Dnacos.remote.client.rpc.tls.mutualAuth=true
    /// -Dnacos.remote.client.rpc.tls.certChainFile=nacos-client-cert.pem
    /// -Dnacos.remote.client.rpc.tls.certPrivateKey=nacos-client-key.pem
    /// -Dnacos.remote.client.rpc.tls.trustCollectionChainPath=nacos-ca-cert.pem
    /// -Dnacos.remote.client.rpc.tls.certPrivateKeyPassword=laokou123
    /// -Dserver.port=10002
    /// ```
	@SneakyThrows
	public static void main(String[] args) {
		System.setProperty("address", String.format("%s:%s", InetAddress.getLocalHost().getHostAddress(), System.getProperty("server.port", "10002")));
		// 因为nacos的log4j2导致本项目的日志不输出的问题
		// 配置关闭nacos日志
		System.setProperty("nacos.logging.default.config.enabled", "false");
		// 关闭sentinel健康检查 https://github.com/alibaba/Sentinel/issues/1494
		System.setProperty("management.health.sentinel.enabled", "false");
		new SpringApplicationBuilder(ReportApp.class).web(WebApplicationType.SERVLET).run(args);
	}
    // @formatter:on

}
