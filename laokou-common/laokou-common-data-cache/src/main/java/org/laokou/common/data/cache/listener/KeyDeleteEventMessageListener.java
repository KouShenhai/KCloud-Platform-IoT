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

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisKeyspaceEvent;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.lang.Nullable;

/**
 * @author laokou
 */
public class KeyDeleteEventMessageListener extends KeyspaceEventMessageListener implements MessageListener {

	private static final Topic KEYEVENT_DELETE_TOPIC = new PatternTopic("__keyevent@*__:del");

	@Nullable
	private ApplicationEventPublisher publisher;

	public KeyDeleteEventMessageListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	protected void doRegister(RedisMessageListenerContainer listenerContainer) {
		listenerContainer.addMessageListener(this, KEYEVENT_DELETE_TOPIC);
	}

	protected void doHandleMessage(Message message) {
		this.publishEvent(new RedisKeyspaceEvent(message.getBody()));
	}

	protected void publishEvent(RedisKeyspaceEvent event) {
		if (this.publisher != null) {
			this.publisher.publishEvent(event);
		}

	}

	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

}
