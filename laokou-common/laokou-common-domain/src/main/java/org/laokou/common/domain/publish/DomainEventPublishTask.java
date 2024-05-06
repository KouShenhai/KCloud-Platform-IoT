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

package org.laokou.common.domain.publish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.domain.convertor.DomainEventConvertor;
import org.laokou.common.domain.database.dataobject.DomainEventDO;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.common.EventStatusEnum;
import org.laokou.common.i18n.common.JobModeEnum;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.mybatisplus.utils.DynamicUtil;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.core.config.TaskExecutorConfig.THREAD_POOL_TASK_EXECUTOR_NAME;
import static org.laokou.common.i18n.common.EventStatusEnum.PUBLISH_FAILED;
import static org.laokou.common.i18n.common.EventStatusEnum.PUBLISH_SUCCEED;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventPublishTask {

	private final Executor executor;

	private final DomainEventService domainEventService;

	private final Environment environment;

	private final RocketMqTemplate rocketMqTemplate;

	private final DynamicUtil dynamicUtil;

	@Async(THREAD_POOL_TASK_EXECUTOR_NAME)
	public void publishEvent(List<DomainEvent<Long>> list, JobModeEnum jobMode) {
		List<DomainEvent<Long>> modifyList = Collections.synchronizedList(new ArrayList<>(16));
		switch (jobMode) {
			case SYNC -> {
				if (CollectionUtil.isNotEmpty(list)) {
					List<CompletableFuture<Void>> futures = list.stream()
						.map(item -> CompletableFuture.runAsync(() -> handleMqSend(modifyList, item), executor))
						.toList();
					// 阻塞所有任务
					CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
					// 批量修改事件状态
					domainEventService.modify(modifyList);
					modifyList.clear();
				}
			}
			case ASYNC -> jobHandler(modifyList);
		}
	}

	private void jobHandler(List<DomainEvent<Long>> modifyList) {
		int chunkSize = 1000;
		List<DomainEventDO> list = Collections.synchronizedList(new ArrayList<>(chunkSize));
		domainEventService.findList(getSourceNames(), getAppName(), resultContext -> {
			list.add(resultContext.getResultObject());
			if (list.size() % chunkSize == 0) {
				mqSend(modifyList, list);
			}
		});
		if (list.size() % chunkSize != 0) {
			mqSend(modifyList, list);
		}
	}

	private void mqSend(List<DomainEvent<Long>> modifyList, List<DomainEventDO> list) {
		List<CompletableFuture<Void>> futures = list.stream()
			.map(item -> CompletableFuture.runAsync(() -> handleMqSend(modifyList, item), executor))
			.toList();
		// 阻塞所有任务
		CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
		// 批量修改事件状态
		domainEventService.modify(modifyList);
		modifyList.clear();
		list.clear();
	}

	private String getAppName() {
		return environment.getProperty("spring.application.name");
	}

	private Set<String> getSourceNames() {
		return dynamicUtil.getDataSources().keySet();
	}

	private void handleMqSend(List<DomainEvent<Long>> modifyList, DomainEventDO item) {
		// 同步发送并修改事件状态
		boolean result = rocketMqTemplate.sendSyncOrderlyMessage(item.getTopic(), item, item.getAppName());
		addEvent(result, modifyList, item.getId(), item.getSourceName());
	}

	private void handleMqSend(List<DomainEvent<Long>> modifyList, DomainEvent<Long> item) {
		// 同步发送并修改事件状态
		boolean result = rocketMqTemplate.sendSyncOrderlyMessage(item.getTopic(),
				DomainEventConvertor.toDataObject(item), item.getAppName());
		addEvent(result, modifyList, item.getId(), item.getSourceName());
	}

	private void addEvent(boolean result, List<DomainEvent<Long>> modifyList, Long id, String sourceName) {
		if (result) {
			// 发布成功
			addEvent(modifyList, id, sourceName, PUBLISH_SUCCEED);
		}
		else {
			// 发布失败
			addEvent(modifyList, id, sourceName, PUBLISH_FAILED);
		}
	}

	private void addEvent(List<DomainEvent<Long>> modifyList, Long id, String sourceName,
			EventStatusEnum eventStatusEnum) {
		modifyList.add(new DefaultDomainEvent(id, eventStatusEnum, sourceName));
	}

}
