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

package org.laokou.mcp.client;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.annotation.EnableWarmUp;
import org.laokou.common.security.annotation.EnableSecurity;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StopWatch;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author laokou
 */
@Slf4j
@EnableSecurity
@EnableWarmUp
@EnableScheduling
@EnableEncryptableProperties
@EnableAspectJAutoProxy
@SpringBootApplication(exclude = { SecurityFilterAutoConfiguration.class }, scanBasePackages = "org.laokou")
@RequiredArgsConstructor
public class McpClientApp {

	// @formatter:off
	public static void main(String[] args) throws UnknownHostException {
		StopWatch stopWatch = new StopWatch("Mcp-Client应用程序");
		stopWatch.start();
		System.setProperty("address", String.format("%s:%s", InetAddress.getLocalHost().getHostAddress(), System.getProperty("server.port", "9096")));
		// 配置关闭nacos日志，因为nacos的log4j2导致本项目的日志不输出的问题
		System.setProperty("nacos.logging.default.config.enabled", "false");
		new SpringApplicationBuilder(McpClientApp.class).web(WebApplicationType.SERVLET).run(args);
		stopWatch.stop();
		log.info("{}", stopWatch.prettyPrint());
	}

}
