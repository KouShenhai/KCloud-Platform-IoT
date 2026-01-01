/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import lombok.experimental.SuperBuilder;
import org.laokou.common.i18n.dto.DomainEvent;

import java.io.Serial;
import java.time.Instant;

/**
 * 操作事件.
 *
 * @author laokou
 */
@Getter
@SuperBuilder(toBuilder = true)
public final class OperateEvent extends DomainEvent {

	@Serial
	private static final long serialVersionUID = -6523521638764501311L;

	private String name;

	private String moduleName;

	private String uri;

	private String methodName;

	private String requestType;

	private String requestParams;

	private String userAgent;

	private String ip;

	private String address;

	private Integer status;

	private String operator;

	private String errorMessage;

	private Long costTime;

	private String serviceId;

	private String serviceAddress;

	private String profile;

	private String stackTrace;

	private Instant createTime;

}
