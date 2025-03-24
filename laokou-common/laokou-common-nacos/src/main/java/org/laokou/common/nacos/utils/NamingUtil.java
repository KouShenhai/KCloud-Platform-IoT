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

	/**
	 * 创建服务发现.
	 * @param serverAddr 服务地址
	 */
	public static NamingService createNamingService(String serverAddr) throws NacosException {
		return NacosFactory.createNamingService(serverAddr);
	}

	/**
	 * 创建服务发现.
	 * @param properties 配置
	 */
	public static NamingService createNamingService(Properties properties) throws NacosException {
		return NacosFactory.createNamingService(properties);
	}

	/**
	 * Nacos优雅停机.
	 */
	public void nacosServiceShutDown() throws NacosException {
		nacosServiceManager.nacosServiceShutDown();
	}

	/**
	 * 获取维护服务.
	 * @param properties 配置
	 */
	public NamingMaintainService getNamingMaintainService(Properties properties) {
		return nacosServiceManager.getNamingMaintainService(properties);
	}

	/**
	 * Nacos注册服务的信息是否变更.
	 */
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

	/**
	 * 通过服务ID获取服务实例.
	 * @param serviceId 服务ID
	 * @return 服务实例
	 */
	public List<Instance> getAllInstances(String serviceId) throws NacosException {
		return getNamingService().getAllInstances(serviceId);
	}

	/**
	 * 通过服务ID和分组获取服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 */
	public List<Instance> getAllInstances(String serviceId, String group) throws NacosException {
		return getNamingService().getAllInstances(serviceId, group);
	}

	/**
	 * 通过服务ID和订阅标识获取所有服务实例.
	 * @param serviceId 服务ID
	 * @param subscribe 是否订阅
	 */
	public List<Instance> getAllInstances(String serviceId, boolean subscribe) throws NacosException {
		return getNamingService().getAllInstances(serviceId, subscribe);
	}

	/**
	 * 通过服务ID、分组和订阅标识获取所有服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param subscribe 是否订阅
	 */
	public List<Instance> getAllInstances(String serviceId, String group, boolean subscribe) throws NacosException {
		return getNamingService().getAllInstances(serviceId, group, subscribe);
	}

	/**
	 * 通过服务ID和集群列表获取所有服务实例.
	 * @param serviceId 服务ID
	 * @param clusters 集群列表
	 */
	public List<Instance> getAllInstances(String serviceId, List<String> clusters) throws NacosException {
		return getNamingService().getAllInstances(serviceId, clusters);
	}

	/**
	 * 通过服务ID、分组和集群列表获取所有服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param clusters 集群列表
	 */
	public List<Instance> getAllInstances(String serviceId, String group, List<String> clusters) throws NacosException {
		return getNamingService().getAllInstances(serviceId, group, clusters);
	}

	/**
	 * 通过服务ID、集群列表和订阅标识获取所有服务实例.
	 * @param serviceId 服务ID
	 * @param clusters 集群列表
	 * @param subscribe 是否订阅
	 */
	public List<Instance> getAllInstances(String serviceId, List<String> clusters, boolean subscribe)
			throws NacosException {
		return getNamingService().getAllInstances(serviceId, clusters, subscribe);
	}

	/**
	 * 通过服务ID、分组、集群列表和订阅标识获取所有服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param clusters 集群列表
	 * @param subscribe 是否订阅
	 */
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

	/**
	 * 根据服务ID和健康标识查询服务实例.
	 * @param serviceId 服务ID
	 * @param healthy 是否健康
	 */
	public List<Instance> selectInstances(String serviceId, boolean healthy) throws NacosException {
		return getNamingService().selectInstances(serviceId, healthy);
	}

	/**
	 * 根据服务ID、分组和健康标识查询服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param healthy 是否健康
	 */
	public List<Instance> selectInstances(String serviceId, String group, boolean healthy) throws NacosException {
		return getNamingService().selectInstances(serviceId, group, healthy);
	}

	/**
	 * 根据服务ID和集群列表查询服务实例.
	 * @param serviceId 服务ID
	 * @param healthy 是否健康
	 * @param subscribe 是否订阅
	 */
	public List<Instance> selectInstances(String serviceId, boolean healthy, boolean subscribe) throws NacosException {
		return getNamingService().selectInstances(serviceId, healthy, subscribe);
	}

	/**
	 * 根据服务ID、分组和集群列表查询服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param healthy 是否健康
	 * @param subscribe 是否订阅
	 */
	public List<Instance> selectInstances(String serviceId, String group, boolean healthy, boolean subscribe)
			throws NacosException {
		return getNamingService().selectInstances(serviceId, group, healthy, subscribe);
	}

	/**
	 * 根据服务ID、集群列表和健康查询服务实例.
	 * @param serviceId 服务ID
	 * @param clusters 集群列表
	 * @param healthy 是否健康
	 */
	public List<Instance> selectInstances(String serviceId, List<String> clusters, boolean healthy)
			throws NacosException {
		return getNamingService().selectInstances(serviceId, clusters, healthy);
	}

	/**
	 * 根据服务ID、分组、集群列表和健康查询服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param clusters 集群列表
	 * @param healthy 是否健康
	 */
	public List<Instance> selectInstances(String serviceId, String group, List<String> clusters, boolean healthy)
			throws NacosException {
		return getNamingService().selectInstances(serviceId, group, clusters, healthy);
	}

	/**
	 * 根据服务ID、集群列表、健康和订阅查询服务实例.
	 * @param serviceId 服务ID
	 * @param clusters 集群列表
	 * @param healthy 是否健康
	 * @param subscribe 是否订阅
	 */
	public List<Instance> selectInstances(String serviceId, List<String> clusters, boolean healthy, boolean subscribe)
			throws NacosException {
		return getNamingService().selectInstances(serviceId, clusters, healthy, subscribe);
	}

	/**
	 * 根据服务ID、分组、集群列表、健康和订阅查询服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param clusters 集群列表
	 * @param healthy 是否健康
	 * @param subscribe 是否订阅
	 */
	public List<Instance> selectInstances(String serviceId, String group, List<String> clusters, boolean healthy,
			boolean subscribe) throws NacosException {
		return getNamingService().selectInstances(serviceId, group, clusters, healthy, subscribe);
	}

	/**
	 * 根据服务ID查询健康的服务实例.
	 * @param serviceId 服务ID
	 */
	public Instance selectOneHealthyInstance(String serviceId) throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId);
	}

	/**
	 * 根据服务ID和分组查询健康的服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 */
	public Instance selectOneHealthyInstance(String serviceId, String group) throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, group);
	}

	/**
	 * 根据服务ID和订阅标识查询健康的服务实例.
	 * @param serviceId 服务ID
	 * @param subscribe 是否订阅
	 */
	public Instance selectOneHealthyInstance(String serviceId, boolean subscribe) throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, subscribe);
	}

	/**
	 * 根据服务ID、分组和订阅标识查询一个健康的服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param subscribe 是否订阅
	 */
	public Instance selectOneHealthyInstance(String serviceId, String group, boolean subscribe) throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, group, subscribe);
	}

	/**
	 * 根据服务ID和集群列表查询一个健康的服务实例.
	 * @param serviceId 服务ID
	 * @param clusters 集群列表
	 */
	public Instance selectOneHealthyInstance(String serviceId, List<String> clusters) throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, clusters);
	}

	/**
	 * 根据服务ID、分组和集群列表查询一个健康的服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param clusters 集群列表
	 */
	public Instance selectOneHealthyInstance(String serviceId, String group, List<String> clusters)
			throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, group, clusters);
	}

	/**
	 * 根据服务ID、集群列表和订阅查询一个健康的服务实例.
	 * @param serviceId 服务ID
	 * @param clusters 集群列表
	 * @param subscribe 是否订阅
	 */
	public Instance selectOneHealthyInstance(String serviceId, List<String> clusters, boolean subscribe)
			throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, clusters, subscribe);
	}

	/**
	 * 根据服务ID、分组、集群列表和订阅查询一个健康的服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param clusters 集群列表
	 * @param subscribe 是否订阅
	 */
	public Instance selectOneHealthyInstance(String serviceId, String group, List<String> clusters, boolean subscribe)
			throws NacosException {
		return getNamingService().selectOneHealthyInstance(serviceId, group, clusters, subscribe);
	}

	/**
	 * 根据服务ID订阅服务并监听.
	 * @param serviceId 服务ID
	 * @param listener 监听器
	 */
	public void subscribe(String serviceId, EventListener listener) throws NacosException {
		getNamingService().subscribe(serviceId, listener);
	}

	/**
	 * 根据服务ID和分组订阅服务并监听.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param listener 监听器
	 */
	public void subscribe(String serviceId, String group, EventListener listener) throws NacosException {
		getNamingService().subscribe(serviceId, group, listener);
	}

	/**
	 * 根据服务ID和集群列表订阅服务并监听.
	 * @param serviceId 服务ID
	 * @param clusters 集群列表
	 * @param listener 监听器
	 */
	public void subscribe(String serviceId, List<String> clusters, EventListener listener) throws NacosException {
		getNamingService().subscribe(serviceId, clusters, listener);
	}

	/**
	 * 根据服务ID、分组和集群列表订阅服务并监听.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param clusters 集群列表
	 * @param listener 监听器
	 */
	public void subscribe(String serviceId, String group, List<String> clusters, EventListener listener)
			throws NacosException {
		getNamingService().subscribe(serviceId, group, clusters, listener);
	}

	/**
	 * 根据服务ID取消订阅并监听.
	 * @param serviceId 服务ID
	 * @param listener 监听器
	 */
	public void unsubscribe(String serviceId, EventListener listener) throws NacosException {
		getNamingService().unsubscribe(serviceId, listener);
	}

	/**
	 * 根据服务ID和分组取消订阅并监听.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param listener 监听器
	 */
	public void unsubscribe(String serviceId, String group, EventListener listener) throws NacosException {
		getNamingService().unsubscribe(serviceId, group, listener);
	}

	/**
	 * 根据服务ID和集群列表取消订阅并监听.
	 * @param serviceId 服务ID
	 * @param clusters 集群列表
	 * @param listener 监听器
	 */
	public void unsubscribe(String serviceId, List<String> clusters, EventListener listener) throws NacosException {
		getNamingService().unsubscribe(serviceId, clusters, listener);
	}

	/**
	 * 根据服务ID、分组和集群列表取消订阅并监听.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param clusters 集群列表
	 * @param listener 监听器
	 */
	public void unsubscribe(String serviceId, String group, List<String> clusters, EventListener listener)
			throws NacosException {
		getNamingService().unsubscribe(serviceId, group, clusters, listener);
	}

	/**
	 * 根据服务ID、分组和服务实例列表批量注册服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param instances 服务实例列表
	 */
	public void batchRegisterInstance(String serviceId, String group, List<Instance> instances) throws NacosException {
		getNamingService().batchRegisterInstance(serviceId, group, instances);
	}

	/**
	 * 根据服务ID和服务实例列表批量取消注册服务实例.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param instances 服务实例列表
	 */
	public void batchDeregisterInstance(String serviceId, String group, List<Instance> instances)
			throws NacosException {
		getNamingService().batchDeregisterInstance(serviceId, group, instances);
	}

	/**
	 * 根据服务ID和选择器订阅并监听事件.
	 * @param serviceId 服务ID
	 * @param selector 选择器
	 * @param listener 事件监听器
	 */
	public void subscribe(String serviceId, NamingSelector selector, EventListener listener) throws NacosException {
		getNamingService().subscribe(serviceId, selector, listener);
	}

	/**
	 * 根据服务ID、分组和选择器订阅并监听事件.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param selector 选择器
	 * @param listener 事件监听器
	 */
	public void subscribe(String serviceId, String group, NamingSelector selector, EventListener listener)
			throws NacosException {
		getNamingService().subscribe(serviceId, group, selector, listener);
	}

	/**
	 * 根据服务ID和选择器取消订阅并监听事件.
	 * @param serviceId 服务ID
	 * @param selector 选择器
	 * @param listener 事件监听器
	 */
	public void unsubscribe(String serviceId, NamingSelector selector, EventListener listener) throws NacosException {
		getNamingService().unsubscribe(serviceId, selector, listener);
	}

	/**
	 * 根据服务ID、分组和选择器取消订阅并监听事件.
	 * @param serviceId 服务ID
	 * @param group 分组
	 * @param selector 选择器
	 * @param listener 事件监听器
	 */
	public void unsubscribe(String serviceId, String group, NamingSelector selector, EventListener listener)
			throws NacosException {
		getNamingService().unsubscribe(serviceId, group, selector, listener);
	}

	/**
	 * 分页获取服务列表.
	 * @param pageNo 页数
	 * @param pageSize 条数
	 */
	public ListView<String> getServicesOfServer(int pageNo, int pageSize) throws NacosException {
		return getNamingService().getServicesOfServer(pageNo, pageSize);
	}

	/**
	 * 根据分组分页获取服务列表.
	 * @param pageNo 页数
	 * @param pageSize 条数
	 * @param group 分组
	 */
	public ListView<String> getServicesOfServer(int pageNo, int pageSize, String group) throws NacosException {
		return getNamingService().getServicesOfServer(pageNo, pageSize, group);
	}

	/**
	 * 获取订阅服务列表.
	 */
	public List<ServiceInfo> getSubscribeServices() throws NacosException {
		return getNamingService().getSubscribeServices();
	}

	/**
	 * 获取服务.
	 */
	private NamingService getNamingService() {
		return nacosServiceManager.getNamingService();
	}

}
