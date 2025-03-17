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

package org.laokou.common.nacos.utils;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.alibaba.nacos.api.naming.selector.NamingSelector;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

// @formatter:off
/**
 * @see <a href="https://nacos.io/docs/latest/manual/user/java-sdk/usage/?spm=5238cd80.47ee59c.0.0.189fcd3671Q8Cm">...</a>
 * @author laokou
 */
// @formatter:on
@Component
@RequiredArgsConstructor
public final class NamingUtil {

	private final NacosServiceManager nacosServiceManager;

	private final LoadBalancerClient loadBalancerClient;

	private final DiscoveryClient nacosDiscoveryClient;

	public static NamingService createNamingService(String serverAddr) throws NacosException {
		return NacosFactory.createNamingService(serverAddr);
	}

	public static NamingService createNamingService(Properties properties) throws NacosException {
		return NacosFactory.createNamingService(properties);
	}

	public void nacosServiceShutDown() throws NacosException {
		nacosServiceManager.nacosServiceShutDown();
	}

	public NamingMaintainService getNamingMaintainService(Properties properties) {
		return nacosServiceManager.getNamingMaintainService(properties);
	}

	public boolean isNacosDiscoveryInfoChanged(NacosDiscoveryProperties currentNacosDiscoveryPropertiesCache) {
		return nacosServiceManager.isNacosDiscoveryInfoChanged(currentNacosDiscoveryPropertiesCache);
	}

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

	public List<Instance> getAllInstances(String serviceId) throws NacosException {
		return getNamingService().getAllInstances(serviceId);
	}

	public List<Instance> getAllInstances(String serviceId, String group) throws NacosException {
		return getNamingService().getAllInstances(serviceId, group);
	}

	public List<Instance> getAllInstances(String serviceId, boolean subscribe) throws NacosException {
		return getNamingService().getAllInstances(serviceId, subscribe);
	}

	public List<Instance> getAllInstances(String serviceId, String group, boolean subscribe) throws NacosException {
		return getNamingService().getAllInstances(serviceId, group, subscribe);
	}

	public List<Instance> getAllInstances(String serviceId, List<String> clusters) throws NacosException {
		return getNamingService().getAllInstances(serviceId, clusters);
	}

	public List<Instance> getAllInstances(String serviceId, String group, List<String> clusters) throws NacosException {
		return getNamingService().getAllInstances(serviceId, group, clusters);
	}

	public List<Instance> getAllInstances(String serviceId, List<String> clusters, boolean subscribe)
			throws NacosException {
		return getNamingService().getAllInstances(serviceId, clusters, subscribe);
	}

	public List<Instance> getAllInstances(String serviceId, String group, List<String> clusters, boolean subscribe)
			throws NacosException {
		return getNamingService().getAllInstances(serviceId, group, clusters, subscribe);
	}

	/**
	 * 注册实例.
	 * @param serviceId 服务ID
	 * @param ip 服务IP
	 * @param port 服务端口
	 */
	public void registerInstance(String serviceId, String ip, int port) throws NacosException {
		getNamingService().registerInstance(serviceId, ip, port);
	}

	/**
	 * 注册服务.
	 * @param serviceId 服务ID
	 * @param group 服务分组
	 * @param ip 服务IP
	 * @param port 服务端口
	 */
	public void registerInstance(String serviceId, String group, String ip, int port) throws NacosException {
		getNamingService().registerInstance(serviceId, group, ip, port);
	}

	/**
	 * 注册服务实例.
	 * @param serviceId 服务ID
	 * @param clusterName 集群名称
	 * @param ip 服务IP
	 * @param port 服务端口
	 */
	public void registerInstance(String serviceId, String ip, int port, String clusterName) throws NacosException {
		getNamingService().registerInstance(serviceId, ip, port, clusterName);
	}

	/**
	 * 注册服务实例.
	 * @param serviceId 服务ID
	 * @param clusterName 集群名称
	 * @param ip 服务IP
	 * @param port 服务端口
	 * @param group 服务分组
	 */
	public void registerInstance(String serviceId, String group, String ip, int port, String clusterName)
			throws NacosException {
		getNamingService().registerInstance(serviceId, group, ip, port, clusterName);
	}

