/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.domain.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.SpringUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventScheduler {

	private final SpringUtil springUtil;

	private final RemoveDomainEventExecutor removeDomainEventExecutor;

	/**
	 * 每天凌晨1点执行.
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeDomainEvent() {
		try {
			removeDomainEventExecutor.execute(springUtil.getServiceId());
		}
		catch (Exception e) {
			log.error("定时执行删除前三个月的领域事件任务失败，错误信息：{}", e.getMessage(), e);
		}
	}

}
