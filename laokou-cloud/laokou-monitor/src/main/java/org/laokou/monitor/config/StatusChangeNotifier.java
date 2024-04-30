/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.monitor.config;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import io.micrometer.common.lang.NonNullApi;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 通知配置.
 *
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
public class StatusChangeNotifier extends AbstractStatusChangeNotifier {

	@Schema(name = "DOWN_STATUS", description = "未通过健康检查")
	private static final String DOWN_STATUS = "DOWN";

	@Schema(name = "OFFLINE_STATUS", description = "离线")
	private static final String OFFLINE_STATUS = "OFFLINE";

	@Schema(name = "UP_STATUS", description = "上线")
	private static final String UP_STATUS = "UP";

	@Schema(name = "UNKNOWN_STATUS", description = "未知异常")
	private static final String UNKNOWN_STATUS = "UNKNOWN";

	public StatusChangeNotifier(InstanceRepository instanceRepository) {
		super(instanceRepository);
	}

	/**
	 * 通知消息.
	 * @param event 事件
	 * @param instance 实例
	 * @return 消息
	 */
	@Override
	protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
		return Mono.fromRunnable(() -> {
			if (event instanceof InstanceStatusChangedEvent eventStatus) {
				String status = eventStatus.getStatusInfo().getStatus();
				switch (status) {
					case DOWN_STATUS -> log.info("未通过健康检查");
					case OFFLINE_STATUS -> log.info("离线");
					case UP_STATUS -> log.info("上线");
					case UNKNOWN_STATUS -> log.error("未知异常");
				}
			}
		});
	}

}
