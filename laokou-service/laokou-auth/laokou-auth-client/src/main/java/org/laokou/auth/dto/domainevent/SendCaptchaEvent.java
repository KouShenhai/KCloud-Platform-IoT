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

package org.laokou.auth.dto.domainevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.constant.EventStatus;
import org.laokou.common.i18n.common.constant.EventType;
import org.laokou.common.i18n.dto.DefaultDomainEvent;

import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendCaptchaEvent extends DefaultDomainEvent {

	private String tag;

	private String uuid;

	public void create(String topic, String tag, EventType eventType, EventStatus eventStatus, String appName,
			String sourceName, LocalDateTime timestamp) {
		create(topic, tag, eventType, eventStatus);
		super.createDate = timestamp;
		super.updateDate = timestamp;
		super.appName = appName;
		super.sourceName = sourceName;
	}

	@Override
	protected void create(String topic, String tag, EventType eventType, EventStatus eventStatus) {
		super.create(topic, tag, eventType, eventStatus);
		super.id = IdGenerator.defaultSnowflakeId();
	}

}
