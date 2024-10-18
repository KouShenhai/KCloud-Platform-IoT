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

package org.laokou.common.domain.model;

import lombok.Getter;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.dto.DefaultDomainEvent;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Getter
public class DomainEventA extends AggregateRoot<Long> {

	/**
	 * 事件类型.
	 */
	private final String eventType;

	/**
	 * MQ主题.
	 */
	private final String topic;

	/**
	 * 标签.
	 */
	private final String tag;

	/**
	 * 聚合根ID.
	 */
	private final Long aggregateId;

	/**
	 * 扩展属性.
	 */
	private final String attribute;

	public DomainEventA(byte[] payload, DefaultDomainEvent domainEvent) {
		super(domainEvent.getId());
		this.tenantId = domainEvent.getTenantId();
		this.creator = domainEvent.getCreator();
		this.editor = domainEvent.getEditor();
		this.eventType = domainEvent.getEventType().name();
		this.sourceName = domainEvent.getSourceName();
		this.serviceId = domainEvent.getServiceId();
		this.aggregateId = domainEvent.getAggregateId();
		this.tag = domainEvent.getTag();
		this.topic = domainEvent.getTopic();
		this.attribute = new String(payload, StandardCharsets.UTF_8);
	}

}