	/**
	 * 注册服务实例.
	 * @param serviceId 服务ID
	 * @param instance 服务实例
	 */
	public void registerInstance(String serviceId, Instance instance) throws NacosException {
		getNamingService().registerInstance(serviceId, instance);
	}

	/**
	 * 注册服务实例.
	 * @param serviceId 服务ID
	 * @param group 服务分组
	 * @param instance 服务实例
	 */
	public void registerInstance(String serviceId, String group, Instance instance) throws NacosException {
		getNamingService().registerInstance(serviceId, group, instance);
	}

	/**
	 * 注销服务实例.
	 * @param serviceId 服务ID
	 * @param ip 服务IP
	 * @param port 服务端口
	 */
	public void deregisterInstance(String serviceId, String ip, int port) throws NacosException {
		getNamingService().deregisterInstance(serviceId, ip, port);
	}

	/**
	 * 注销服务实例.
	 * @param serviceId 服务ID
	 * @param group 服务分组
	 * @param ip 服务IP
	 * @param port 服务端口
	 */
	public void deregisterInstance(String serviceId, String group, String ip, int port) throws NacosException {
		getNamingService().deregisterInstance(serviceId, group, ip, port);
	}

	/**
	 * 注销服务实例.
	 * @param serviceId 服务ID
	 * @param clusterName 集群名称
	 * @param ip 服务IP
	 * @param port 服务端口
	 */
	public void deregisterInstance(String serviceId, String ip, int port, String clusterName) throws NacosException {
		getNamingService().deregisterInstance(serviceId, ip, port, clusterName);
	}

	/**
	 * 注销服务实例.
	 * @param serviceId 服务ID
	 * @param clusterName 集群名称
	 * @param ip 服务IP
	 * @param port 服务端口
	 * @param group 服务分组
	 */
	public void deregisterInstance(String serviceId, String group, String ip, int port, String clusterName)
			throws NacosException {
		getNamingService().deregisterInstance(serviceId, group, ip, port, clusterName);
	}

	/**
	 * 注销服务实例.
	 * @param serviceId 服务ID
	 * @param instance 服务实例
	 */
	public void deregisterInstance(String serviceId, Instance instance) throws NacosException {
		getNamingService().deregisterInstance(serviceId, instance);
	}

	/**
	 * 注销服务实例.
	 * @param serviceId 服务ID
	 * @param instance 服务实例
	 * @param group 服务分组
	 */
	public void deregisterInstance(String serviceId, String group, Instance instance) throws NacosException {
		getNamingService().deregisterInstance(serviceId, group, instance);
	}

	public List<Instance> selectInstances(String serviceId, boolean healthy) throws NacosException {
		return getNamingService().selectInstances(serviceId, healthy);
	}

	public List<Instance> selectInstances(String serviceId, String group, boolean healthy) throws NacosException {
		return getNamingService().selectInstances(serviceId, group, healthy);
	}

	public List<Instance> selectInstances(String serviceId, boolean healthy, boolean subscribe) throws NacosException {
		return getNamingService().selectInstances(serviceId, healthy, subscribe);
	}

	public List<Instance> selectInstances(String serviceId, String group, boolean healthy, boolean subscribe)
			throws NacosException {
		return getNamingService().selectInstances(serviceId, group, healthy, subscribe);
	}

	public List<Instance> selectInstances(String serviceId, List<String> clusters, boolean healthy)
			throws NacosException {
		return getNamingService().selectInstances(serviceId, clusters, healthy);
	}

	public List<Instance> selectInstances(String serviceId, String group, List<String> clusters, boolean healthy)
			throws NacosException {
		return getNamingService().selectInstances(serviceId, group, clusters, healthy);
	}

	public List<Instance> selectInstances(String serviceId, List<String> clusters, boolean healthy, boolean subscribe)
			throws NacosException {
		return getNamingService().selectInstances(serviceId, clusters, healthy, subscribe);
	}

