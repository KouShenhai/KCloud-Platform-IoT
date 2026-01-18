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

package org.laokou.common.pulsar.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.pulsar.PulsarContainer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ForySchema integration test with Pulsar Testcontainers.
 *
 * @author laokou
 */
@Testcontainers
class ForySchemaIntegrationTest {

	@Container
	static PulsarContainer pulsarContainer = new PulsarContainer(DockerImageNames.pulsar()).withReuse(true);

	private static PulsarClient pulsarClient;

	static {
		// Register test classes for Fory serialization
		ForyFactory.INSTANCE.register(PulsarTestMessage.class);
		ForyFactory.INSTANCE.register(PulsarTestEvent.class);
		ForyFactory.INSTANCE.register(ArrayList.class);
	}

	@BeforeAll
	static void setup() throws PulsarClientException {

		// Create Pulsar client
		pulsarClient = PulsarClient.builder().serviceUrl(pulsarContainer.getPulsarBrokerUrl()).build();
	}

	@AfterAll
	static void tearDown() throws PulsarClientException {
		if (pulsarClient != null) {
			pulsarClient.close();
		}
	}

	@Test
	@DisplayName("Test send and receive simple message with ForySchema")
	void testSendAndReceiveSimpleMessage() throws PulsarClientException {
		String topic = "persistent://public/default/test-simple-message";
		PulsarTestMessage message = new PulsarTestMessage(1L, "Hello Pulsar!", 100);

		// Create producer with ForySchema
		try (Producer<Object> producer = pulsarClient.newProducer(ForySchema.INSTANCE).topic(topic).create()) {

			// Send message
			MessageId messageId = producer.send(message);
			Assertions.assertThat(messageId).isNotNull();
		}

		// Create consumer with ForySchema
		try (Consumer<Object> consumer = pulsarClient.newConsumer(ForySchema.INSTANCE)
			.topic(topic)
			.subscriptionName("test-subscription")
			.subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
			.subscribe()) {

			// Receive message
			Message<Object> received = consumer.receive(10, TimeUnit.SECONDS);
			Assertions.assertThat(received).isNotNull();

			Object value = received.getValue();
			Assertions.assertThat(value).isInstanceOf(PulsarTestMessage.class);

			PulsarTestMessage result = (PulsarTestMessage) value;
			Assertions.assertThat(result.getId()).isEqualTo(message.getId());
			Assertions.assertThat(result.getContent()).isEqualTo(message.getContent());
			Assertions.assertThat(result.getPriority()).isEqualTo(message.getPriority());

			consumer.acknowledge(received);
		}
	}

	@Test
	@DisplayName("Test send and receive complex message with nested list")
	void testSendAndReceiveComplexMessage() throws PulsarClientException {
		String topic = "persistent://public/default/test-complex-message";
		List<String> payloads = new ArrayList<>();
		payloads.add("Payload A");
		payloads.add("Payload B");
		payloads.add("Payload C");
		PulsarTestEvent event = new PulsarTestEvent("EVT-001", "device-sensor", payloads);

		// Create producer with ForySchema
		try (Producer<Object> producer = pulsarClient.newProducer(ForySchema.INSTANCE).topic(topic).create()) {

			// Send message
			MessageId messageId = producer.send(event);
			Assertions.assertThat(messageId).isNotNull();
		}

		// Create consumer with ForySchema
		try (Consumer<Object> consumer = pulsarClient.newConsumer(ForySchema.INSTANCE)
			.topic(topic)
			.subscriptionName("test-complex-subscription")
			.subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
			.subscribe()) {

			// Receive message
			Message<Object> received = consumer.receive(10, TimeUnit.SECONDS);
			Assertions.assertThat(received).isNotNull();

			Object value = received.getValue();
			Assertions.assertThat(value).isInstanceOf(PulsarTestEvent.class);

			PulsarTestEvent result = (PulsarTestEvent) value;
			Assertions.assertThat(result.getEventId()).isEqualTo(event.getEventId());
			Assertions.assertThat(result.getEventType()).isEqualTo(event.getEventType());
			Assertions.assertThat(result.getPayloads()).containsExactlyElementsOf(event.getPayloads());

			consumer.acknowledge(received);
		}
	}

	@Test
	@DisplayName("Test send and receive multiple messages")
	void testSendAndReceiveMultipleMessages() throws PulsarClientException {
		String topic = "persistent://public/default/test-multiple-messages";
		int messageCount = 5;

		// Create producer with ForySchema
		try (Producer<Object> producer = pulsarClient.newProducer(ForySchema.INSTANCE).topic(topic).create()) {

			// Send multiple messages
			for (int i = 0; i < messageCount; i++) {
				PulsarTestMessage message = new PulsarTestMessage((long) i, "Message " + i, i * 10);
				MessageId messageId = producer.send(message);
				Assertions.assertThat(messageId).isNotNull();
			}
		}

		// Create consumer with ForySchema
		try (Consumer<Object> consumer = pulsarClient.newConsumer(ForySchema.INSTANCE)
			.topic(topic)
			.subscriptionName("test-multi-subscription")
			.subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
			.subscribe()) {

			// Receive multiple messages
			for (int i = 0; i < messageCount; i++) {
				Message<Object> received = consumer.receive(10, TimeUnit.SECONDS);
				Assertions.assertThat(received).isNotNull();

				Object value = received.getValue();
				Assertions.assertThat(value).isInstanceOf(PulsarTestMessage.class);

				PulsarTestMessage result = (PulsarTestMessage) value;
				Assertions.assertThat(result.getId()).isEqualTo(i);
				Assertions.assertThat(result.getContent()).isEqualTo("Message " + i);
				Assertions.assertThat(result.getPriority()).isEqualTo(i * 10);

				consumer.acknowledge(received);
			}
		}
	}

