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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.dto.DefaultDomainEvent;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainEventA extends AggregateRoot<Long> {

	/**
	 * 事件类型.
	 */
	private String eventType;

	/**
	 * 事件状态.
	 */
	private String eventStatus;

	/**
	 * MQ主题.
	 */
	private String topic;

	/**
	 * 标签.
	 */
	private String tag;

	/**
	 * 聚合根ID.
	 */
	private Long aggregateId;

	/**
	 * 扩展属性.
	 */
	private String attribute;

	public DomainEventA(byte[] payload, DefaultDomainEvent domainEvent) {
		this.id = domainEvent.getId();
		this.tenantId = domainEvent.getTenantId();
		this.deptId = domainEvent.getDeptId();
		this.deptPath = domainEvent.getDeptPath();
		this.creator = domainEvent.getCreator();
		this.editor = domainEvent.getEditor();
		this.eventType = domainEvent.getEventType().name();
		this.sourceName = domainEvent.getSourceName();
		this.appName = domainEvent.getAppName();
		this.aggregateId = domainEvent.getAggregateId();
		this.tag = domainEvent.getTag();
		this.topic = domainEvent.getTopic();
		this.attribute = new String(payload, StandardCharsets.UTF_8);
	}

}
