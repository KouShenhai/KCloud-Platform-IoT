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

package org.laokou.oss.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.oss.gatewayimpl.rpc.DistributedIdentifierRpc;
import org.laokou.oss.model.MqEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * @author laokou
 */
@Configuration
public class SystemConfig {

	static {
		ForyFactory.INSTANCE.register(org.laokou.oss.dto.domainevent.OssUploadEvent.class);
	}

	@Bean
	IdGenerator idGenerator(DistributedIdentifierRpc distributedIdentifierRpc) {
		return distributedIdentifierRpc::getId;
	}

	@Bean("authNewTopics")
	KafkaAdmin.NewTopics newTopics() {
		return new KafkaAdmin.NewTopics(new NewTopic(MqEnum.OSS_LOG_TOPIC, 3, (short) 1));
	}

}