	public List<Instance> selectInstances(String serviceId, String group, List<String> clusters, boolean healthy,
			boolean subscribe) throws NacosException {
		return getNamingService().selectInstances(serviceId, group, clusters, healthy, subscribe);
	}

	public Instance selectOneHealthyInstance(String serviceId) throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId);
	}

	public Instance selectOneHealthyInstance(String serviceId, String group) throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, group);
	}

	public Instance selectOneHealthyInstance(String serviceId, boolean subscribe) throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, subscribe);
	}

	public Instance selectOneHealthyInstance(String serviceId, String group, boolean subscribe) throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, group, subscribe);
	}

	public Instance selectOneHealthyInstance(String serviceId, List<String> clusters) throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, clusters);
	}

	public Instance selectOneHealthyInstance(String serviceId, String group, List<String> clusters)
			throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, group, clusters);
	}

	public Instance selectOneHealthyInstance(String serviceId, List<String> clusters, boolean subscribe)
			throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, clusters, subscribe);
	}

	public Instance selectOneHealthyInstance(String serviceId, String group, List<String> clusters, boolean subscribe)
			throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, group, clusters, subscribe);
	}

	public void subscribe(String serviceId, EventListener listener) throws NacosException {
		getNamingService().subscribe(serviceId, listener);
	}

	public void subscribe(String serviceId, String group, EventListener listener) throws NacosException {
		getNamingService().subscribe(serviceId, group, listener);
	}

	public void subscribe(String serviceId, List<String> clusters, EventListener listener) throws NacosException {
		getNamingService().subscribe(serviceId, clusters, listener);
	}

	public void subscribe(String serviceId, String group, List<String> clusters, EventListener listener)
			throws NacosException {
		getNamingService().subscribe(serviceId, group, clusters, listener);
	}

	public void unsubscribe(String serviceId, EventListener listener) throws NacosException {
		getNamingService().unsubscribe(serviceId, listener);
	}

	public void unsubscribe(String serviceId, String group, EventListener listener) throws NacosException {
		getNamingService().unsubscribe(serviceId, group, listener);
	}

	public void unsubscribe(String serviceId, List<String> clusters, EventListener listener) throws NacosException {
		getNamingService().unsubscribe(serviceId, clusters, listener);
	}

	public void unsubscribe(String serviceId, String group, List<String> clusters, EventListener listener)
			throws NacosException {
		getNamingService().unsubscribe(serviceId, group, clusters, listener);
	}

	public void batchRegisterInstance(String serviceId, String group, List<Instance> instances) throws NacosException {
		getNamingService().batchRegisterInstance(serviceId, group, instances);
	}

	public void batchDeregisterInstance(String serviceId, String group, List<Instance> instances)
			throws NacosException {
		getNamingService().batchDeregisterInstance(serviceId, group, instances);
	}

	public void subscribe(String serviceId, NamingSelector selector, EventListener listener) throws NacosException {
		getNamingService().subscribe(serviceId, selector, listener);
	}

	public void subscribe(String serviceId, String group, NamingSelector selector, EventListener listener)
			throws NacosException {
		getNamingService().subscribe(serviceId, group, selector, listener);
	}

	public void unsubscribe(String serviceId, NamingSelector selector, EventListener listener) throws NacosException {
		getNamingService().unsubscribe(serviceId, selector, listener);
	}

	public void unsubscribe(String serviceId, String group, NamingSelector selector, EventListener listener)
			throws NacosException {
		getNamingService().unsubscribe(serviceId, group, selector, listener);
	}

	public ListView<String> getServicesOfServer(int pageNo, int pageSize) throws NacosException {
		return getNamingService().getServicesOfServer(pageNo, pageSize);
	}

	public ListView<String> getServicesOfServer(int pageNo, int pageSize, String group) throws NacosException {
		return getNamingService().getServicesOfServer(pageNo, pageSize, group);
	}

	public List<ServiceInfo> getSubscribeServices() throws NacosException {
		return getNamingService().getSubscribeServices();
	}

	private NamingService getNamingService() {
		return nacosServiceManager.getNamingService();
	}

}
