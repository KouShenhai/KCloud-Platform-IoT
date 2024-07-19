/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import com.alibaba.nacos.client.naming.NacosNamingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.util.List;
import java.util.Properties;

/**
 * Nacos服务工具类.
 * <a href="https://github.com/alibaba/spring-cloud-alibaba/wiki/Nacos-discovery">...</a>
 *
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public class ServiceUtil {

	private static final String NAMESPACE = "namespace";

	private static final String SERVER_ADDR = "serverAddr";

	private static final String USERNAME = "username";

	private static final String PASSWORD = "password";

	private final LoadBalancerClient loadBalancerClient;

	private final NacosDiscoveryProperties nacosDiscoveryProperties;

	private final DiscoveryClient nacosDiscoveryClient;

	private volatile NacosNamingService nacosNamingService;

	/**
	 * 查询服务列表.
	 * @return 服务列表
	 */
	public List<String> getServices() {
		return nacosDiscoveryClient.getServices();
	}

	/**
	 * 查询服务实例列表.
	 * @param serviceId 服务ID
	 * @return 服务实例列表
	 */
	public List<ServiceInstance> getInstances(String serviceId) {
		return nacosDiscoveryClient.getInstances(serviceId);
	}

	/**
	 * 通过负载均衡获取服务实例.
	 * @param serviceId 服务ID
	 * @return 服务实例
	 */
	public ServiceInstance getInstance(String serviceId) {
		return loadBalancerClient.choose(serviceId);
	}

	/**
	 * 查看命名服务.
	 * @return 命令服务
	 */
	@SneakyThrows
	private NacosNamingService getNacosNamingService() {
		if (ObjectUtil.isNull(nacosNamingService)) {
			synchronized (NacosNamingService.class) {
				if (ObjectUtil.isNull(nacosNamingService)) {
					Properties properties = new Properties();
					properties.put(NAMESPACE, nacosDiscoveryProperties.getNamespace());
					properties.put(SERVER_ADDR, nacosDiscoveryProperties.getServerAddr());
					properties.put(USERNAME, nacosDiscoveryProperties.getUsername());
					properties.put(PASSWORD, nacosDiscoveryProperties.getPassword());
					nacosNamingService = new NacosNamingService(properties);
				}
			}
		}
		return nacosNamingService;
	}

	/**
	 * 注册实例.
	 * @param serviceName 服务名称
	 * @param ip 服务IP
	 * @param port 服务端口
	 */
	@SneakyThrows
	public void registerInstance(String serviceName, String ip, int port) {
		getNacosNamingService().registerInstance(serviceName, ip, port);
	}

	/**
	 * 注册服务.
	 * @param serviceName 服务名称
	 * @param group 服务分组
	 * @param ip 服务IP
	 * @param port 服务端口
	 */
	@SneakyThrows
	public void registerInstance(String serviceName, String group, String ip, int port) {
		getNacosNamingService().registerInstance(serviceName, group, ip, port);
	}

	/**
	 * 注销服务.
	 * @param serviceName 服务名称
	 * @param ip 服务IP
	 * @param port 服务端口
	 */
	@SneakyThrows
	public void deregisterInstance(String serviceName, String ip, int port) {
		getNacosNamingService().deregisterInstance(serviceName, ip, port);
	}

	/**
	 * 注销服务.
	 * @param serviceName 服务名称
	 * @param group 服务分组
	 * @param ip 服务IP
	 * @param port 服务端口
	 */
	@SneakyThrows
	public void deregisterInstance(String serviceName, String group, String ip, int port) {
		getNacosNamingService().deregisterInstance(serviceName, group, ip, port);
	}

}
