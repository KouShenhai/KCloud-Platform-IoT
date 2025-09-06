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

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.nacos.util.ConfigUtils;
import org.springframework.util.DigestUtils;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
class ConfigUtilsTest {

	// @formatter:off
	private ConfigUtils configUtils;

	private NacosConfigProperties nacosConfigProperties;

	static NamingUtilsTest.NacosContainer nacos = new NamingUtilsTest.NacosContainer();

	@BeforeAll
	static void beforeAll() {
		nacos.start();
	}

	@AfterAll
	static void afterAll() {
		nacos.stop();
	}

	@BeforeEach
	void setUp() {
		nacosConfigProperties = new NacosConfigProperties();
		nacosConfigProperties.setServerAddr(nacos.getServerAddr());
		nacosConfigProperties.setNamespace("public");
		nacosConfigProperties.setUsername("nacos");
		nacosConfigProperties.setPassword("nacos");
		nacosConfigProperties.setGroup("DEFAULT_GROUP");
		assertThat(nacosConfigProperties.assembleConfigServiceProperties()).isNotNull();

		NacosConfigManager nacosConfigManager = new NacosConfigManager(nacosConfigProperties);
		assertThat(nacosConfigManager).isNotNull();

		configUtils = new ConfigUtils(nacosConfigManager);
		assertThat(configUtils).isNotNull();
	}

	@Test
	void test_config() throws NacosException, InterruptedException {
		Thread.sleep(Duration.ofSeconds(15));
		assertThat( configUtils.getGroup()).isEqualTo("DEFAULT_GROUP");
		assertThat(configUtils.getServerStatus()).isEqualTo("UP");
		ConfigService configService = ConfigUtils.createConfigService(nacosConfigProperties.getServerAddr());
		assertThat(configService).isNotNull();
		assertThat(configService.getServerStatus()).isEqualTo("UP");

		configService = ConfigUtils.createConfigService(nacosConfigProperties.assembleConfigServiceProperties());
		assertThat(configService).isNotNull();
		assertThat(configService.getServerStatus()).isEqualTo("UP");
		assertThat(configUtils.publishConfig("test.yaml", nacosConfigProperties.getGroup(), "test: 123")).isTrue();
		Thread.sleep(2000);
		assertThat(configUtils.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000)).isEqualTo("test: 123");

		assertThat(configUtils.publishConfig("test.yaml", nacosConfigProperties.getGroup(), "test: 456", "yaml")).isTrue();
		assertThat(configUtils.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000)).isEqualTo("test: 456");

		assertThat(configUtils.publishConfig("test.yaml", nacosConfigProperties.getGroup(), "test: 123")).isTrue();
		assertThat(configUtils.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000)).isEqualTo("test: 123");

		String md5 = DigestUtils.md5DigestAsHex("test: 123".getBytes());
		assertThat(md5).isEqualTo("5e76b5e94b54e1372f8b452ef64dc55c");
		assertThat(configUtils.publishConfigCas("test.yaml", nacosConfigProperties.getGroup(), "test: 456", md5)).isTrue();
		assertThat(configUtils.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000)).isEqualTo("test: 456");

		String test = configUtils.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000, new Listener() {
			@Override
			public Executor getExecutor() {
				return Executors.newSingleThreadExecutor();
			}

			@Override
			public void receiveConfigInfo(String s) {
				assertThat(s).isNotBlank().contains("test");
			}
		});
		assertThat(test).isEqualTo("test: 456");

		md5 = DigestUtils.md5DigestAsHex("test: 456".getBytes());
		assertThat(md5).isEqualTo("76e2eabbf24a8c90dc3b4372c20a72cf");
		assertThat(configUtils.publishConfigCas("test.yaml", nacosConfigProperties.getGroup(), "test: 789", md5, "yaml")).isTrue();
		assertThat(configUtils.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000)).isEqualTo("test: 789");

		assertThat(configUtils.publishConfig("test1.yaml", nacosConfigProperties.getGroup(), "test: 123")).isTrue();
		Thread.sleep(2000);
		assertThat( configUtils.getConfig("test1.yaml", nacosConfigProperties.getGroup(), 5000)).isEqualTo("test: 123");

		assertThat(configUtils.removeConfig("test1.yaml", nacosConfigProperties.getGroup())).isTrue();
		Thread.sleep(2000);
		assertThat(configUtils.getConfig("test1.yaml", nacosConfigProperties.getGroup(), 5000)).isNull();

		configUtils.removeListener("test.yaml", nacosConfigProperties.getGroup(), new Listener() {
			@Override
			public Executor getExecutor() {
				return Executors.newSingleThreadExecutor();
			}

			@Override
			public void receiveConfigInfo(String s) {
				assertThat(s).isNotBlank().contains("test");
			}
		});
		configUtils.addListener("test.yaml", nacosConfigProperties.getGroup(), new Listener() {
			@Override
			public Executor getExecutor() {
				return Executors.newSingleThreadExecutor();
			}

			@Override
			public void receiveConfigInfo(String s) {
				assertThat(s).isNotBlank().contains("test");
			}
		});
	}

	// @formatter:on

}
