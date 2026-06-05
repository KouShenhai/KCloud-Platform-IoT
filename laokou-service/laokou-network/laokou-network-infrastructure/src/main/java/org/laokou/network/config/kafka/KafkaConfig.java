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

package org.laokou.network.config.kafka;

import lombok.Data;

/**
 * Kafka Vert.x client config.
 *
 * @author laokou
 */
@Data
public class KafkaConfig {

	private String bootstrapServers = "127.0.0.1:9092";

	private String clientId = "laokou-network";

	private String groupId = "laokou-network";

	private String topic = "";

	private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";

	private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";

	private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

	private String valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

	private String acks = "1";

	private String autoOffsetReset = "latest";

	private boolean enableAutoCommit = false;

	private int maxPollRecords = 500;

	private int requestTimeoutMs = 30000;

	private int deliveryTimeoutMs = 120000;

	private String compressionType = "none";

	private String securityProtocol = "PLAINTEXT";

	private String saslMechanism = "";

	private String username = "";

	private String password = "";

}
