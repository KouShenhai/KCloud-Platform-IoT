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

package org.laokou.common.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.laokou.common.kafka.template.DefaultKafkaTemplate;
import org.laokou.common.kafka.template.KafkaSender;
import org.laokou.common.kafka.template.ReactiveKafkaSender;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.internals.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author laokou
 */
@AutoConfiguration
public class KafkaAutoConfig {

	@Bean("defaultKafkaTemplate")
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	public DefaultKafkaTemplate defaultKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
		return new DefaultKafkaTemplate(kafkaTemplate);
	}

	@Bean("reactiveKafkaSender")
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	public KafkaSender reactiveKafkaSender(SenderOptions<String, String> senderOptions) {
		return new ReactiveKafkaSender(
				new reactor.kafka.sender.internals.DefaultKafkaSender<>(ProducerFactory.INSTANCE, senderOptions));
	}

	@Bean
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	public SenderOptions<String, String> senderOptions(KafkaProperties kafkaProperties) {
		Map<String, Object> props = new HashMap<>();
		KafkaProperties.Producer producer = kafkaProperties.getProducer();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		props.put(ProducerConfig.ACKS_CONFIG, producer.getAcks());
		props.put(ProducerConfig.RETRIES_CONFIG, producer.getRetries());
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, (int) producer.getBatchSize().toBytes());
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, (int) producer.getBufferMemory().toBytes());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return SenderOptions.create(props);
	}

	@Bean
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	public ReceiverOptions<String, String> receiverOptions(KafkaProperties kafkaProperties) {
		Map<String, Object> props = new HashMap<>();
		KafkaProperties.Consumer consumer = kafkaProperties.getConsumer();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, consumer.getGroupId());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumer.getMaxPollRecords());
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumer.getEnableAutoCommit());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return ReceiverOptions.create(props);
	}

}
