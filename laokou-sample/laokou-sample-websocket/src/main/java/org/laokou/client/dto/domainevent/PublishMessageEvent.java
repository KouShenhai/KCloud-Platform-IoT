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

package org.laokou.client.dto.domainevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.client.dto.clientobject.PayloadCO;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.constant.EventType;
import org.laokou.common.i18n.dto.DefaultDomainEvent;

import static com.baomidou.dynamic.datasource.enums.DdConstants.MASTER;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishMessageEvent extends DefaultDomainEvent {

	private PayloadCO co;

	public PublishMessageEvent(String topic, String tag, PayloadCO co, String serviceId) {
		super(topic, tag);
		this.co = co;
		super.sourceName = MASTER;
		super.eventType = EventType.PUBLISH;
		super.serviceId = serviceId;
		generatorId();
	}

	@Override
	protected void generatorId() {
		super.id = IdGenerator.defaultSnowflakeId();
	}

}
