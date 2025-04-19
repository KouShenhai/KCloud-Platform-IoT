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

/*
 * Copyright 2013-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.nacos.loadbalancer;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.util.InetIPv6Utils;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.RegexUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.nacos.util.ReactiveRequestUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.constant.StringConstants.TRUE;
import static org.laokou.common.i18n.common.constant.TraceConstants.*;

/**
 * nacos路由负载均衡.
 * {@link com.alibaba.cloud.nacos.loadbalancer.NacosLoadBalancerClientConfiguration}
 * {@link org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer}
 *
 * @author XuDaojie
 * @author laokou
 * @since 2021.1
 */
@Slf4j
public class NacosLoadBalancer implements ReactorServiceInstanceLoadBalancer {

	/**
	 * 优雅停机URL.
	 */
	public static final String GRACEFUL_SHUTDOWN_URL = "/graceful-shutdown";

	/**
	 * Nacos集群配置.
	 */
	private static final String CLUSTER_CONFIG = "nacos.cluster";

	/**
	 * 版本.
	 */
	private static final String VERSION = "version";

	/**
	 * 版本号.
	 */
	private static final String DEFAULT_VERSION_VALUE = "v3";

	/**
	 * IPV6常量.
	 */
	private static final String IPV6_KEY = "IPv6";

	/**
	 * Storage local valid IPv6 address, it's a flag whether local machine support IPv6
	 * address stack.
	 */
	public static String ipv6;

	/**
	 * 服务ID.
	 */
	private final String serviceId;

	private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

	private final NacosDiscoveryProperties nacosDiscoveryProperties;

	private final InetIPv6Utils inetIPv6Utils;

	private final List<ServiceInstanceFilter> serviceInstanceFilters;

	private final Map<String, LoadBalancerAlgorithm> loadBalancerAlgorithmMap;

