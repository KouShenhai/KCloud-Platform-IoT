/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.common.config.pulsar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.Namespaces;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.admin.Tenants;
import org.apache.pulsar.client.admin.Topics;
import org.apache.pulsar.common.policies.data.TenantInfoImpl;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.iot.session.model.enums.mqtt.MqttMessageType;
import org.laokou.iot.session.model.enums.MqTopic;

import java.util.HashSet;
import java.util.Set;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
final class DefaultPulsarTopicFactory implements PulsarTopicFactory {

	private final PulsarAdmin pulsarAdmin;

	private final SystemSettingsProperties systemSettingsProperties;

	@Override
	public void createTopic() throws PulsarAdminException {
		Tenants tenants = pulsarAdmin.tenants();
		Namespaces namespaces = pulsarAdmin.namespaces();
		Topics topics = pulsarAdmin.topics();
		TenantInfoImpl tenantInfo = TenantInfoImpl.builder()
			.adminRoles(Set.of())
			.allowedClusters(new HashSet<>(pulsarAdmin.clusters().getClusters()))
			.build();
		String tenantCode = systemSettingsProperties.getTenantCode();
		String gatewayNamespace = String.format("%s/gateway", tenantCode);
		String sessionNamespace = String.format("%s/session", tenantCode);
		boolean exist = tenants.getTenants().contains(tenantCode);
		if (!exist) {
			tenants.createTenant(tenantCode, tenantInfo);
		}
		log.info("create topic for tenant code {}", tenantCode);
		exist = namespaces.getNamespaces(tenantCode).contains(gatewayNamespace);
		if (!exist) {
			namespaces.createNamespace(gatewayNamespace);
		}
		log.info("create topic for namespace {}", gatewayNamespace);
		exist = namespaces.getNamespaces(tenantCode).contains(sessionNamespace);
		if (!exist) {
			namespaces.createNamespace(sessionNamespace);
		}
		log.info("create topic for namespace {}", sessionNamespace);
		for (MqttMessageType mqttMessageType : MqttMessageType.values()) {
			createTopic(topics, gatewayNamespace, mqttMessageType.getMqTopic(), mqttMessageType.getNumPartitions());
		}
		for (MqTopic mqTopic : MqTopic.values()) {
			createTopic(topics, sessionNamespace, mqTopic.getTopic(), mqTopic.getNumPartitions());
		}
	}

	private void createTopic(Topics topics, String namespace, String mqTopic, int numPartitions)
			throws PulsarAdminException {
		String topic = getMqTopic(namespace, mqTopic);
		boolean exist = topics.getPartitionedTopicList(namespace).contains(topic);
		if (!exist) {
			topics.createPartitionedTopic(topic, numPartitions);
		}
		log.info("create topic {} for namespace {}", topic, namespace);
	}

	private String getMqTopic(String namespace, String topic) {
		return String.format("persistent://%s/%s", namespace, topic);
	}

}
