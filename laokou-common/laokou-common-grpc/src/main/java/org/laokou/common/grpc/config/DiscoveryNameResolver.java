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

package org.laokou.common.grpc.config;

import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.Status;
import io.grpc.StatusOr;
import org.laokou.common.core.util.CollectionExtUtils;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
final class DiscoveryNameResolver extends NameResolver {

	private final String serviceId;

	private final String grpcPortName;

	private final DiscoveryClient discoveryClient;

	private final ExecutorService executorService;

	private final ServiceConfigParser serviceConfigParser;

	private final AtomicReference<List<ServiceInstance>> serviceInstanceReference;

	private final AtomicBoolean resolving;

	private Listener2 listener;

	public DiscoveryNameResolver(String serviceId, DiscoveryClient discoveryClient, ExecutorService executorService,
			Args args) {
		this.serviceId = serviceId;
		this.grpcPortName = String.format("%s_grpc_port", serviceId.replaceAll("-", "_"));
		this.discoveryClient = discoveryClient;
		this.executorService = executorService;
		this.serviceInstanceReference = new AtomicReference<>(Collections.emptyList());
		this.serviceConfigParser = args.getServiceConfigParser();
		this.resolving = new AtomicBoolean(false);
	}

	@Override
	public String getServiceAuthority() {
		return serviceId;
	}

	@Override
	public void shutdown() {
		this.serviceInstanceReference.set(null);
	}

	@Override
	public void start(Listener2 listener) {
		this.listener = listener;
		resolve();
	}

	@Override
	public void refresh() {
		resolve();
	}

	public void refreshFromExternal() {
		executorService.execute(() -> {
			if (ObjectUtils.isNotNull(listener)) {
				resolve();
			}
		});
	}

	private void resolve() {
		if (this.resolving.compareAndSet(false, true)) {
			this.executorService.execute(() -> {
				this.serviceInstanceReference.set(resolveInternal());
				this.resolving.set(false);
			});
		}
	}

	private List<ServiceInstance> resolveInternal() {
		List<ServiceInstance> serviceInstances = serviceInstanceReference.get();
		List<ServiceInstance> newServiceInstanceList = this.discoveryClient.getInstances(this.serviceId);
		if (CollectionExtUtils.isEmpty(newServiceInstanceList)) {
			listener.onError(Status.UNAVAILABLE.withDescription("No servers found for " + serviceId));
			return Collections.emptyList();
		}
		if (!isUpdateServiceInstance(serviceInstances, newServiceInstanceList)) {
			return serviceInstances;
		}
		this.listener.onResult(ResolutionResult.newBuilder()
			.setAddressesOrError(StatusOr.fromValue(toAddresses(newServiceInstanceList)))
			.setServiceConfig(resolveServiceConfig(newServiceInstanceList))
			.build());
		return newServiceInstanceList;
	}

	private boolean isUpdateServiceInstance(List<ServiceInstance> serviceInstances,
			List<ServiceInstance> newServiceInstanceList) {
		if (serviceInstances.size() != newServiceInstanceList.size()) {
			return true;
		}
		Set<String> oldSet = serviceInstances.stream().map(this::getAddressStr).collect(Collectors.toSet());
		Set<String> newSet = newServiceInstanceList.stream().map(this::getAddressStr).collect(Collectors.toSet());
		return !ObjectUtils.equals(oldSet, newSet);
	}

	private ConfigOrError resolveServiceConfig(List<ServiceInstance> newServiceInstanceList) {
		String serviceConfig = getServiceConfig(newServiceInstanceList);
		if (StringExtUtils.hasText(serviceConfig)) {
			return serviceConfigParser
				.parseServiceConfig(JacksonUtils.toMap(serviceConfig, String.class, Object.class));
		}
		return null;
	}

	private String getServiceConfig(List<ServiceInstance> newServiceInstanceList) {
		if (CollectionExtUtils.isEmpty(newServiceInstanceList)) {
			return "";
		}
		return newServiceInstanceList.stream()
			.map(item -> ObjectUtils.requireNotNull(item.getMetadata()).getOrDefault("grpc_service_config", ""))
			.collect(Collectors.joining(","));
	}

	private List<EquivalentAddressGroup> toAddresses(List<ServiceInstance> newServiceInstanceList) {
		List<EquivalentAddressGroup> addresses = new ArrayList<>(newServiceInstanceList.size());
		for (ServiceInstance serviceInstance : newServiceInstanceList) {
			addresses.add(toAddress(serviceInstance));
		}
		return addresses;
	}

	private EquivalentAddressGroup toAddress(ServiceInstance serviceInstance) {
		String host = serviceInstance.getHost();
		int port = getGrpcPost(serviceInstance);
		return new EquivalentAddressGroup(new InetSocketAddress(host, port), getAttributes(serviceInstance));
	}

	private Attributes getAttributes(ServiceInstance serviceInstance) {
		return Attributes.newBuilder()
			.set(Attributes.Key.create("serviceId"), serviceId)
			.set(Attributes.Key.create("instanceId"), serviceInstance.getInstanceId())
			.build();
	}

	private String getAddressStr(ServiceInstance serviceInstance) {
		return serviceInstance.getHost() + ":" + getGrpcPost(serviceInstance);
	}

	private int getGrpcPost(ServiceInstance serviceInstance) {
		Map<String, String> metadata = serviceInstance.getMetadata();
		if (MapUtils.isNotEmpty(metadata)) {
			return Integer.parseInt(ObjectUtils.requireNotNull(metadata).getOrDefault(grpcPortName, "9090"));
		}
		return 9090;
	}

}
