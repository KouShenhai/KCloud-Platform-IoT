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

package org.laokou.admin.domainEvent.dto.clientobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * 领域事件客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainEventCO extends ClientObject {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 聚合根ID.
	 */
	private Long aggregateId;

	/**
	 * 事件类型.
	 */
	private String eventType;

	/**
	 * MQ主题.
	 */
	private String topic;

	/**
	 * 数据源名称.
	 */
	private String sourceName;

	/**
	 * 扩展属性.
	 */
	private String attribute;

	/**
	 * 服务ID.
	 */
	private String serviceId;

	/**
	 * 标签.
	 */
	private String tag;

}
