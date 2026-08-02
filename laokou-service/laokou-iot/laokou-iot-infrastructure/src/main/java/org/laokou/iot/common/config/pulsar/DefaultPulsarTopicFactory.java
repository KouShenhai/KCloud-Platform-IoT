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
import org.laokou.iot.session.dto.mqtt.MessageType;
import org.laokou.iot.session.dto.mqtt.MqttMessageType;

import java.util.Arrays;
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
		String namespace = String.format("%s/gateway", tenantCode);
		boolean exist = tenants.getTenants().contains(tenantCode);
		if (!exist) {
			tenants.createTenant(tenantCode, tenantInfo);
		}
		log.info("create topic for tenant code {}", tenantCode);
		exist = namespaces.getNamespaces(tenantCode).contains(namespace);
		if (!exist) {
			namespaces.createNamespace(namespace);
		}
		log.info("create topic for namespace {}", namespace);
		for (MqttMessageType mqttMessageType : Arrays.stream(MqttMessageType.values())
			.filter(item -> item.getMessageType() == MessageType.GATEWAY)
			.toList()) {
			createTopic(topics, namespace, mqttMessageType);
		}
	}

	private void createTopic(Topics topics, String namespace, MqttMessageType messageType) throws PulsarAdminException {
		String topic = getMqTopic(namespace, messageType);
		boolean exist = topics.getPartitionedTopicList(namespace).contains(topic);
		if (!exist) {
			topics.createPartitionedTopic(topic, messageType.getNumPartitions());
		}
		log.info("create topic {} for namespace {}", topic, namespace);
	}

	private String getMqTopic(String namespace, MqttMessageType messageType) {
		return String.format("persistent://%s/%s", namespace, messageType.getMqTopic());
	}

}
