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
public abstract class DomainEvent extends Identifier {

	@Serial
	private static final long serialVersionUID = 1532877866226749304L;

	/**
	 * 服务ID.
	 */
	protected final String serviceId;

	/**
	 * 租户ID.
	 */
	protected final Long tenantId;

	/**
	 * 用户ID.
	 */
	protected final Long userId;

	/**
	 * 聚合根ID.
	 */
	protected final Long aggregateId;

	/**
	 * 数据源前缀.
	 */
	protected final String sourcePrefix;

	/**
	 * MQ主题.
	 */
	protected final String topic;

	/**
	 * 标签.
	 */
	protected final String tag;

	/**
	 * 操作时间.
	 */
	protected final Instant instant = DateUtil.nowInstant();

	protected DomainEvent(String serviceId, Long tenantId, Long userId, Long aggregateId, String sourcePrefix,
			String topic, String tag) {
		this.serviceId = serviceId;
		this.tenantId = tenantId;
		this.userId = userId;
		this.aggregateId = aggregateId;
		this.sourcePrefix = sourcePrefix;
		this.topic = topic;
		this.tag = tag;
	}

}
