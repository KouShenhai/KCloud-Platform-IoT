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

package org.laokou.monitor;

import com.alibaba.nacos.common.tls.TlsSystemConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.SneakyThrows;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.util.ResourceUtils;

import java.net.InetAddress;

import static org.laokou.common.i18n.common.NetworkConstant.IP;
import static org.laokou.common.i18n.common.StringConstant.TRUE;

/**
 * 监控启动类.
 *
 * @author laokou
 */
@SpringBootApplication
@EnableAdminServer
@EnableEncryptableProperties
@EnableDiscoveryClient
public class MonitorApp {

	@SneakyThrows
	public static void main(String[] args) {
		System.setProperty(IP, InetAddress.getLocalHost().getHostAddress());
		// 因为nacos的log4j2导致本项目的日志不输出的问题
		// 配置关闭nacos日志
		System.setProperty("nacos.logging.default.config.enabled", "false");
		System.setProperty(TlsSystemConfig.TLS_ENABLE, TRUE);
		System.setProperty(TlsSystemConfig.CLIENT_AUTH, TRUE);
		System.setProperty(TlsSystemConfig.CLIENT_TRUST_CERT,
				ResourceUtils.getFile("classpath:nacos.cer").getCanonicalPath());
		new SpringApplicationBuilder(MonitorApp.class).web(WebApplicationType.REACTIVE).run(args);
	}

}
