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

package org.laokou.common.domain.publish;

import lombok.RequiredArgsConstructor;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.lock.annotation.Lock4j;
import org.springframework.stereotype.Component;

import static org.laokou.common.domain.publish.JobMode.ASYNC;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DomainEventJob {

	private final DomainEventPublisher domainEventPublisher;

	private final DomainEventService domainEventService;

	@Lock4j(key = "public_domain_event_job_lock", expire = 280000)
	// @XxlJob("publishDomainEventJobHandler")
	public void publishDomainEventJob() {
		// 定时任务 => 每5分钟运行一次
		//domainEventPublisher.publish(ASYNC);
	}

	@Lock4j(key = "remove_domain_event_job_lock", expire = 600000)
	// @XxlJob("removeDomainEventJobHandler")
	public void removeDomainEventJob() {
		// 定时任务 => 每天00:20执行一次（保留一个月的领域事件）
		String ymd = DateUtil.format(DateUtil.plusMonths(DateUtil.now(), -1), DateUtil.YYYYMMDD);
		domainEventService.removeLastMonth(ymd);
	}

}
