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

import lombok.Getter;
import org.laokou.common.i18n.utils.DateUtil;

import java.io.Serial;
import java.time.Instant;

/**
 * 领域事件.
 *
 * @author laokou
 */
@Getter
public class DomainEvent extends Identifier {

	@Serial
	private static final long serialVersionUID = 1532877866226749304L;

	/**
	 * 租户ID.
	 */
	private final Long tenantId;

	/**
	 * 用户ID.
	 */
	private final Long userId;

	/**
	 * 聚合根ID.
	 */
	private final Long aggregateId;

	/**
	 * MQ主题.
	 */
	private final String topic;

	/**
	 * MQ标签.
	 */
	private final String tag;

	/**
	 * 时间.
	 */
	private final Instant instant = DateUtil.nowInstant();

	/**
	 * 版本号【乐观锁】.
	 */
	private final int version;

	/**
	 * 内容.
	 */
	private final String payload;

	/**
	 * 类型.
	 */
	private final String type;

	public DomainEvent(Long eventId, Long tenantId, Long userId, Long aggregateId, String topic, String tag,
			int version, String payload, String type) {
		this.payload = payload;
		this.type = type;
		this.id = eventId;
		this.tenantId = tenantId;
		this.userId = userId;
		this.aggregateId = aggregateId;
		this.topic = topic;
		this.tag = tag;
		this.version = version;
	}

}
