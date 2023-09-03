/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.common.data.cache.listener;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Component
@Slf4j
public class RedisKeyDeleteListener extends KeyDeleteEventMessageListener {

	private final Cache<String, Object> caffeineCache;

	private static final String ANY_MATCH_VALUE = ".*";

	public RedisKeyDeleteListener(RedisMessageListenerContainer listenerContainer,
			Cache<String, Object> caffeineCache) {
		super(listenerContainer);
		this.caffeineCache = caffeineCache;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String key = new String(message.getBody(), StandardCharsets.UTF_8);
		if (key.matches(RedisKeyUtil.getUserInfoKey(ANY_MATCH_VALUE))) {
			log.info("监听key为{}的删除事件", key);
			caffeineCache.invalidate(key);
		}
	}

}
