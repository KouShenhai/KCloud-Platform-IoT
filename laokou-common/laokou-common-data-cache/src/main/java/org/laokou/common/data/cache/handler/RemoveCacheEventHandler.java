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

package org.laokou.common.data.cache.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.laokou.common.data.cache.handler.event.RemovedCacheEvent;
import org.laokou.common.domain.handler.AbstractDomainEventHandler;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.BROADCASTING;
import static org.laokou.common.data.cache.constant.MqConstants.LAOKOU_CACHE_CONSUMER_GROUP;
import static org.laokou.common.data.cache.constant.MqConstants.LAOKOU_CACHE_TOPIC;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = LAOKOU_CACHE_CONSUMER_GROUP, topic = LAOKOU_CACHE_TOPIC,
		messageModel = BROADCASTING, consumeMode = CONCURRENTLY, consumeThreadMax = 128, consumeThreadNumber = 64)
public class RemoveCacheEventHandler extends AbstractDomainEventHandler {

	private final List<CacheManager> cacheManagers;

	@Override
	protected void handleDomainEvent(DomainEvent domainEvent) throws JsonProcessingException {
		RemovedCacheEvent evt = JacksonUtils.toBean(domainEvent.getPayload(), RemovedCacheEvent.class);
		cacheManagers.forEach(item -> {
			Cache cache = item.getCache(evt.name());
			if (ObjectUtils.isNotNull(cache)) {
				String key = evt.key();
				if (StringUtils.isNotEmpty(key)) {
					cache.evictIfPresent(key);
				}
				else {
					cache.clear();
				}
			}
		});
	}

}
