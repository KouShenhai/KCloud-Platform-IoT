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
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.dto.DefaultDomainEvent;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 登录事件.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginEvent extends DefaultDomainEvent {

	@Serial
	private static final long serialVersionUID = -325094951800650353L;

	/**
	 * 登录的用户名.
	 */
	private String username;

	/**
	 * 登录的IP地址.
	 */
	private String ip;

	/**
	 * 登录的归属地.
	 */
	private String address;

	/**
	 * 登录的浏览器.
	 */
	private String browser;

	/**
	 * 登录的操作系统.
	 */
	private String os;

	/**
	 * 登录状态 0登录成功 1登录失败.
	 */
	private Integer status;

	/**
	 * 错误信息.
	 */
	private String errorMessage;

	/**
	 * 登录类型.
	 */
	private String type;

	public void create(AggregateRoot<Long> aggregateRoot, String topic, EventType eventType, EventStatus eventStatus,
			LocalDateTime timestamp) {
		create(aggregateRoot, topic, eventType, eventStatus);
		super.createDate = timestamp;
		super.updateDate = timestamp;
	}

	@Override
	protected void create(AggregateRoot<Long> aggregateRoot, String topic, EventType eventType,
			EventStatus eventStatus) {
		super.create(aggregateRoot, topic, eventType, eventStatus);
		super.id = IdGenerator.defaultSnowflakeId();
	}

}
