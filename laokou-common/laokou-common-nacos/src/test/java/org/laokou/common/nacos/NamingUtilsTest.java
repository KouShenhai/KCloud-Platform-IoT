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
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.selector.NamingSelector;
import com.alibaba.nacos.client.naming.selector.NamingSelectorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.nacos.util.NamingUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@ContextConfiguration(classes = { NamingUtils.class, NacosServiceManager.class, NacosDiscoveryProperties.class,
		ApplicationEventPublisher.class, InetUtilsProperties.class, Environment.class, InetUtils.class,
		InetIPv6Utils.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class NamingUtilsTest {

	private final NamingUtils namingUtils;

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
		Assertions.assertNotNull(namingUtils);
	}

	@Test
	void testCreateNamingService() throws Exception {
		NamingService namingService = NamingUtils.createNamingService(nacosDiscoveryProperties.getServerAddr());
		Assertions.assertNotNull(namingService);
		namingService = NamingUtils.createNamingService(nacosDiscoveryProperties.getNacosProperties());
		Assertions.assertNotNull(namingService);
	}

	@Test
	void testGetNamingMaintainService() {
		NamingMaintainService namingMaintainService = namingUtils
			.getNamingMaintainService(nacosDiscoveryProperties.getNacosProperties());
		Assertions.assertNotNull(namingMaintainService);
	}

	@Test
	void testIsNacosDiscoveryInfoChanged() {
		Assertions.assertFalse(namingUtils.isNacosDiscoveryInfoChanged(nacosDiscoveryProperties));
	}

	@Test
	void testGetAllInstances() throws NacosException, InterruptedException {
		Assertions.assertTrue(namingUtils.getAllInstances("test-service").isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080));
		Thread.sleep(1000);
		Assertions.assertFalse(namingUtils.getAllInstances("test-service").isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080));
		Thread.sleep(1000);
		Assertions.assertTrue(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP").isEmpty());

		Instance instance = new Instance();
		instance.setIp("127.0.0.1");
		instance.setPort(8080);
		Assertions.assertDoesNotThrow(() -> namingUtils.registerInstance("test-service", instance));
		Thread.sleep(1000);
		Assertions.assertFalse(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.deregisterInstance("test-service", instance));
		Thread.sleep(1000);
		Assertions.assertTrue(namingUtils.getAllInstances("test-service", false).isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.registerInstance("test-service", "DEFAULT_GROUP", instance));
		Thread.sleep(1000);
		Assertions.assertFalse(namingUtils.getAllInstances("test-service", Collections.emptyList()).isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.deregisterInstance("test-service", "DEFAULT_GROUP", instance));
		Thread.sleep(1000);
		Assertions.assertTrue(
				namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", Collections.emptyList()).isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1",
				8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertFalse(
				namingUtils.getAllInstances("test-service", List.of(nacosDiscoveryProperties.getClusterName()), false)
					.isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1",
				8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertTrue(namingUtils
			.getAllInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), false)
			.isEmpty());

		Assertions
			.assertDoesNotThrow(() -> namingUtils.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080));
		Thread.sleep(1000);
		Assertions.assertFalse(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty());

		Assertions.assertDoesNotThrow(
				() -> namingUtils.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080));
		Thread.sleep(1000);
		Assertions.assertTrue(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080,
				nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertFalse(namingUtils
			.getAllInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), false)
			.isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080,
				nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertTrue(namingUtils
			.getAllInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), false)
			.isEmpty());
	}

	@Test
	void testSelectInstances() throws NacosException, InterruptedException {
		Assertions.assertDoesNotThrow(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080,
				nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertFalse(namingUtils.selectInstances("test-service", true).isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080,
				nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertTrue(namingUtils.selectInstances("test-service", true).isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080,
				nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertFalse(namingUtils.selectInstances("test-service", true, false).isEmpty());

		Assertions.assertFalse(namingUtils.selectInstances("test-service", "DEFAULT_GROUP", true, false).isEmpty());
		Assertions.assertFalse(
				namingUtils.selectInstances("test-service", List.of(nacosDiscoveryProperties.getClusterName()), true)
					.isEmpty());
		Assertions
			.assertFalse(namingUtils
				.selectInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()),
						true, false)
				.isEmpty());
		Assertions.assertFalse(namingUtils
			.selectInstances("test-service", List.of(nacosDiscoveryProperties.getClusterName()), true, false)
			.isEmpty());
		Assertions.assertFalse(namingUtils.selectInstances("test-service", "DEFAULT_GROUP", true).isEmpty());
		Assertions.assertFalse(namingUtils
			.selectInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), true)
			.isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080,
				nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertTrue(namingUtils
			.getAllInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), false)
			.isEmpty());
	}

	@Test
	void testSelectOneHealthyInstance() throws NacosException, InterruptedException {
		Assertions.assertDoesNotThrow(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080,
				nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertFalse(namingUtils.selectInstances("test-service", true).isEmpty());

		Assertions.assertNotNull(namingUtils.selectOneHealthyInstance("test-service"));
		Assertions.assertNotNull(namingUtils.selectOneHealthyInstance("test-service", "DEFAULT_GROUP"));
		Assertions.assertNotNull(namingUtils.selectOneHealthyInstance("test-service", false));
		Assertions.assertNotNull(namingUtils.selectOneHealthyInstance("test-service", "DEFAULT_GROUP", false));
		Assertions.assertNotNull(namingUtils.selectOneHealthyInstance("test-service",
				List.of(nacosDiscoveryProperties.getClusterName())));
		Assertions.assertNotNull(namingUtils.selectOneHealthyInstance("test-service", "DEFAULT_GROUP",
				List.of(nacosDiscoveryProperties.getClusterName())));
		Assertions.assertNotNull(namingUtils.selectOneHealthyInstance("test-service",
				List.of(nacosDiscoveryProperties.getClusterName()), false));
		Assertions.assertNotNull(namingUtils.selectOneHealthyInstance("test-service", "DEFAULT_GROUP",
				List.of(nacosDiscoveryProperties.getClusterName()), false));

		Assertions.assertDoesNotThrow(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080,
				nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertTrue(namingUtils.selectInstances("test-service", true).isEmpty());
	}

	@Test
	void testSubscribeService() throws NacosException, InterruptedException {
		Assertions.assertDoesNotThrow(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080,
				nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertFalse(namingUtils.selectInstances("test-service", true).isEmpty());

		Assertions.assertDoesNotThrow(
				() -> namingUtils.subscribe("test-service", "DEFAULT_GROUP", Assertions::assertNotNull));
		Assertions.assertDoesNotThrow(
				() -> namingUtils.unsubscribe("test-service", "DEFAULT_GROUP", Assertions::assertNotNull));
		Assertions.assertDoesNotThrow(() -> namingUtils.subscribe("test-service", Assertions::assertNotNull));
		Assertions.assertDoesNotThrow(() -> namingUtils.unsubscribe("test-service", Assertions::assertNotNull));
		Assertions.assertDoesNotThrow(() -> namingUtils.subscribe("test-service", "DEFAULT_GROUP",
				List.of(nacosDiscoveryProperties.getClusterName()), Assertions::assertNotNull));
		Assertions.assertDoesNotThrow(() -> namingUtils.unsubscribe("test-service", "DEFAULT_GROUP",
				List.of(nacosDiscoveryProperties.getClusterName()), Assertions::assertNotNull));
		Assertions.assertDoesNotThrow(() -> namingUtils.subscribe("test-service",
				List.of(nacosDiscoveryProperties.getClusterName()), Assertions::assertNotNull));
		Assertions.assertDoesNotThrow(() -> namingUtils.unsubscribe("test-service",
				List.of(nacosDiscoveryProperties.getClusterName()), Assertions::assertNotNull));

		// 只选择订阅ip为`127.0`开头的实例。
		NamingSelector selector = NamingSelectorFactory.newIpSelector("127.0.*");
		Assertions.assertDoesNotThrow(
				() -> namingUtils.subscribe("test-service", "DEFAULT_GROUP", selector, Assertions::assertNotNull));
		Assertions.assertDoesNotThrow(
				() -> namingUtils.unsubscribe("test-service", "DEFAULT_GROUP", selector, Assertions::assertNotNull));
		Assertions.assertDoesNotThrow(() -> namingUtils.subscribe("test-service", selector, Assertions::assertNotNull));
		Assertions
			.assertDoesNotThrow(() -> namingUtils.unsubscribe("test-service", selector, Assertions::assertNotNull));

		Assertions.assertDoesNotThrow(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080,
				nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertTrue(namingUtils.selectInstances("test-service", true).isEmpty());
	}

	@Test
	void testGetServicesOfServer() throws NacosException, InterruptedException {
		Assertions.assertDoesNotThrow(() -> namingUtils.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1",
				8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertFalse(namingUtils.selectInstances("test-service", true).isEmpty());

		Assertions.assertTrue(namingUtils.getServicesOfServer(1, 10, "DEFAULT_GROUP").getCount() > 0);
		Assertions.assertTrue(namingUtils.getServicesOfServer(1, 10).getCount() > 0);

		Assertions.assertDoesNotThrow(
				() -> namingUtils.subscribe("test-service", "DEFAULT_GROUP", Assertions::assertNotNull));
		Assertions.assertFalse(namingUtils.getSubscribeServices().isEmpty());

		Assertions.assertDoesNotThrow(() -> namingUtils.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1",
				8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		Assertions.assertTrue(namingUtils.selectInstances("test-service", true).isEmpty());
	}

	@Test
	void testBatchRegisterInstance() throws NacosException, InterruptedException {
		Instance instance = new Instance();
		instance.setIp("127.0.0.1");
		instance.setPort(8080);
		Assertions.assertDoesNotThrow(
				() -> namingUtils.batchRegisterInstance("test-service", "DEFAULT_GROUP", List.of(instance)));
		Thread.sleep(1000);
		Assertions.assertNotEquals(2, namingUtils.selectInstances("test-service", true).size());

		Assertions.assertDoesNotThrow(
				() -> namingUtils.batchDeregisterInstance("test-service", "DEFAULT_GROUP", List.of(instance)));
		Thread.sleep(1000);
		Assertions.assertEquals(0, namingUtils.selectInstances("test-service", false).size());
	}

	@Test
	void testNacosServiceShutDown() throws InterruptedException {
		Thread.sleep(1000);
		Assertions.assertDoesNotThrow(namingUtils::nacosServiceShutDown);
	}

}
