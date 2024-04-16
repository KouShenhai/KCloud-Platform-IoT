/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.balancer.NacosBalancer;
import com.alibaba.cloud.nacos.util.InetIPv6Utils;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.common.i18n.utils.ObjectUtils;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.nacos.utils.ReactiveRequestUtil;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.laokou.common.core.utils.RegexUtil.URL_VERSION_REGEX;
import static org.laokou.common.i18n.common.NetworkConstant.IPV4_REGEX;
import static org.laokou.common.i18n.common.StringConstant.*;
import static org.laokou.common.i18n.common.SysConstants.GRACEFUL_SHUTDOWN_URL;

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

	@Schema(name = "SERVICE_HOST", description = "服务IP")
	private static final String SERVICE_HOST = "service-host";

	@Schema(name = "SERVICE_PORT", description = "服务端口")
	private static final String SERVICE_PORT = "service-port";

	@Schema(name = "SERVICE-GRAY", description = "服务灰度")
	private static final String SERVICE_GRAY = "service-gray";

	@Schema(name = "CLUSTER_CONFIG", description = "Nacos集群配置")
	private static final String CLUSTER_CONFIG = "nacos.cluster";

	private final String serviceId;

	private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

	private final NacosDiscoveryProperties nacosDiscoveryProperties;

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
	 * 初始化.
	 */
	@PostConstruct
	public void init() {
		String ip = nacosDiscoveryProperties.getIp();
		if (StringUtils.isNotEmpty(ip)) {
			ipv6 = Pattern.matches(IPV4_REGEX, ip) ? nacosDiscoveryProperties.getMetadata().get(IPV6_KEY) : ip;
		}
		else {
			ipv6 = SpringContextUtil.getBean(InetIPv6Utils.class).findIPv6Address();
		}
	}

	/**
	 * 根据IP类型过滤服务实例.
	 * @param instances 服务实例
	 * @return 服务实例列表
	 */
	private List<ServiceInstance> filterInstanceByIpType(List<ServiceInstance> instances) {
		if (StringUtils.isNotEmpty(ipv6)) {
			List<ServiceInstance> ipv6InstanceList = new ArrayList<>();
			for (ServiceInstance instance : instances) {
				if (Pattern.matches(IPV4_REGEX, instance.getHost())) {
					if (StringUtils.isNotEmpty(instance.getMetadata().get(IPV6_KEY))) {
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
					.filter(instance -> Pattern.matches(IPV4_REGEX, instance.getHost()))
					.collect(Collectors.toList());
			}
			else {
				return ipv6InstanceList;
			}
		}
		return instances.stream()
			.filter(instance -> Pattern.matches(IPV4_REGEX, instance.getHost()))
			.collect(Collectors.toList());
	}

	public NacosLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
			String serviceId, NacosDiscoveryProperties nacosDiscoveryProperties) {
		this.serviceId = serviceId;
		this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
		this.nacosDiscoveryProperties = nacosDiscoveryProperties;
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
			if (ReactiveRequestUtil.pathMatcher(HttpMethod.GET.name(), path,
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
				String version = RegexUtil.find(path, URL_VERSION_REGEX);
				if (StringUtils.isNotEmpty(version)) {
					serviceInstances = serviceInstances.stream()
						.filter(item -> item.getMetadata().getOrDefault(VERSION, EMPTY).equals(version))
						.toList();
				}
			}
		}
		return getInstanceResponse(serviceInstances);
	}

	/**
	 * 服务实例响应.
	 * @param serviceInstances 服务实例
	 * @return 响应结果
	 */
	private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> serviceInstances) {
		if (serviceInstances.isEmpty()) {
			log.warn("No servers available for service: {}", this.serviceId);
			return new EmptyResponse();
		}
		try {
			String clusterName = this.nacosDiscoveryProperties.getClusterName();
			List<ServiceInstance> instancesToChoose = serviceInstances;
			if (StringUtils.isNotBlank(clusterName)) {
				List<ServiceInstance> sameClusterInstances = serviceInstances.stream().filter(serviceInstance -> {
					String cluster = serviceInstance.getMetadata().get(CLUSTER_CONFIG);
					return StringUtils.equals(cluster, clusterName);
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
			// 路由权重
			ServiceInstance instance = NacosBalancer.getHostByRandomWeight3(instancesToChoose);
			return new DefaultResponse(instance);
		}
		catch (Exception e) {
			log.warn("NacosLoadBalancer error", e);
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
		Assert.isTrue(StringUtil.isNotEmpty(host), "service-host is empty");
		Assert.isTrue(StringUtil.isNotEmpty(port), "service-port is empty");
		return ObjectUtils.equals(host, serviceInstance.getHost()) && Integer.parseInt(port) == instance.getPort();
	}

}
