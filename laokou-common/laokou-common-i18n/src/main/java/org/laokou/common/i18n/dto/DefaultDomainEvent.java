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

package org.laokou.common.i18n.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.common.constant.EventType;

import java.time.Instant;

/**
 * 默认领域事件.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
public class DefaultDomainEvent extends DomainEvent<Long> {

	protected DefaultDomainEvent(AggregateRoot<Long> aggregateRoot, String topic, String tag, EventType eventType,
			Instant instant) {
		generatorId();
		super.tenantId = aggregateRoot.getTenantId();
		super.creator = aggregateRoot.getCreator();
		super.editor = aggregateRoot.getEditor();
		super.eventType = eventType;
		super.sourcePrefix = aggregateRoot.getSourcePrefix();
		super.serviceId = aggregateRoot.getServiceId();
		super.aggregateId = aggregateRoot.getId();
		super.tag = tag;
		super.topic = topic;
		super.createTime = instant;
		super.updateTime = instant;
	}

	protected DefaultDomainEvent(String topic, String tag, EventType eventType, String serviceId, String sourcePrefix,
			Instant instant, Long aggregateId, Long tenantId) {
		generatorId();
		super.aggregateId = aggregateId;
		super.eventType = eventType;
		super.tag = tag;
		super.topic = topic;
		super.createTime = instant;
		super.updateTime = instant;
		super.serviceId = serviceId;
		super.sourcePrefix = sourcePrefix;
		super.tenantId = tenantId;
	}

	protected DefaultDomainEvent(String topic, String tag) {
		generatorId();
		super.topic = topic;
		super.tag = tag;
	}

	public DefaultDomainEvent(String serviceId) {
		super.serviceId = serviceId;
	}

	@Override
	protected void generatorId() {
		super.id = System.currentTimeMillis();
	}

}
