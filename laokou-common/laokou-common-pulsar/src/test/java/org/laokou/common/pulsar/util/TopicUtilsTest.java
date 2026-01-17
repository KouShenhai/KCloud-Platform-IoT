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

package org.laokou.common.pulsar.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * TopicUtils test class.
 *
 * @author laokou
 */
class TopicUtilsTest {

	@Test
	@DisplayName("Test getTopic with valid parameters")
	void testGetTopicWithValidParameters() {
		String tenantCode = "tenant-001";
		String namespace = "default";
		String topic = "test-topic";

		String result = TopicUtils.getTopic(tenantCode, namespace, topic);

		Assertions.assertThat(result).isEqualTo("persistent://tenant-001/default/test-topic");
	}

	@Test
	@DisplayName("Test getTopic with different tenant codes")
	void testGetTopicWithDifferentTenantCodes() {
		String namespace = "events";
		String topic = "orders";

		String result1 = TopicUtils.getTopic("tenant-a", namespace, topic);
		String result2 = TopicUtils.getTopic("tenant-b", namespace, topic);

		Assertions.assertThat(result1).isEqualTo("persistent://tenant-a/events/orders");
		Assertions.assertThat(result2).isEqualTo("persistent://tenant-b/events/orders");
		Assertions.assertThat(result1).isNotEqualTo(result2);
	}

	@Test
	@DisplayName("Test getTopic with different namespaces")
	void testGetTopicWithDifferentNamespaces() {
		String tenantCode = "public";
		String topic = "messages";

		String result1 = TopicUtils.getTopic(tenantCode, "namespace-1", topic);
		String result2 = TopicUtils.getTopic(tenantCode, "namespace-2", topic);

		Assertions.assertThat(result1).isEqualTo("persistent://public/namespace-1/messages");
		Assertions.assertThat(result2).isEqualTo("persistent://public/namespace-2/messages");
	}

	@Test
	@DisplayName("Test getTopic with different topics")
	void testGetTopicWithDifferentTopics() {
		String tenantCode = "laokou";
		String namespace = "iot";

		String result1 = TopicUtils.getTopic(tenantCode, namespace, "device-events");
		String result2 = TopicUtils.getTopic(tenantCode, namespace, "sensor-data");

		Assertions.assertThat(result1).isEqualTo("persistent://laokou/iot/device-events");
		Assertions.assertThat(result2).isEqualTo("persistent://laokou/iot/sensor-data");
	}

	@Test
	@DisplayName("Test getTopic with empty strings")
	void testGetTopicWithEmptyStrings() {
		String result = TopicUtils.getTopic("", "", "");

		Assertions.assertThat(result).isEqualTo("persistent:////");
	}

	@Test
	@DisplayName("Test getTopic with special characters in topic name")
	void testGetTopicWithSpecialCharacters() {
		String tenantCode = "tenant_01";
		String namespace = "ns-default";
		String topic = "topic_v1.0";

		String result = TopicUtils.getTopic(tenantCode, namespace, topic);

		Assertions.assertThat(result).isEqualTo("persistent://tenant_01/ns-default/topic_v1.0");
	}

	@Test
	@DisplayName("Test getTopic with Chinese characters")
	void testGetTopicWithChineseCharacters() {
		String tenantCode = "租户A";
		String namespace = "命名空间";
		String topic = "主题";

		String result = TopicUtils.getTopic(tenantCode, namespace, topic);

		Assertions.assertThat(result).isEqualTo("persistent://租户A/命名空间/主题");
	}

	@Test
	@DisplayName("Test getTopic result starts with persistent protocol")
	void testGetTopicStartsWithPersistent() {
		String result = TopicUtils.getTopic("any-tenant", "any-namespace", "any-topic");

		Assertions.assertThat(result).startsWith("persistent://");
	}

	@Test
	@DisplayName("Test getTopic format consistency")
	void testGetTopicFormatConsistency() {
		String tenantCode = "test";
		String namespace = "ns";
		String topic = "tp";

		String result = TopicUtils.getTopic(tenantCode, namespace, topic);

		// Verify the format: persistent://{tenantCode}/{namespace}/{topic}
		String[] parts = result.split("/");
		Assertions.assertThat(parts).hasSize(5);
		Assertions.assertThat(parts[0]).isEqualTo("persistent:");
		Assertions.assertThat(parts[1]).isEmpty(); // between :// and tenant
		Assertions.assertThat(parts[2]).isEqualTo(tenantCode);
		Assertions.assertThat(parts[3]).isEqualTo(namespace);
		Assertions.assertThat(parts[4]).isEqualTo(topic);
	}

}
