/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.netty.annotation.EnableWebSocketServer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 系统服务启动类. exposeProxy=true => 使用Cglib代理，在切面中暴露代理对象，进行方法增强
 *
 * @author laokou
 */
@Slf4j
@EnableWebSocketServer
@EnableEncryptableProperties
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = { "org.laokou" })
public class WsSampleApp {

	public static void main(String[] args) throws UnknownHostException {
		System.setProperty("address", String.format("%s:%s", InetAddress.getLocalHost().getHostAddress(),
				System.getProperty("server.port", "9032")));
		new SpringApplicationBuilder(WsSampleApp.class).web(WebApplicationType.SERVLET).run(args);
		log.info("访问地址：https://127.0.0.1:9032");
	}

}
