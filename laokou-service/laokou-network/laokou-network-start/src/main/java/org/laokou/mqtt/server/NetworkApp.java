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

package org.laokou.mqtt.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.pulsar.annotation.EnablePulsar;
import org.springframework.util.StopWatch;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author laokou
 */
@Slf4j
@EnablePulsar
@RequiredArgsConstructor
@EnableDiscoveryClient
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = "org.laokou")
class NetworkApp {

	static void main(String[] args) throws UnknownHostException {
		StopWatch stopWatch = new StopWatch("Network应用程序");
		stopWatch.start();
		String host = InetAddress.getLocalHost().getHostAddress();
		System.setProperty("address", String.format("%s:%s", host, System.getProperty("server.port", "9995")));
		System.setProperty("host", host);
		new SpringApplicationBuilder(NetworkApp.class).web(WebApplicationType.SERVLET).run(args);
		stopWatch.stop();
		log.info("{}", stopWatch.prettyPrint());
	}

}
