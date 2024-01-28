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
import org.laokou.common.domain.holder.DomainEventContextHolder;
import org.laokou.common.i18n.common.JobModeEnums;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.SysConstants.THREAD_POOL_TASK_EXECUTOR_NAME;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class AsyncDomainEventPublisher implements DomainEventPublisher {

	private final DomainEventPublishTask domainEventPublishTask;

	@Async(THREAD_POOL_TASK_EXECUTOR_NAME)
	@Override
	public void publish(JobModeEnums jobModeEnums) {
		domainEventPublishTask.publishEvent(DomainEventContextHolder.get(), jobModeEnums);
	}

}
