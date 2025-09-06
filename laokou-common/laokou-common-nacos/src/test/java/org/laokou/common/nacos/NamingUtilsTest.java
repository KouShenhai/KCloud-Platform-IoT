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
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.selector.NamingSelector;
import com.alibaba.nacos.client.naming.selector.NamingSelectorFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.nacos.util.NamingUtils;
import org.laokou.common.testcontainers.NacosContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * @author laokou
 */
// @formatter:off
@SpringBootTest
@RequiredArgsConstructor
@ContextConfiguration(classes = { NamingUtils.class,
	NacosServiceManager.class,
	NacosDiscoveryProperties.class,
	ApplicationEventPublisher.class,
	InetUtilsProperties.class,
	Environment.class,
	InetUtils.class,
	InetIPv6Utils.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class NamingUtilsTest {

	private final NamingUtils namingUtils;

	private final NacosDiscoveryProperties nacosDiscoveryProperties;

	static NacosContainer nacos = new NacosContainer();

	@BeforeAll
	static void beforeAll() {
		nacos.start();
	}

	@AfterAll
	static void afterAll() {
		nacos.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.cloud.nacos.discovery.server-addr", () -> nacos.getServerAddr());
	}

	@BeforeEach
	void setUp() {
		assertThat(nacosDiscoveryProperties).isNotNull();
		assertThat(nacosDiscoveryProperties.getNamespace()).isEqualTo("public");
		assertThat(nacosDiscoveryProperties.getServerAddr()).isEqualTo(nacos.getServerAddr());
		assertThat(nacosDiscoveryProperties.getGroup()).isEqualTo("DEFAULT_GROUP");
		assertThat(nacosDiscoveryProperties.getUsername()).isEqualTo("nacos");
		assertThat(nacosDiscoveryProperties.getPassword()).isEqualTo("nacos");
		assertThat(nacosDiscoveryProperties.getEndpoint()).isEqualTo("");
		assertThat(nacosDiscoveryProperties.getAccessKey()).isEqualTo("");
		assertThat(nacosDiscoveryProperties.getSecretKey()).isEqualTo("");
		assertThat(nacosDiscoveryProperties.getClusterName()).isEqualTo("nacos-cluster");
		assertThat(nacosDiscoveryProperties.getNacosProperties()).isNotNull();
		assertThat(namingUtils).isNotNull();
	}

	@Test
	void test_createNamingService() throws Exception {
		Thread.sleep(Duration.ofSeconds(1));
		NamingService namingService = NamingUtils.createNamingService(nacosDiscoveryProperties.getServerAddr());
		assertThat(namingService).isNotNull();
		namingService = NamingUtils.createNamingService(nacosDiscoveryProperties.getNacosProperties());
		assertThat(namingService).isNotNull();
	}

	@Test
	void test_isNacosDiscoveryInfoChanged()throws InterruptedException {
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingUtils.isNacosDiscoveryInfoChanged(nacosDiscoveryProperties)).isFalse();
	}

	@Test
	void test_getAllInstances() throws NacosException, InterruptedException {
		Thread.sleep(Duration.ofSeconds(8));
		assertThat(namingUtils.getAllInstances("test-service").isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service").isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP").isEmpty()).isTrue();

		Instance instance = new Instance();
		instance.setIp("127.0.0.1");
		instance.setPort(8080);
		assertThatNoException().isThrownBy(() -> namingUtils.registerInstance("test-service", instance));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingUtils.deregisterInstance("test-service", instance));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", false).isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingUtils.registerInstance("test-service", "DEFAULT_GROUP", instance));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", Collections.emptyList()).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingUtils.deregisterInstance("test-service", "DEFAULT_GROUP", instance));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", Collections.emptyList()).isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingUtils.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", List.of(nacosDiscoveryProperties.getClusterName()), false).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingUtils.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), false).isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingUtils.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingUtils.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), false).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), false).isEmpty()).isTrue();
	}

	@Test
	void test_selectInstances() throws NacosException, InterruptedException {
		Thread.sleep(Duration.ofSeconds(15));
		assertThatNoException().isThrownBy(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.selectInstances("test-service", true).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.selectInstances("test-service", true).isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.selectInstances("test-service", true, false).isEmpty()).isFalse();

		assertThat(namingUtils.selectInstances("test-service", "DEFAULT_GROUP", true, false).isEmpty()).isFalse();
		assertThat(namingUtils.selectInstances("test-service", List.of(nacosDiscoveryProperties.getClusterName()), true).isEmpty()).isFalse();
		assertThat(namingUtils.selectInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), true, false).isEmpty()).isFalse();
		assertThat(namingUtils.selectInstances("test-service", List.of(nacosDiscoveryProperties.getClusterName()), true, false).isEmpty()).isFalse();
		assertThat(namingUtils.selectInstances("test-service", "DEFAULT_GROUP", true).isEmpty()).isFalse();
		assertThat(namingUtils.selectInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), true).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.getAllInstances("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), false).isEmpty()).isTrue();
	}

	@Test
	void test_selectOneHealthyInstance() throws NacosException, InterruptedException {
		Thread.sleep(Duration.ofSeconds(8));
		assertThatNoException().isThrownBy(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.selectInstances("test-service", true).isEmpty()).isFalse();

		assertThat(namingUtils.selectOneHealthyInstance("test-service")).isNotNull();
		assertThat(namingUtils.selectOneHealthyInstance("test-service", "DEFAULT_GROUP")).isNotNull();
		assertThat(namingUtils.selectOneHealthyInstance("test-service", false)).isNotNull();
		assertThat(namingUtils.selectOneHealthyInstance("test-service", "DEFAULT_GROUP", false)).isNotNull();
		assertThat(namingUtils.selectOneHealthyInstance("test-service", List.of(nacosDiscoveryProperties.getClusterName()))).isNotNull();
		assertThat(namingUtils.selectOneHealthyInstance("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()))).isNotNull();
		assertThat(namingUtils.selectOneHealthyInstance("test-service", List.of(nacosDiscoveryProperties.getClusterName()), false)).isNotNull();
		assertThat(namingUtils.selectOneHealthyInstance("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), false)).isNotNull();

		assertThatNoException().isThrownBy(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.selectInstances("test-service", true).isEmpty()).isTrue();
	}

	@Test
	void test_subscribeService() throws NacosException, InterruptedException {
		Thread.sleep(Duration.ofSeconds(8));
		assertThatNoException().isThrownBy(() -> namingUtils.registerInstance("test-service", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.selectInstances("test-service", true).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingUtils.subscribe("test-service", "DEFAULT_GROUP", evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingUtils.unsubscribe("test-service", "DEFAULT_GROUP", evt -> assertThat(evt).isNotNull()));

		assertThatNoException().isThrownBy(() -> namingUtils.subscribe("test-service", evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingUtils.unsubscribe("test-service", evt -> assertThat(evt).isNotNull()));

		assertThatNoException().isThrownBy(() -> namingUtils.subscribe("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingUtils.unsubscribe("test-service", "DEFAULT_GROUP", List.of(nacosDiscoveryProperties.getClusterName()), evt -> assertThat(evt).isNotNull()));

		assertThatNoException().isThrownBy(() -> namingUtils.subscribe("test-service", List.of(nacosDiscoveryProperties.getClusterName()), evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingUtils.unsubscribe("test-service", List.of(nacosDiscoveryProperties.getClusterName()), evt -> assertThat(evt).isNotNull()));

		// 只选择订阅ip为`127.0`开头的实例。
		NamingSelector selector = NamingSelectorFactory.newIpSelector("127.0.*");
		assertThatNoException().isThrownBy(() -> namingUtils.subscribe("test-service", "DEFAULT_GROUP", selector, evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingUtils.unsubscribe("test-service", "DEFAULT_GROUP", selector, evt -> assertThat(evt).isNotNull()));

		assertThatNoException().isThrownBy(() -> namingUtils.subscribe("test-service", selector, evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingUtils.unsubscribe("test-service", selector, evt -> assertThat(evt).isNotNull()));

		assertThatNoException().isThrownBy(() -> namingUtils.deregisterInstance("test-service", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.selectInstances("test-service", true).isEmpty()).isTrue();
	}

	@Test
	void test_getServicesOfServer() throws NacosException, InterruptedException {
		Thread.sleep(Duration.ofSeconds(8));
		assertThatNoException().isThrownBy(() -> namingUtils.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.selectInstances("test-service", true).isEmpty()).isFalse();

		assertThat(namingUtils.getServicesOfServer(1, 10, "DEFAULT_GROUP").getCount() > 0).isTrue();
		assertThat(namingUtils.getServicesOfServer(1, 10).getCount() > 0).isTrue();

		assertThatNoException().isThrownBy(() -> namingUtils.subscribe("test-service", "DEFAULT_GROUP", evt -> assertThat(evt).isNotNull()));
		assertThat(namingUtils.getSubscribeServices().isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingUtils.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, nacosDiscoveryProperties.getClusterName()));
		Thread.sleep(1000);
		assertThat(namingUtils.selectInstances("test-service", true).isEmpty()).isTrue();
	}

	@Test
	void test_batchRegisterInstance() throws NacosException, InterruptedException {
		Thread.sleep(Duration.ofSeconds(8));
		Instance instance = new Instance();
		instance.setIp("127.0.0.1");
		instance.setPort(8080);
		assertThatNoException().isThrownBy(() -> namingUtils.batchRegisterInstance("test-service", "DEFAULT_GROUP", List.of(instance)));
		Thread.sleep(1000);
		assertThat(namingUtils.selectInstances("test-service", true).size()).isEqualTo(1);

		assertThatNoException().isThrownBy(() -> namingUtils.batchDeregisterInstance("test-service", "DEFAULT_GROUP", List.of(instance)));
		Thread.sleep(1000);
		assertThat(namingUtils.selectInstances("test-service", false).size()).isEqualTo(0);
	}

	@Test
	void test_nacosServiceShutDown() throws InterruptedException {
		Thread.sleep(Duration.ofSeconds(1));
		assertThatNoException().isThrownBy(namingUtils::nacosServiceShutDown);
	}

}
// @formatter:on
