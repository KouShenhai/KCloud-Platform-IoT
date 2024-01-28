/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.domain.publish;

import lombok.RequiredArgsConstructor;
import org.laokou.common.domain.convertor.DomainEventConvertor;
import org.laokou.common.i18n.common.JobModeEnums;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.PropertiesConstants.SPRING_APPLICATION_NAME;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DomainEventPublishTask {

	private final Environment environment;

	private final Executor executor;

	private final RocketMqTemplate rocketMqTemplate;

	public void publishEvent(List<DomainEvent<Long>> list, JobModeEnums jobMode) {
		switch (jobMode) {
			case SYNC -> {
				String appName = environment.getProperty(SPRING_APPLICATION_NAME);
				list.forEach(item -> CompletableFuture.runAsync(() -> rocketMqTemplate
					.sendAsyncOrderlyMessage(item.getTopic(), DomainEventConvertor.toDataObject(item), appName),
						executor));
			}
			case ASYNC -> {

			}
			default -> {
			}
		}
	}

}
