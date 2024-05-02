/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.logstash;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.SneakyThrows;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.net.InetAddress;

import static org.laokou.common.i18n.common.NetworkConstant.IP;

/**
 * @author laokou
 */
@SpringBootApplication(scanBasePackages = "org.laokou")
@EnableDiscoveryClient
@EnableConfigurationProperties
@EnableEncryptableProperties
public class LogtashApp {

	@SneakyThrows
	public static void main(String[] args) {
		// System.setProperty(TlsSystemConfig.TLS_ENABLE, TRUE);
		// System.setProperty(TlsSystemConfig.CLIENT_AUTH, TRUE);
		// System.setProperty(TlsSystemConfig.CLIENT_TRUST_CERT, "tls/nacos.cer");
		System.setProperty(IP, InetAddress.getLocalHost().getHostAddress());
		// 因为nacos的log4j2导致本项目的日志不输出的问题
		// 配置关闭nacos日志
		System.setProperty("nacos.logging.default.config.enabled", "false");
		new SpringApplicationBuilder(LogtashApp.class).web(WebApplicationType.SERVLET).run(args);
	}

}
