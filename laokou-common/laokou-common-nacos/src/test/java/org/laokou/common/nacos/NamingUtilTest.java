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
import com.alibaba.nacos.api.naming.NamingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.nacos.utils.NamingUtil;
import java.util.Properties;
import static com.alibaba.nacos.api.PropertyKeyConst.*;
import static com.alibaba.nacos.api.PropertyKeyConst.NAMESPACE;
import static com.alibaba.nacos.api.naming.CommonParams.GROUP_NAME;

/**
 * @author laokou
 */
class NamingUtilTest {

	private NamingUtil namingUtil;

	private NacosDiscoveryProperties nacosDiscoveryProperties;

	private Properties properties;

	@BeforeEach
	void setUp() {
		nacosDiscoveryProperties = new NacosDiscoveryProperties();
		nacosDiscoveryProperties.setServerAddr("127.0.0.1:8848");
		nacosDiscoveryProperties.setNamespace("public");
		nacosDiscoveryProperties.setUsername("nacos");
		nacosDiscoveryProperties.setPassword("nacos");
		nacosDiscoveryProperties.setGroup("DEFAULT_GROUP");
		Assertions.assertNotNull(nacosDiscoveryProperties);
		Assertions.assertEquals("public", nacosDiscoveryProperties.getNamespace());
		Assertions.assertEquals("127.0.0.1:8848", nacosDiscoveryProperties.getServerAddr());
		Assertions.assertEquals("DEFAULT_GROUP", nacosDiscoveryProperties.getGroup());
		Assertions.assertEquals("nacos", nacosDiscoveryProperties.getUsername());
		Assertions.assertEquals("nacos", nacosDiscoveryProperties.getPassword());

		properties = new Properties();
		properties.put(SERVER_ADDR, nacosDiscoveryProperties.getServerAddr());
		properties.put(USERNAME, nacosDiscoveryProperties.getUsername());
		properties.put(PASSWORD, nacosDiscoveryProperties.getPassword());
		properties.put(NAMESPACE, nacosDiscoveryProperties.getNamespace());
		properties.put(GROUP_NAME, nacosDiscoveryProperties.getGroup());
		Assertions.assertNotNull(properties);
		Assertions.assertEquals(nacosDiscoveryProperties.getServerAddr(), properties.get(SERVER_ADDR));
		Assertions.assertEquals(nacosDiscoveryProperties.getUsername(), properties.get(USERNAME));
		Assertions.assertEquals(nacosDiscoveryProperties.getPassword(), properties.get(PASSWORD));
		Assertions.assertEquals(nacosDiscoveryProperties.getNamespace(), properties.get(NAMESPACE));
		Assertions.assertEquals(nacosDiscoveryProperties.getGroup(), properties.get(GROUP_NAME));

		NacosServiceManager nacosServiceManager = new NacosServiceManager();
		nacosServiceManager.setNacosDiscoveryProperties(nacosDiscoveryProperties);
		namingUtil = new NamingUtil(nacosServiceManager);
		Assertions.assertNotNull(namingUtil);
	}

	@Test
	void testCreateNamingService() throws Exception {
		NamingService namingService = NamingUtil.createNamingService(nacosDiscoveryProperties.getServerAddr());
		Assertions.assertNotNull(namingService);
		namingService = NamingUtil.createNamingService(properties);
		Assertions.assertNotNull(namingService);
	}

	@Test
	void testNacosServiceShutDown() {
		Assertions.assertDoesNotThrow(() -> namingUtil.nacosServiceShutDown());
	}

}
