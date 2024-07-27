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

import java.time.LocalDateTime;

/**
 * 默认领域事件.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
public class DefaultDomainEvent extends DomainEvent<Long> {

	@Override
	protected void generatorId() {
		super.id = System.currentTimeMillis();
	}

	public void create(AggregateRoot<Long> aggregateRoot, String topic, String tag, EventType eventType,
			LocalDateTime timestamp) {
		generatorId();
		super.tenantId = aggregateRoot.getTenantId();
		super.deptId = aggregateRoot.getDeptId();
		super.deptPath = aggregateRoot.getDeptPath();
		super.creator = aggregateRoot.getCreator();
		super.editor = aggregateRoot.getEditor();
		super.eventType = eventType;
		super.sourceName = aggregateRoot.getSourceName();
		super.appName = aggregateRoot.getAppName();
		super.aggregateId = aggregateRoot.getId();
		super.tag = tag;
		super.topic = topic;
		super.createDate = timestamp;
		super.updateDate = timestamp;
	}

	public void create(String topic, String tag, EventType eventType, String appName, String sourceName,
			LocalDateTime timestamp, Long aggregateId) {
		generatorId();
		super.aggregateId = aggregateId;
		super.eventType = eventType;
		super.tag = tag;
		super.topic = topic;
		super.createDate = timestamp;
		super.updateDate = timestamp;
		super.appName = appName;
		super.sourceName = sourceName;
	}

}