	@Test
	@DisplayName("Test send message with null fields")
	void testSendAndReceiveMessageWithNullFields() throws PulsarClientException {
		String topic = "persistent://public/default/test-null-fields";
		PulsarTestMessage message = new PulsarTestMessage(2L, null, 0);

		// Create producer with ForySchema
		try (Producer<Object> producer = pulsarClient.newProducer(ForySchema.INSTANCE).topic(topic).create()) {

			// Send message
			MessageId messageId = producer.send(message);
			Assertions.assertThat(messageId).isNotNull();
		}

		// Create consumer with ForySchema
		try (Consumer<Object> consumer = pulsarClient.newConsumer(ForySchema.INSTANCE)
			.topic(topic)
			.subscriptionName("test-null-subscription")
			.subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
			.subscribe()) {

			// Receive message
			Message<Object> received = consumer.receive(10, TimeUnit.SECONDS);
			Assertions.assertThat(received).isNotNull();

			Object value = received.getValue();
			Assertions.assertThat(value).isInstanceOf(PulsarTestMessage.class);

			PulsarTestMessage result = (PulsarTestMessage) value;
			Assertions.assertThat(result.getId()).isEqualTo(2L);
			Assertions.assertThat(result.getContent()).isNull();
			Assertions.assertThat(result.getPriority()).isZero();

			consumer.acknowledge(received);
		}
	}

	@Test
	@DisplayName("Test send event with empty list")
	void testSendAndReceiveEventWithEmptyList() throws PulsarClientException {
		String topic = "persistent://public/default/test-empty-list";
		PulsarTestEvent event = new PulsarTestEvent("EVT-002", "test-event", new ArrayList<>());

		// Create producer with ForySchema
		try (Producer<Object> producer = pulsarClient.newProducer(ForySchema.INSTANCE).topic(topic).create()) {

			// Send message
			MessageId messageId = producer.send(event);
			Assertions.assertThat(messageId).isNotNull();
		}

		// Create consumer with ForySchema
		try (Consumer<Object> consumer = pulsarClient.newConsumer(ForySchema.INSTANCE)
			.topic(topic)
			.subscriptionName("test-empty-list-subscription")
			.subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
			.subscribe()) {

			// Receive message
			Message<Object> received = consumer.receive(10, TimeUnit.SECONDS);
			Assertions.assertThat(received).isNotNull();

			Object value = received.getValue();
			Assertions.assertThat(value).isInstanceOf(PulsarTestEvent.class);

			PulsarTestEvent result = (PulsarTestEvent) value;
			Assertions.assertThat(result.getEventId()).isEqualTo("EVT-002");
			Assertions.assertThat(result.getEventType()).isEqualTo("test-event");
			Assertions.assertThat(result.getPayloads()).isEmpty();

			consumer.acknowledge(received);
		}
	}

	@Test
	@DisplayName("Test async message sending and receiving")
	void testAsyncSendAndReceive() throws Exception {
		String topic = "persistent://public/default/test-async-message";
		PulsarTestMessage message = new PulsarTestMessage(3L, "Async Message", 200);

		// Create producer with ForySchema
		try (Producer<Object> producer = pulsarClient.newProducer(ForySchema.INSTANCE).topic(topic).create()) {

			// Send message asynchronously
			MessageId messageId = producer.sendAsync(message).get(10, TimeUnit.SECONDS);
			Assertions.assertThat(messageId).isNotNull();
		}

		// Create consumer with ForySchema
		try (Consumer<Object> consumer = pulsarClient.newConsumer(ForySchema.INSTANCE)
			.topic(topic)
			.subscriptionName("test-async-subscription")
			.subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
			.subscribe()) {

			// Receive message
			Message<Object> received = consumer.receive(10, TimeUnit.SECONDS);
			Assertions.assertThat(received).isNotNull();

			Object value = received.getValue();
			Assertions.assertThat(value).isInstanceOf(PulsarTestMessage.class);

			PulsarTestMessage result = (PulsarTestMessage) value;
			Assertions.assertThat(result.getId()).isEqualTo(3L);
			Assertions.assertThat(result.getContent()).isEqualTo("Async Message");
			Assertions.assertThat(result.getPriority()).isEqualTo(200);

			consumer.acknowledgeAsync(received).get(5, TimeUnit.SECONDS);
		}
	}

	// ==================== Test Classes ====================

	/**
	 * Test message class for Pulsar integration.
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class PulsarTestMessage implements Serializable {

		private Long id;

		private String content;

		private Integer priority;

	}

	/**
	 * Test event class for Pulsar integration.
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class PulsarTestEvent implements Serializable {

		private String eventId;

		private String eventType;

		private List<String> payloads;

	}

}
