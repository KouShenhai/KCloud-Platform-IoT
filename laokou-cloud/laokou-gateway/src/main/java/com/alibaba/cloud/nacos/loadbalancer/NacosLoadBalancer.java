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
import jakarta.annotation.PostConstruct;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.common.nacos.utils.ServiceUtil;
import org.laokou.gateway.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.Constant.*;

/**
 * see original.
 * {@link org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer}
 *
 * @author XuDaojie
 * @author laokou
 * @since 2021.1
 */
public class NacosLoadBalancer implements ReactorServiceInstanceLoadBalancer {

	private static final Logger log = LoggerFactory.getLogger(NacosLoadBalancer.class);

	private final String serviceId;

	private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

	private final NacosDiscoveryProperties nacosDiscoveryProperties;

	private static final String IPV4_REGEX = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

	private static final String IPV6_KEY = "IPv6";

	/**
	 * Storage local valid IPv6 address, it's a flag whether local machine support IPv6
	 * address stack.
	 */
	public static String ipv6;

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

	@Override
	public Mono<Response<ServiceInstance>> choose(Request request) {
		// IP优先
		Mono<Response<ServiceInstance>> mono = chooseIp(request);
		if (Objects.nonNull(mono)) {
			return mono;
		}
		return chooseDefault(request);
	}

	private Mono<Response<ServiceInstance>> chooseDefault(Request request) {
		ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
			.getIfAvailable(NoopServiceInstanceListSupplier::new);
		return supplier.get(request).next().mapNotNull(this::getInstanceResponse);
	}

	private Mono<Response<ServiceInstance>> chooseIp(Request request) {
		if (request.getContext() instanceof RequestDataContext context) {
			String path = context.getClientRequest().getUrl().getPath();
			if (RequestUtil.pathMatcher(path, Collections.singleton(GRACEFUL_SHUTDOWN_URL))) {
				HttpHeaders headers = context.getClientRequest().getHeaders();
				String serviceId = Objects.requireNonNull(headers.get(SERVICE_ID)).getFirst();
				String host = Objects.requireNonNull(headers.get(SERVICE_HOST)).getFirst();
				String port = Objects.requireNonNull(headers.get(SERVICE_PORT)).getFirst();
				List<ServiceInstance> instances = SpringContextUtil.getBean(ServiceUtil.class).getInstances(serviceId);
				ServiceInstance serviceInstance = instances.stream()
					.filter(i -> host.equals(i.getHost()) && Integer.parseInt(port) == i.getPort())
					.findFirst()
					.orElse(null);
				if (Objects.nonNull(serviceInstance)) {
					return Mono.just(new DefaultResponse(serviceInstance));
				}
			}
		}
		return null;
	}

	private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> serviceInstances) {
		if (serviceInstances.isEmpty()) {
			log.warn("No servers available for service: " + this.serviceId);
			return new EmptyResponse();
		}

		try {
			String clusterName = this.nacosDiscoveryProperties.getClusterName();

			List<ServiceInstance> instancesToChoose = serviceInstances;
			if (StringUtils.isNotBlank(clusterName)) {
				List<ServiceInstance> sameClusterInstances = serviceInstances.stream().filter(serviceInstance -> {
					String cluster = serviceInstance.getMetadata().get("nacos.cluster");
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

			ServiceInstance instance = NacosBalancer.getHostByRandomWeight3(instancesToChoose);

			return new DefaultResponse(instance);
		}
		catch (Exception e) {
			log.warn("NacosLoadBalancer error", e);
			return null;
		}
	}

}
