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

package org.laokou.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

import static org.laokou.common.domain.entity.DomainEventDO.BOOT_DOMAIN_EVENT;

/**
 * @author laokou
 */
@Data
@TableName(BOOT_DOMAIN_EVENT)
public class DomainEventDO extends BaseDO {

	protected static final String BOOT_DOMAIN_EVENT = "boot_domain_event";

	/**
	 * 事件类型.
	 */
	private String eventType;

	/**
	 * 数据源前缀.
	 */
	private String sourcePrefix;

	/**
	 * MQ主题.
	 */
	private String topic;

	/**
	 * 标签.
	 */
	private String tag;

	/**
	 * 服务ID.
	 */
	private String serviceId;

	/**
	 * 聚合根ID.
	 */
	private Long aggregateId;

	/**
	 * 扩展属性.
	 */
	private String attribute;

}
