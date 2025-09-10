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
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.selector.NamingSelector;
import com.alibaba.nacos.client.naming.selector.NamingSelectorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.nacos.util.NamingUtils;
import org.laokou.common.testcontainers.container.NacosContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author laokou
 */
// @formatter:off
class NamingUtilsTest {

	private NamingService namingService;

	static final NacosContainer nacos = new NacosContainer(DockerImageNames.nacos("v3.0.3"));

	@BeforeAll
	static void beforeAll() {
		nacos.start();
	}

	@AfterAll
	static void afterAll() {
		nacos.stop();
	}

	@BeforeEach
	void setUp()throws NacosException {
		namingService = NamingUtils.createNamingService(nacos.getServerAddr());
		Assertions.assertThat(namingService.getServerStatus()).isEqualTo("UP");
		Properties properties = new Properties();
		properties.setProperty(PropertyKeyConst.USERNAME, "nacos");
		properties.setProperty(PropertyKeyConst.NAMESPACE, "nacos");
		properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacos.getServerAddr());
		properties.setProperty(PropertyKeyConst.NAMESPACE, "public");
		namingService = NamingUtils.createNamingService(properties);
		Assertions.assertThat(namingService.getServerStatus()).isEqualTo("UP");
	}

	@Test
	void test_getAllInstances() throws NacosException, InterruptedException {
		Assertions.assertThat(namingService.getAllInstances("test-service").isEmpty()).isTrue();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service").isEmpty()).isFalse();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP").isEmpty()).isTrue();

		Instance instance = new Instance();
		instance.setIp("127.0.0.1");
		instance.setPort(8080);
		Assertions.assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", instance));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty()).isFalse();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", instance));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", false).isEmpty()).isTrue();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "DEFAULT_GROUP", instance));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", Collections.emptyList()).isEmpty()).isFalse();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "DEFAULT_GROUP", instance));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", Collections.emptyList()).isEmpty()).isTrue();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", List.of("nacos-cluster"), false).isEmpty()).isFalse();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), false).isEmpty()).isTrue();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty()).isFalse();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty()).isTrue();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), false).isEmpty()).isFalse();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), false).isEmpty()).isTrue();
	}

	@Test
	void test_selectInstances() throws NacosException, InterruptedException {
		Assertions.assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.selectInstances("test-service", true).isEmpty()).isFalse();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.selectInstances("test-service", true).isEmpty()).isTrue();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.selectInstances("test-service", true, false).isEmpty()).isFalse();

		Assertions.assertThat(namingService.selectInstances("test-service", "DEFAULT_GROUP", true, false).isEmpty()).isFalse();
		Assertions.assertThat(namingService.selectInstances("test-service", List.of("nacos-cluster"), true).isEmpty()).isFalse();
		Assertions.assertThat(namingService.selectInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), true, false).isEmpty()).isFalse();
		Assertions.assertThat(namingService.selectInstances("test-service", List.of("nacos-cluster"), true, false).isEmpty()).isFalse();
		Assertions.assertThat(namingService.selectInstances("test-service", "DEFAULT_GROUP", true).isEmpty()).isFalse();
		Assertions.assertThat(namingService.selectInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), true).isEmpty()).isFalse();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), false).isEmpty()).isTrue();
	}

	@Test
	void test_selectOneHealthyInstance() throws NacosException, InterruptedException {
		Assertions.assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.selectInstances("test-service", true).isEmpty()).isFalse();

		Assertions.assertThat(namingService.selectOneHealthyInstance("test-service")).isNotNull();
		Assertions.assertThat(namingService.selectOneHealthyInstance("test-service", "DEFAULT_GROUP")).isNotNull();
		Assertions.assertThat(namingService.selectOneHealthyInstance("test-service", false)).isNotNull();
		Assertions.assertThat(namingService.selectOneHealthyInstance("test-service", "DEFAULT_GROUP", false)).isNotNull();
		Assertions.assertThat(namingService.selectOneHealthyInstance("test-service", List.of("nacos-cluster"))).isNotNull();
		Assertions.assertThat(namingService.selectOneHealthyInstance("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"))).isNotNull();
		Assertions.assertThat(namingService.selectOneHealthyInstance("test-service", List.of("nacos-cluster"), false)).isNotNull();
		Assertions.assertThat(namingService.selectOneHealthyInstance("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), false)).isNotNull();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.selectInstances("test-service", true).isEmpty()).isTrue();
	}

	@Test
	void test_subscribeService() throws NacosException, InterruptedException {
		Assertions.assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.selectInstances("test-service", true).isEmpty()).isFalse();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", "DEFAULT_GROUP", evt -> Assertions.assertThat(evt).isNotNull()));
		Assertions.assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", "DEFAULT_GROUP", evt -> Assertions.assertThat(evt).isNotNull()));

		Assertions.assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", evt -> Assertions.assertThat(evt).isNotNull()));
		Assertions.assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", evt -> Assertions.assertThat(evt).isNotNull()));

		Assertions.assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), evt -> Assertions.assertThat(evt).isNotNull()));
		Assertions.assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), evt -> Assertions.assertThat(evt).isNotNull()));

		Assertions.assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", List.of("nacos-cluster"), evt -> Assertions.assertThat(evt).isNotNull()));
		Assertions.assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", List.of("nacos-cluster"), evt -> Assertions.assertThat(evt).isNotNull()));

		// 只选择订阅ip为`127.0`开头的实例。
		NamingSelector selector = NamingSelectorFactory.newIpSelector("127.0.*");
		Assertions.assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", "DEFAULT_GROUP", selector, evt -> Assertions.assertThat(evt).isNotNull()));
		Assertions.assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", "DEFAULT_GROUP", selector, evt -> Assertions.assertThat(evt).isNotNull()));

		Assertions.assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", selector, evt -> Assertions.assertThat(evt).isNotNull()));
		Assertions.assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", selector, evt -> Assertions.assertThat(evt).isNotNull()));

		Assertions.assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.selectInstances("test-service", true).isEmpty()).isTrue();
	}

	@Test
	void test_getServicesOfServer() throws NacosException, InterruptedException {
		Assertions.assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.selectInstances("test-service", true).isEmpty()).isFalse();

		Assertions.assertThat(namingService.getServicesOfServer(1, 10, "DEFAULT_GROUP").getCount() > 0).isTrue();
		Assertions.assertThat(namingService.getServicesOfServer(1, 10).getCount() > 0).isTrue();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", "DEFAULT_GROUP", evt -> Assertions.assertThat(evt).isNotNull()));
		Assertions.assertThat(namingService.getSubscribeServices().isEmpty()).isFalse();

		Assertions.assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.selectInstances("test-service", true).isEmpty()).isTrue();
	}

	@Test
	void test_batchRegisterInstance() throws NacosException, InterruptedException {
		Instance instance = new Instance();
		instance.setIp("127.0.0.1");
		instance.setPort(8080);
		Assertions.assertThatNoException().isThrownBy(() -> namingService.batchRegisterInstance("test-service", "DEFAULT_GROUP", List.of(instance)));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.selectInstances("test-service", true).size()).isEqualTo(1);

		Assertions.assertThatNoException().isThrownBy(() -> namingService.batchDeregisterInstance("test-service", "DEFAULT_GROUP", List.of(instance)));
		Thread.sleep(Duration.ofSeconds(1));
		Assertions.assertThat(namingService.selectInstances("test-service", false).size()).isEqualTo(0);
	}

}
// @formatter:on
