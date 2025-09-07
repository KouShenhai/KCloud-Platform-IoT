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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

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
		assertThat(namingService.getServerStatus()).isEqualTo("UP");
		Properties properties = new Properties();
		properties.setProperty(PropertyKeyConst.USERNAME, "nacos");
		properties.setProperty(PropertyKeyConst.NAMESPACE, "nacos");
		properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacos.getServerAddr());
		properties.setProperty(PropertyKeyConst.NAMESPACE, "public");
		namingService = NamingUtils.createNamingService(properties);
		assertThat(namingService.getServerStatus()).isEqualTo("UP");
	}

	@Test
	void test_getAllInstances() throws NacosException, InterruptedException {
		assertThat(namingService.getAllInstances("test-service").isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service").isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP").isEmpty()).isTrue();

		Instance instance = new Instance();
		instance.setIp("127.0.0.1");
		instance.setPort(8080);
		assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", instance));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", instance));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", false).isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "DEFAULT_GROUP", instance));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", Collections.emptyList()).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "DEFAULT_GROUP", instance));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", Collections.emptyList()).isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", List.of("nacos-cluster"), false).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), false).isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", false).isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), false).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), false).isEmpty()).isTrue();
	}

	@Test
	void test_selectInstances() throws NacosException, InterruptedException {
		assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.selectInstances("test-service", true).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.selectInstances("test-service", true).isEmpty()).isTrue();

		assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.selectInstances("test-service", true, false).isEmpty()).isFalse();

		assertThat(namingService.selectInstances("test-service", "DEFAULT_GROUP", true, false).isEmpty()).isFalse();
		assertThat(namingService.selectInstances("test-service", List.of("nacos-cluster"), true).isEmpty()).isFalse();
		assertThat(namingService.selectInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), true, false).isEmpty()).isFalse();
		assertThat(namingService.selectInstances("test-service", List.of("nacos-cluster"), true, false).isEmpty()).isFalse();
		assertThat(namingService.selectInstances("test-service", "DEFAULT_GROUP", true).isEmpty()).isFalse();
		assertThat(namingService.selectInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), true).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.getAllInstances("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), false).isEmpty()).isTrue();
	}

	@Test
	void test_selectOneHealthyInstance() throws NacosException, InterruptedException {
		assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.selectInstances("test-service", true).isEmpty()).isFalse();

		assertThat(namingService.selectOneHealthyInstance("test-service")).isNotNull();
		assertThat(namingService.selectOneHealthyInstance("test-service", "DEFAULT_GROUP")).isNotNull();
		assertThat(namingService.selectOneHealthyInstance("test-service", false)).isNotNull();
		assertThat(namingService.selectOneHealthyInstance("test-service", "DEFAULT_GROUP", false)).isNotNull();
		assertThat(namingService.selectOneHealthyInstance("test-service", List.of("nacos-cluster"))).isNotNull();
		assertThat(namingService.selectOneHealthyInstance("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"))).isNotNull();
		assertThat(namingService.selectOneHealthyInstance("test-service", List.of("nacos-cluster"), false)).isNotNull();
		assertThat(namingService.selectOneHealthyInstance("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), false)).isNotNull();

		assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.selectInstances("test-service", true).isEmpty()).isTrue();
	}

	@Test
	void test_subscribeService() throws NacosException, InterruptedException {
		assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.selectInstances("test-service", true).isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", "DEFAULT_GROUP", evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", "DEFAULT_GROUP", evt -> assertThat(evt).isNotNull()));

		assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", evt -> assertThat(evt).isNotNull()));

		assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", "DEFAULT_GROUP", List.of("nacos-cluster"), evt -> assertThat(evt).isNotNull()));

		assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", List.of("nacos-cluster"), evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", List.of("nacos-cluster"), evt -> assertThat(evt).isNotNull()));

		// 只选择订阅ip为`127.0`开头的实例。
		NamingSelector selector = NamingSelectorFactory.newIpSelector("127.0.*");
		assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", "DEFAULT_GROUP", selector, evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", "DEFAULT_GROUP", selector, evt -> assertThat(evt).isNotNull()));

		assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", selector, evt -> assertThat(evt).isNotNull()));
		assertThatNoException().isThrownBy(() -> namingService.unsubscribe("test-service", selector, evt -> assertThat(evt).isNotNull()));

		assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.selectInstances("test-service", true).isEmpty()).isTrue();
	}

	@Test
	void test_getServicesOfServer() throws NacosException, InterruptedException {
		assertThatNoException().isThrownBy(() -> namingService.registerInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.selectInstances("test-service", true).isEmpty()).isFalse();

		assertThat(namingService.getServicesOfServer(1, 10, "DEFAULT_GROUP").getCount() > 0).isTrue();
		assertThat(namingService.getServicesOfServer(1, 10).getCount() > 0).isTrue();

		assertThatNoException().isThrownBy(() -> namingService.subscribe("test-service", "DEFAULT_GROUP", evt -> assertThat(evt).isNotNull()));
		assertThat(namingService.getSubscribeServices().isEmpty()).isFalse();

		assertThatNoException().isThrownBy(() -> namingService.deregisterInstance("test-service", "DEFAULT_GROUP", "127.0.0.1", 8080, "nacos-cluster"));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.selectInstances("test-service", true).isEmpty()).isTrue();
	}

	@Test
	void test_batchRegisterInstance() throws NacosException, InterruptedException {
		Instance instance = new Instance();
		instance.setIp("127.0.0.1");
		instance.setPort(8080);
		assertThatNoException().isThrownBy(() -> namingService.batchRegisterInstance("test-service", "DEFAULT_GROUP", List.of(instance)));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.selectInstances("test-service", true).size()).isEqualTo(1);

		assertThatNoException().isThrownBy(() -> namingService.batchDeregisterInstance("test-service", "DEFAULT_GROUP", List.of(instance)));
		Thread.sleep(Duration.ofSeconds(1));
		assertThat(namingService.selectInstances("test-service", false).size()).isEqualTo(0);
	}

}
// @formatter:on
