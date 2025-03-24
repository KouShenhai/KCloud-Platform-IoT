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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.nacos.utils.ConfigUtil;
import org.springframework.util.DigestUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author laokou
 */
class ConfigUtilTest {

	private ConfigUtil configUtil;

	private NacosConfigProperties nacosConfigProperties;

	@BeforeEach
	void setUp() {
		nacosConfigProperties = new NacosConfigProperties();
		nacosConfigProperties.setServerAddr("127.0.0.1:8848");
		nacosConfigProperties.setNamespace("public");
		nacosConfigProperties.setUsername("nacos");
		nacosConfigProperties.setPassword("nacos");
		nacosConfigProperties.setGroup("DEFAULT_GROUP");

		Assertions.assertNotNull(nacosConfigProperties);
		Assertions.assertEquals("public", nacosConfigProperties.getNamespace());
		Assertions.assertEquals("127.0.0.1:8848", nacosConfigProperties.getServerAddr());
		Assertions.assertEquals("nacos", nacosConfigProperties.getPassword());
		Assertions.assertEquals("nacos", nacosConfigProperties.getUsername());
		Assertions.assertEquals("DEFAULT_GROUP", nacosConfigProperties.getGroup());

		NacosConfigManager nacosConfigManager = new NacosConfigManager(nacosConfigProperties);
		Assertions.assertNotNull(nacosConfigManager);

		configUtil = new ConfigUtil(nacosConfigManager);
		Assertions.assertNotNull(configUtil);
	}

	@Test
	void testGetGroup() {
		Assertions.assertEquals("DEFAULT_GROUP", configUtil.getGroup());
	}

	@Test
	void testCreateConfigService() throws NacosException {
		ConfigService configService = ConfigUtil.createConfigService(nacosConfigProperties.getServerAddr());
		Assertions.assertNotNull(configService);
		Assertions.assertEquals("UP", configService.getServerStatus());

		configService = ConfigUtil.createConfigService(nacosConfigProperties.assembleConfigServiceProperties());
		Assertions.assertNotNull(configService);
		Assertions.assertEquals("UP", configService.getServerStatus());
	}

	@Test
	void testGetConfig() throws NacosException {
		String config = configUtil.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000);
		Assertions.assertNotNull(config);
		Assertions.assertTrue(config.contains("test"));
		config = configUtil.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000, new Listener() {
			@Override
			public Executor getExecutor() {
				return Executors.newSingleThreadExecutor();
			}

			@Override
			public void receiveConfigInfo(String s) {
				Assertions.assertNotNull(s);
				Assertions.assertTrue(s.contains("test"));
			}
		});
		Assertions.assertNotNull(config);
		Assertions.assertTrue(config.contains("test"));
	}

	@Test
	void testAddListener() throws NacosException {
		configUtil.addListener("test.yaml", nacosConfigProperties.getGroup(), new Listener() {
			@Override
			public Executor getExecutor() {
				return Executors.newSingleThreadExecutor();
			}

			@Override
			public void receiveConfigInfo(String s) {
				Assertions.assertNotNull(s);
				Assertions.assertTrue(s.contains("test"));
			}
		});
	}

	@Test
	void testRemoveListener() throws NacosException {
		configUtil.removeListener("test.yaml", nacosConfigProperties.getGroup(), new Listener() {
			@Override
			public Executor getExecutor() {
				return Executors.newSingleThreadExecutor();
			}

			@Override
			public void receiveConfigInfo(String s) {
				Assertions.assertNotNull(s);
				Assertions.assertTrue(s.contains("test"));
			}
		});
	}

	@Test
	void testPublishConfig() throws NacosException, InterruptedException {
		Assertions.assertTrue(configUtil.publishConfig("test.yaml", nacosConfigProperties.getGroup(), "test: 123"));
		Thread.sleep(100);
		Assertions.assertEquals("test: 123", configUtil.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000));

		Assertions
			.assertTrue(configUtil.publishConfig("test.yaml", nacosConfigProperties.getGroup(), "test: 456", "yaml"));
		Assertions.assertEquals("test: 456", configUtil.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000));

		Assertions.assertTrue(configUtil.publishConfig("test.yaml", nacosConfigProperties.getGroup(), "test: 123"));
		Assertions.assertEquals("test: 123", configUtil.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000));

		String md5 = DigestUtils.md5DigestAsHex("test: 123".getBytes());
		Assertions.assertEquals("5e76b5e94b54e1372f8b452ef64dc55c", md5);
		Assertions
			.assertTrue(configUtil.publishConfigCas("test.yaml", nacosConfigProperties.getGroup(), "test: 456", md5));
		Assertions.assertEquals("test: 456", configUtil.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000));

		md5 = DigestUtils.md5DigestAsHex("test: 456".getBytes());
		Assertions.assertEquals("76e2eabbf24a8c90dc3b4372c20a72cf", md5);
		Assertions.assertTrue(
				configUtil.publishConfigCas("test.yaml", nacosConfigProperties.getGroup(), "test: 789", md5, "yaml"));
		Assertions.assertEquals("test: 789", configUtil.getConfig("test.yaml", nacosConfigProperties.getGroup(), 5000));
	}

	@Test
	void testRemoveConfig() throws NacosException, InterruptedException {
		Assertions.assertTrue(configUtil.publishConfig("test1.yaml", nacosConfigProperties.getGroup(), "test: 123"));
		Thread.sleep(1000);
		Assertions.assertEquals("test: 123",
				configUtil.getConfig("test1.yaml", nacosConfigProperties.getGroup(), 5000));

		Assertions.assertTrue(configUtil.removeConfig("test1.yaml", nacosConfigProperties.getGroup()));
		Assertions.assertNull(configUtil.getConfig("test1.yaml", nacosConfigProperties.getGroup(), 5000));
	}

	@Test
	void testGetServerStatus() {
		Assertions.assertEquals("UP", configUtil.getServerStatus());
	}

}
