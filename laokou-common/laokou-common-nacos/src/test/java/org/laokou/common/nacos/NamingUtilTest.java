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

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.util.InetIPv6Utils;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.nacos.utils.NamingUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@ContextConfiguration(classes = { NamingUtil.class, NacosServiceManager.class, NacosDiscoveryProperties.class,
		ApplicationEventPublisher.class, InetUtilsProperties.class, Environment.class, InetUtils.class,
		InetIPv6Utils.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class NamingUtilTest {

	private final NamingUtil namingUtil;

	private final NacosDiscoveryProperties nacosDiscoveryProperties;

	@BeforeEach
	void setUp() {
		Assertions.assertNotNull(nacosDiscoveryProperties);
		Assertions.assertEquals("public", nacosDiscoveryProperties.getNamespace());
		Assertions.assertEquals("127.0.0.1:8848", nacosDiscoveryProperties.getServerAddr());
		Assertions.assertEquals("DEFAULT_GROUP", nacosDiscoveryProperties.getGroup());
		Assertions.assertEquals("nacos", nacosDiscoveryProperties.getUsername());
		Assertions.assertEquals("nacos", nacosDiscoveryProperties.getPassword());
		Assertions.assertEquals("", nacosDiscoveryProperties.getEndpoint());
		Assertions.assertEquals("", nacosDiscoveryProperties.getAccessKey());
		Assertions.assertEquals("", nacosDiscoveryProperties.getSecretKey());
		Assertions.assertNotNull(nacosDiscoveryProperties.getNacosProperties());
		Assertions.assertNotNull(namingUtil);
	}

	@Test
	void testCreateNamingService() throws Exception {
		NamingService namingService = NamingUtil.createNamingService(nacosDiscoveryProperties.getServerAddr());
		Assertions.assertNotNull(namingService);
		namingService = NamingUtil.createNamingService(nacosDiscoveryProperties.getNacosProperties());
		Assertions.assertNotNull(namingService);
	}

	@Test
	void testGetNamingMaintainService() {
		NamingMaintainService namingMaintainService = namingUtil
			.getNamingMaintainService(nacosDiscoveryProperties.getNacosProperties());
		Assertions.assertNotNull(namingMaintainService);
	}

	@Test
	void testIsNacosDiscoveryInfoChanged() {
		Assertions.assertFalse(namingUtil.isNacosDiscoveryInfoChanged(nacosDiscoveryProperties));
	}

	@Test
	void testGetAllInstances() throws NacosException, InterruptedException {
		Assertions.assertTrue(namingUtil.getAllInstances("test-service").isEmpty());
		Assertions.assertDoesNotThrow(() -> namingUtil.registerInstance("test-service", "127.0.0.1", 8080));
		Thread.sleep(1000);
		Assertions.assertFalse(namingUtil.getAllInstances("test-service").isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtil.deregisterInstance("test-service", "127.0.0.1", 8080));
		Thread.sleep(1000);
		Assertions.assertTrue(namingUtil.getAllInstances("test-service").isEmpty());
	}

	@Test
	void testNacosServiceShutDown() throws InterruptedException {
		Thread.sleep(4000);
		Assertions.assertDoesNotThrow(namingUtil::nacosServiceShutDown);
	}

}
