/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.log.handler.event;

import lombok.Getter;
import org.laokou.common.i18n.dto.DomainEvent;

import java.io.Serial;
import java.time.Instant;

/**
 * 操作事件.
 *
 * @author laokou
 */
@Getter
public final class OperateEvent extends DomainEvent {

	@Serial
	private static final long serialVersionUID = -6523521638764501311L;

	private final String name;

	private final String moduleName;

	private final String uri;

	private final String methodName;

	private final String requestType;

	private final String requestParams;

	private final String userAgent;

	private final String ip;

	private final String address;

	private final Integer status;

	private final String operator;

	private final String errorMessage;

	private final Long costTime;

	private final String serviceId;

	private final String serviceAddress;

	private final String profile;

	private final String stackTrace;

	private final Instant createTime;

	public OperateEvent(final Long id,
						final String name,
						final String moduleName,
						final String uri,
						final String methodName,
						final String requestType,
						final String requestParams,
						final String userAgent,
						final String ip,
						final String address,
						final Integer status,
						final String operator,
						final String errorMessage,
						final Long costTime,
						final String serviceId,
						final String serviceAddress,
						final String profile,
						final String stackTrace,
						final Instant createTime,
						final Long tenantId,
						final Long userId) {
		super.id = id;
		super.userId = userId;
		super.tenantId = tenantId;
		this.name = name;
		this.moduleName = moduleName;
		this.uri = uri;
		this.methodName = methodName;
		this.requestType = requestType;
		this.requestParams = requestParams;
		this.userAgent = userAgent;
		this.ip = ip;
		this.address = address;
		this.status = status;
		this.operator = operator;
		this.errorMessage = errorMessage;
		this.costTime = costTime;
		this.serviceId = serviceId;
		this.serviceAddress = serviceAddress;
		this.profile = profile;
		this.stackTrace = stackTrace;
		this.createTime = createTime;
	}

}
