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

package org.laokou.common.nacos;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.nacos.util.ConfigUtils;
import org.laokou.common.testcontainers.container.NacosContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.util.DigestUtils;

import java.time.Duration;
import java.util.Properties;

/**
 * @author laokou
 */
class ConfigUtilsTest {

	private ConfigService configService;

	static final NacosContainer nacos = new NacosContainer(DockerImageNames.nacos("v3.1.0"), 38848, 39848);

	@BeforeAll
	static void beforeAll() {
		nacos.start();
	}

	@AfterAll
	static void afterAll() {
		nacos.stop();
	}

	@BeforeEach
	void setUp() throws NacosException {
		Properties properties = new Properties();
		properties.setProperty(PropertyKeyConst.USERNAME, "nacos");
		properties.setProperty(PropertyKeyConst.NAMESPACE, "nacos");
		properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacos.getServerAddr());
		properties.setProperty(PropertyKeyConst.NAMESPACE, "public");
		configService = ConfigUtils.createConfigService(properties);
		Assertions.assertThat(configService.getServerStatus()).isEqualTo("UP");
		configService = ConfigUtils.createConfigService(nacos.getServerAddr());
		Assertions.assertThat(configService.getServerStatus()).isEqualTo("UP");
	}

	@Test
	void test_config() throws NacosException, InterruptedException {
		Assertions.assertThat(configService.publishConfig("test.yaml", "DEFAULT", "test: 123")).isTrue();
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(configService.getConfig("test.yaml", "DEFAULT", 5000)).isEqualTo("test: 123");

		Assertions.assertThat(configService.publishConfig("test.yaml", "DEFAULT", "test: 456", "yaml")).isTrue();
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(configService.getConfig("test.yaml", "DEFAULT", 5000)).isEqualTo("test: 456");

		Assertions.assertThat(configService.publishConfig("test.yaml", "DEFAULT", "test: 123")).isTrue();
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(configService.getConfig("test.yaml", "DEFAULT", 5000)).isEqualTo("test: 123");

		String md5 = DigestUtils.md5DigestAsHex("test: 123".getBytes());
		Assertions.assertThat(md5).isEqualTo("5e76b5e94b54e1372f8b452ef64dc55c");
		Assertions.assertThat(configService.publishConfigCas("test.yaml", "DEFAULT", "test: 456", md5)).isTrue();
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(configService.getConfig("test.yaml", "DEFAULT", 5000)).isEqualTo("test: 456");

		md5 = DigestUtils.md5DigestAsHex("test: 456".getBytes());
		Assertions.assertThat(md5).isEqualTo("76e2eabbf24a8c90dc3b4372c20a72cf");
		Assertions.assertThat(configService.publishConfigCas("test.yaml", "DEFAULT", "test: 789", md5, "yaml"))
			.isTrue();
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(configService.getConfig("test.yaml", "DEFAULT", 5000)).isEqualTo("test: 789");

		Assertions.assertThat(configService.publishConfig("test1.yaml", "DEFAULT", "test: 123")).isTrue();
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(configService.getConfig("test1.yaml", "DEFAULT", 5000)).isEqualTo("test: 123");

		Assertions.assertThat(configService.removeConfig("test1.yaml", "DEFAULT")).isTrue();
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(configService.getConfig("test1.yaml", "DEFAULT", 5000)).isNull();
	}

}