	public NacosLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
			String serviceId, NacosDiscoveryProperties nacosDiscoveryProperties, InetIPv6Utils inetIPv6Utils,
			List<ServiceInstanceFilter> serviceInstanceFilters,
			Map<String, LoadBalancerAlgorithm> loadBalancerAlgorithmMap) {
		this.serviceId = serviceId;
		this.inetIPv6Utils = inetIPv6Utils;
		this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
		this.nacosDiscoveryProperties = nacosDiscoveryProperties;
		this.serviceInstanceFilters = serviceInstanceFilters;
		this.loadBalancerAlgorithmMap = loadBalancerAlgorithmMap;
	}

	/**
	 * 初始化.
	 */
	@PostConstruct
	public void init() {
		String ip = nacosDiscoveryProperties.getIp();
		if (com.alibaba.cloud.commons.lang.StringUtils.isNotEmpty(ip)) {
			ipv6 = RegexUtils.ipv4Regex(ip) ? nacosDiscoveryProperties.getMetadata().get(IPV6_KEY) : ip;
		}
		else {
			ipv6 = inetIPv6Utils.findIPv6Address();
		}
	}

	/**
	 * 根据IP类型过滤服务实例.
	 * @param instances 服务实例
	 * @return 服务实例列表
	 */
	private List<ServiceInstance> filterInstanceByIpType(List<ServiceInstance> instances) {
		if (com.alibaba.cloud.commons.lang.StringUtils.isNotEmpty(ipv6)) {
			List<ServiceInstance> ipv6InstanceList = new ArrayList<>();
			for (ServiceInstance instance : instances) {
				if (RegexUtils.ipv4Regex(instance.getHost())) {
					if (com.alibaba.cloud.commons.lang.StringUtils.isNotEmpty(instance.getMetadata().get(IPV6_KEY))) {
						ipv6InstanceList.add(instance);
					}
				}
				else {
					ipv6InstanceList.add(instance);
				}
			}
			// Provider has no IPv6, should use IPv4.
			if (ipv6InstanceList.isEmpty()) {
				return instances.stream()
					.filter(instance -> RegexUtils.ipv4Regex(instance.getHost()))
					.collect(Collectors.toList());
			}
			else {
				return ipv6InstanceList;
			}
		}
		return instances.stream()
			.filter(instance -> RegexUtils.ipv4Regex(instance.getHost()))
			.collect(Collectors.toList());
	}

	/**
	 * 路由负载均衡.
	 * @param request 请求
	 * @return 服务实例（响应式）
	 */
	@Override
	public Mono<Response<ServiceInstance>> choose(Request request) {
		return serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new)
			.get(request)
			.next()
			.map(instances -> getInstanceResponse(instances, request));
	}

	/**
	 * 路由负载均衡.
	 * @param serviceInstances 服务实例列表
	 * @param request 请求
	 * @return 服务实例响应体
	 */
	private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> serviceInstances, Request<?> request) {
		if (serviceInstances.isEmpty()) {
			log.warn("No servers available for service: {}", this.serviceId);
			return new EmptyResponse();
		}
		if (request.getContext() instanceof RequestDataContext context) {
			// IP优先（优雅停机）
			String path = context.getClientRequest().getUrl().getPath();
			HttpHeaders headers = context.getClientRequest().getHeaders();
			if (ReactiveRequestUtils.pathMatcher(HttpMethod.GET.name(), path,
					Map.of(HttpMethod.GET.name(), Collections.singleton(GRACEFUL_SHUTDOWN_URL)))) {
				ServiceInstance serviceInstance = serviceInstances.stream()
					.filter(instance -> match(instance, headers))
					.findFirst()
					.orElse(null);
				if (ObjectUtils.isNotNull(serviceInstance)) {
					return new DefaultResponse(serviceInstance);
				}
			}
			// 服务灰度路由
			if (isGrayRouter(headers)) {
				String version = RegexUtils.getRegexValue(path, "/(v\\d+)/");
				if (com.alibaba.cloud.commons.lang.StringUtils.isNotEmpty(version)) {
					serviceInstances = serviceInstances.stream()
						.filter(item -> item.getMetadata().getOrDefault(VERSION, DEFAULT_VERSION_VALUE).equals(version))
						.toList();
				}
			}
		}
		return getInstanceResponse(request, serviceInstances);
	}

	/**
	 * 服务实例响应.
	 * @param serviceInstances 服务实例
	 * @return 响应结果
	 */
	private Response<ServiceInstance> getInstanceResponse(Request<?> request, List<ServiceInstance> serviceInstances) {
		if (serviceInstances.isEmpty()) {
			log.error("No servers available for service: {}", this.serviceId);
			return new EmptyResponse();
		}
		try {
			String clusterName = this.nacosDiscoveryProperties.getClusterName();
			List<ServiceInstance> instancesToChoose = serviceInstances;
			if (com.alibaba.cloud.commons.lang.StringUtils.isNotBlank(clusterName)) {
				List<ServiceInstance> sameClusterInstances = serviceInstances.stream().filter(serviceInstance -> {
					String cluster = serviceInstance.getMetadata().get(CLUSTER_CONFIG);
					return com.alibaba.cloud.commons.lang.StringUtils.equals(cluster, clusterName);
				}).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(sameClusterInstances)) {
					instancesToChoose = sameClusterInstances;
				}
			}
			else {
				log.warn("A cross-cluster call occurs，name = {}, clusterName = {}, instance = {}", serviceId,
						clusterName, serviceInstances);
			}
			instancesToChoose = this.filterInstanceByIpType(instancesToChoose);

			// Filter the service list sequentially based on the order number
			for (ServiceInstanceFilter filter : serviceInstanceFilters) {
				instancesToChoose = filter.filterInstance(request, instancesToChoose);
			}

			ServiceInstance instance;
			// Find the corresponding load balancing algorithm through the service ID and
			// select the final service instance
			if (loadBalancerAlgorithmMap.containsKey(serviceId)) {
				instance = loadBalancerAlgorithmMap.get(serviceId).getInstance(request, instancesToChoose);
			}
			else {
				instance = loadBalancerAlgorithmMap.get(LoadBalancerAlgorithm.DEFAULT_SERVICE_ID)
					.getInstance(request, instancesToChoose);
			}

			return new DefaultResponse(instance);
		}
		catch (Exception e) {
			log.error("NacosLoadBalancer error", e);
			return null;
		}
	}

	/**
	 * 判断服务灰度路由.
	 * @param headers 请求头
	 * @return 判断结果
	 */
	private boolean isGrayRouter(HttpHeaders headers) {
		String gray = headers.getFirst(SERVICE_GRAY);
		return ObjectUtils.equals(TRUE, gray);
	}

	/**
	 * 根据IP和端口匹配服务节点.
	 * @param serviceInstance 服务实例
	 * @param headers 请求头
	 * @return 匹配结果
	 */
	private boolean match(ServiceInstance serviceInstance, HttpHeaders headers) {
		String host = headers.getFirst(SERVICE_HOST);
		String port = headers.getFirst(SERVICE_PORT);
		Assert.isTrue(StringUtils.isNotEmpty(host), "service-host is empty");
		Assert.isTrue(StringUtils.isNotEmpty(port), "service-port is empty");
		return ObjectUtils.equals(host, serviceInstance.getHost())
				&& Integer.parseInt(port) == serviceInstance.getPort();
	}

}
