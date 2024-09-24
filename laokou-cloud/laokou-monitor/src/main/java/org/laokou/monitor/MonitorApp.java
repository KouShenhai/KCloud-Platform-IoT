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

package org.laokou.monitor;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.SneakyThrows;
import org.laokou.common.i18n.utils.SslUtil;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.net.InetAddress;

/**
 * 监控启动类.
 *
 * @author laokou
 */
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
@EnableEncryptableProperties
public class MonitorApp {

	// @formatter:off
    /// ```properties
    /// -Dnacos.remote.client.rpc.tls.enable=true
    /// -Dnacos.remote.client.rpc.tls.mutualAuth=true
    /// -Dnacos.remote.client.rpc.tls.certChainFile=nacos-client-cert.pem
    /// -Dnacos.remote.client.rpc.tls.certPrivateKey=nacos-client-key.pem
    /// -Dnacos.remote.client.rpc.tls.trustCollectionChainPath=nacos-ca-cert.pem
    /// -Dnacos.remote.client.rpc.tls.certPrivateKeyPassword=laokou123
    /// -Dserver.port=5000
    /// ```
	@SneakyThrows
	public static void main(String[] args) {
		System.setProperty("ip", InetAddress.getLocalHost().getHostAddress());
		// 配置关闭nacos日志，因为nacos的log4j2导致本项目的日志不输出的问题
		System.setProperty("nacos.logging.default.config.enabled", "false");
		// 忽略SSL认证
		SslUtil.ignoreSSLTrust();
		new SpringApplicationBuilder(MonitorApp.class).web(WebApplicationType.REACTIVE).run(args);
	}
    // @formatter:on

}
