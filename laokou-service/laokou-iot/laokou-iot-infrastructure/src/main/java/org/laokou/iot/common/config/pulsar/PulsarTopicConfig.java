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

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.PulsarClientException;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.pulsar.core.PulsarAdministration;

/**
 * @author laokou
 */
@Configuration
public class PulsarTopicConfig {

	/**
	 * 注册原生 PulsarAdmin.
	 * <p>
	 * destroyMethod = "close"： Spring 容器关闭时自动释放 HTTP 连接、线程等资源。
	 */
	@Bean(destroyMethod = "close")
	public PulsarAdmin pulsarAdmin(PulsarAdministration pulsarAdministration) throws PulsarClientException {
		return pulsarAdministration.createAdminClient();
	}

	@Bean(initMethod = "createTopic")
	public PulsarTopicFactory pulsarTopicFactory(PulsarAdmin pulsarAdmin,
			SystemSettingsProperties systemSettingsProperties) {
		return new DefaultPulsarTopicFactory(pulsarAdmin, systemSettingsProperties);
	}

}
