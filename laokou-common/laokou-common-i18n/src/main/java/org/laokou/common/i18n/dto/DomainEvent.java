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
import lombok.Setter;
import org.laokou.common.i18n.common.constant.EventType;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.StringUtil;

import java.io.Serial;
import java.time.Instant;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;

/**
 * 领域事件.
 *
 * @author laokou
 */
@Setter
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

	/**
	 * 数据源前缀.
	 */
	private final String sourcePrefix;

	public DomainEvent() {
		this.payload = EMPTY;
		this.type = EMPTY;
		this.sourcePrefix = EMPTY;
		this.tenantId = 0L;
		this.userId = 0L;
		this.aggregateId = 0L;
		this.topic = EMPTY;
		this.tag = EMPTY;
		this.version = 0;
	}

	public DomainEvent(Long id, Long tenantId, Long userId, Long aggregateId, String topic, String tag, int version,
			String payload, EventType type, String sourcePrefix) {
		super.id = id;
		this.payload = payload;
		this.type = StringUtil.convertUnder(type.name().toLowerCase());
		this.sourcePrefix = sourcePrefix;
		this.tenantId = tenantId;
		this.userId = userId;
		this.aggregateId = aggregateId;
		this.topic = topic;
		this.tag = tag;
		this.version = version;
	}

}
