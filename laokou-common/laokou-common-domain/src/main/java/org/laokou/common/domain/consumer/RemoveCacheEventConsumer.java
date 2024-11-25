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

package org.laokou.common.domain.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.event.RemoveCacheEvent;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.BROADCASTING;
import static org.laokou.common.domain.constant.MqConstant.LAOKOU_CACHE_CONSUMER_GROUP;
import static org.laokou.common.domain.constant.MqConstant.LAOKOU_CACHE_TOPIC;

/**
 * @author laokou
 */
@Component
@RocketMQMessageListener(consumerGroup = LAOKOU_CACHE_CONSUMER_GROUP, topic = LAOKOU_CACHE_TOPIC,
		messageModel = BROADCASTING, consumeMode = CONCURRENTLY)
public class RemoveCacheEventConsumer extends AbstractDomainEventConsumer {

	private final List<CacheManager> cacheManagers;

	public RemoveCacheEventConsumer(DomainEventPublisher rocketMQDomainEventPublisher,
			List<CacheManager> cacheManagers) {
		super(rocketMQDomainEventPublisher);
		this.cacheManagers = cacheManagers;
	}

	@Override
	protected void handleDomainEvent(DefaultDomainEvent domainEvent) {
		RemoveCacheEvent event = (RemoveCacheEvent) domainEvent;
		cacheManagers.forEach(item -> {
			Cache cache = item.getCache(event.getName());
			if (ObjectUtil.isNotNull(cache)) {
				cache.evictIfPresent(event.getKey());
			}
		});
	}

	@Override
	protected DefaultDomainEvent convert(String msg) {
		return JacksonUtil.toBean(msg, RemoveCacheEvent.class);
	}

}
