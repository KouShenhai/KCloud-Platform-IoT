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

import java.time.Instant;

/**
 * 聚合根.
 *
 * @author laokou
 */
@Getter
public abstract class AggregateRoot extends Identifier {

	/**
	 * 服务ID.
	 */
	protected String serviceId;

	/**
	 * 租户ID.
	 */
	protected Long tenantId;

	/**
	 * 用户ID.
	 */
	protected Long userId;

	/**
	 * 操作时间.
	 */
	protected final Instant instant = DateUtil.nowInstant();

}
