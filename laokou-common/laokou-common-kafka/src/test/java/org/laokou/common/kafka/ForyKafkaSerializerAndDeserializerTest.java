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

package org.laokou.common.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.kafka.config.ForyKafkaDeserializer;
import org.laokou.common.kafka.config.ForyKafkaSerializer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * ForyKafkaSerializer 和 ForyKafkaDeserializer 测试类. 使用 Testcontainers 进行 Kafka 集成测试.
 *
 * @author laokou
 */
@Testcontainers
class ForyKafkaSerializerAndDeserializerTest {

	@Container
	static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageNames.kafka()).withReuse(true);

	private final ForyKafkaSerializer serializer = new ForyKafkaSerializer();

	private final ForyKafkaDeserializer deserializer = new ForyKafkaDeserializer();

	@BeforeAll
	static void setup() {
		// 注册测试用的类到 ForyFactory
		ForyFactory.INSTANCE.register(TestMessage.class);
		ForyFactory.INSTANCE.register(TestOrder.class);
		ForyFactory.INSTANCE.register(ArrayList.class);
	}

	// ==================== 单元测试 ====================

	@Test
	@DisplayName("Test null object serialization returns empty byte array")
	void testSerializeNull() {
		byte[] result = serializer.serialize("test-topic", null);
		Assertions.assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("Test string serialization to UTF-8 bytes")
	void testSerializeString() {
		String testString = "Hello, Kafka!";
		byte[] result = serializer.serialize("test-topic", testString);
		Assertions.assertThat(result).isEqualTo(testString.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	@DisplayName("Test Chinese string serialization")
	void testSerializeChineseString() {
		String chineseString = "你好，Kafka！";
		byte[] result = serializer.serialize("test-topic", chineseString);
		Assertions.assertThat(result).isEqualTo(chineseString.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	@DisplayName("Test simple object serialization and deserialization")
	void testSerializeAndDeserializeSimpleObject() {
		TestMessage message = new TestMessage(1L, "Test Message", 100);

		byte[] serialized = serializer.serialize("test-topic", message);
		Assertions.assertThat(serialized).isNotEmpty();

		Object deserialized = deserializer.deserialize("test-topic", serialized);
		Assertions.assertThat(deserialized).isInstanceOf(TestMessage.class);

		TestMessage result = (TestMessage) deserialized;
		Assertions.assertThat(result.getId()).isEqualTo(message.getId());
		Assertions.assertThat(result.getContent()).isEqualTo(message.getContent());
		Assertions.assertThat(result.getPriority()).isEqualTo(message.getPriority());
	}

	@Test
	@DisplayName("Test complex nested object serialization and deserialization")
	void testSerializeAndDeserializeComplexObject() {
		List<String> items = new ArrayList<>();
		items.add("商品A");
		items.add("商品B");
		items.add("商品C");
		TestOrder order = new TestOrder("ORD-001", 199.99, items);

		byte[] serialized = serializer.serialize("test-topic", order);
		Assertions.assertThat(serialized).isNotEmpty();

		Object deserialized = deserializer.deserialize("test-topic", serialized);
		Assertions.assertThat(deserialized).isInstanceOf(TestOrder.class);

		TestOrder result = (TestOrder) deserialized;
		Assertions.assertThat(result.getOrderId()).isEqualTo(order.getOrderId());
		Assertions.assertThat(result.getTotalAmount()).isEqualTo(order.getTotalAmount());
		Assertions.assertThat(result.getItems()).containsExactlyElementsOf(order.getItems());
	}

	@Test
	@DisplayName("Test object with null fields serialization and deserialization")
	void testSerializeAndDeserializeWithNullFields() {
		TestMessage message = new TestMessage(2L, null, 0);

		byte[] serialized = serializer.serialize("test-topic", message);
		Assertions.assertThat(serialized).isNotEmpty();

		Object deserialized = deserializer.deserialize("test-topic", serialized);
		Assertions.assertThat(deserialized).isInstanceOf(TestMessage.class);

		TestMessage result = (TestMessage) deserialized;
		Assertions.assertThat(result.getId()).isEqualTo(2L);
		Assertions.assertThat(result.getContent()).isNull();
		Assertions.assertThat(result.getPriority()).isZero();
	}

	@Test
	@DisplayName("Test object with empty list serialization and deserialization")
	void testSerializeAndDeserializeWithEmptyList() {
		TestOrder order = new TestOrder("ORD-002", 0.0, new ArrayList<>());

		byte[] serialized = serializer.serialize("test-topic", order);
		Assertions.assertThat(serialized).isNotEmpty();

		Object deserialized = deserializer.deserialize("test-topic", serialized);
		Assertions.assertThat(deserialized).isInstanceOf(TestOrder.class);

		TestOrder result = (TestOrder) deserialized;
		Assertions.assertThat(result.getOrderId()).isEqualTo("ORD-002");
		Assertions.assertThat(result.getTotalAmount()).isZero();
		Assertions.assertThat(result.getItems()).isEmpty();
	}

	// ==================== Kafka 集成测试 ====================

	@Test
	@DisplayName("Test message transmission via Kafka Producer/Consumer")
	void testKafkaProducerConsumerWithForySerializer() {
		String topic = "test-topic-" + UUID.randomUUID();
		TestMessage message = new TestMessage(100L, "Kafka Integration Test", 50);

		// 创建 Producer 配置
		Properties producerProps = new Properties();
		producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ForyKafkaSerializer.class.getName());

		// 创建 Consumer 配置
		Properties consumerProps = new Properties();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-" + UUID.randomUUID());
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ForyKafkaDeserializer.class.getName());
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		// 发送消息
		try (KafkaProducer<String, Object> producer = new KafkaProducer<>(producerProps)) {
			ProducerRecord<String, Object> record = new ProducerRecord<>(topic, "key-1", message);
			producer.send(record);
			producer.flush();
		}

		// 接收消息
		try (KafkaConsumer<String, Object> consumer = new KafkaConsumer<>(consumerProps)) {
			consumer.subscribe(Collections.singletonList(topic));
			ConsumerRecords<String, Object> records = consumer.poll(Duration.ofSeconds(30));

			Assertions.assertThat(records.count()).isEqualTo(1);

			for (ConsumerRecord<String, Object> record : records) {
				Assertions.assertThat(record.key()).isEqualTo("key-1");
				Assertions.assertThat(record.value()).isInstanceOf(TestMessage.class);

				TestMessage received = (TestMessage) record.value();
				Assertions.assertThat(received.getId()).isEqualTo(message.getId());
				Assertions.assertThat(received.getContent()).isEqualTo(message.getContent());
				Assertions.assertThat(received.getPriority()).isEqualTo(message.getPriority());
			}
		}
	}

	@Test
	@DisplayName("Test complex object transmission via Kafka")
	void testKafkaProducerConsumerWithComplexObject() {
		String topic = "test-complex-topic-" + UUID.randomUUID();
		List<String> items = new ArrayList<>();
		items.add("Item1");
		items.add("Item2");
		TestOrder order = new TestOrder("ORD-KAFKA-001", 999.99, items);

		// 创建 Producer 配置
		Properties producerProps = new Properties();
		producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ForyKafkaSerializer.class.getName());

		// 创建 Consumer 配置
		Properties consumerProps = new Properties();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-" + UUID.randomUUID());
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ForyKafkaDeserializer.class.getName());
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		// 发送消息
		try (KafkaProducer<String, Object> producer = new KafkaProducer<>(producerProps)) {
			ProducerRecord<String, Object> record = new ProducerRecord<>(topic, "order-key", order);
			producer.send(record);
			producer.flush();
		}

		// 接收消息
		try (KafkaConsumer<String, Object> consumer = new KafkaConsumer<>(consumerProps)) {
			consumer.subscribe(Collections.singletonList(topic));
			ConsumerRecords<String, Object> records = consumer.poll(Duration.ofSeconds(30));

			Assertions.assertThat(records.count()).isEqualTo(1);

			for (ConsumerRecord<String, Object> record : records) {
				Assertions.assertThat(record.value()).isInstanceOf(TestOrder.class);

				TestOrder received = (TestOrder) record.value();
				Assertions.assertThat(received.getOrderId()).isEqualTo(order.getOrderId());
				Assertions.assertThat(received.getTotalAmount()).isEqualTo(order.getTotalAmount());
				Assertions.assertThat(received.getItems()).containsExactlyElementsOf(order.getItems());
			}
		}
	}

	@Test
	@DisplayName("Test sending and receiving multiple messages")
	void testKafkaMultipleMessages() {
		String topic = "test-multi-topic-" + UUID.randomUUID();

		// 创建 Producer 配置
		Properties producerProps = new Properties();
		producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ForyKafkaSerializer.class.getName());

		// 创建 Consumer 配置
		Properties consumerProps = new Properties();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-" + UUID.randomUUID());
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ForyKafkaDeserializer.class.getName());
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		// 发送多条消息
		try (KafkaProducer<String, Object> producer = new KafkaProducer<>(producerProps)) {
			for (int i = 0; i < 5; i++) {
				TestMessage message = new TestMessage((long) i, "Message " + i, i * 10);
				ProducerRecord<String, Object> record = new ProducerRecord<>(topic, "key-" + i, message);
				producer.send(record);
			}
			producer.flush();
		}

		// 接收消息
		try (KafkaConsumer<String, Object> consumer = new KafkaConsumer<>(consumerProps)) {
			consumer.subscribe(Collections.singletonList(topic));
			ConsumerRecords<String, Object> records = consumer.poll(Duration.ofSeconds(30));

			Assertions.assertThat(records.count()).isEqualTo(5);

			int count = 0;
			for (ConsumerRecord<String, Object> record : records) {
				Assertions.assertThat(record.value()).isInstanceOf(TestMessage.class);
				TestMessage received = (TestMessage) record.value();
				Assertions.assertThat(received.getId()).isEqualTo((long) count);
				Assertions.assertThat(received.getContent()).isEqualTo("Message " + count);
				Assertions.assertThat(received.getPriority()).isEqualTo(count * 10);
				count++;
			}
		}
	}

	/**
	 * 测试用消息类.
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class TestMessage implements Serializable {

		private Long id;

		private String content;

		private Integer priority;

	}

	/**
	 * 测试用订单类.
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class TestOrder implements Serializable {

		private String orderId;

		private Double totalAmount;

		private List<String> items;

	}

}
