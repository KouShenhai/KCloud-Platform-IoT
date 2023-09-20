/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.common.nacos.utils;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * <a href="https://github.com/alibaba/spring-cloud-alibaba/wiki/Nacos-discovery">...</a>
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceUtil {

	private final LoadBalancerClient loadBalancerClient;

	private final NacosDiscoveryProperties nacosDiscoveryProperties;

	private final DiscoveryClient nacosDiscoveryClient;

	public List<String> getServices() {
		return nacosDiscoveryClient.getServices();
	}

	public List<ServiceInstance> getInstances(String serviceId) {
		return nacosDiscoveryClient.getInstances(serviceId);
	}

	public ServiceInstance getInstance(String serviceId) {
		return loadBalancerClient.choose(serviceId);
	}

	@SneakyThrows
	private NamingService getNamingService() {
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.NAMESPACE, nacosDiscoveryProperties.getNamespace());
		properties.put(PropertyKeyConst.SERVER_ADDR, nacosDiscoveryProperties.getServerAddr());
		properties.put(PropertyKeyConst.USERNAME, nacosDiscoveryProperties.getUsername());
		properties.put(PropertyKeyConst.PASSWORD, nacosDiscoveryProperties.getPassword());
		return NacosFactory.createNamingService(properties);
	}

	@SneakyThrows
	public void registerInstance(String serviceName, String ip, int port) {
		NamingService namingService = getNamingService();
		namingService.registerInstance(serviceName, ip, port);
	}

	@SneakyThrows
	public void registerInstance(String serviceName, String group, String ip, int port) {
		NamingService namingService = getNamingService();
		namingService.registerInstance(serviceName, group, ip, port);
	}

	@SneakyThrows
	public void deregisterInstance(String serviceName, String ip, int port) {
		NamingService namingService = getNamingService();
		namingService.deregisterInstance(serviceName, ip, port);
	}

	@SneakyThrows
	public void deregisterInstance(String serviceName, String group, String ip, int port) {
		NamingService namingService = getNamingService();
		namingService.deregisterInstance(serviceName, group, ip, port);
	}

}
