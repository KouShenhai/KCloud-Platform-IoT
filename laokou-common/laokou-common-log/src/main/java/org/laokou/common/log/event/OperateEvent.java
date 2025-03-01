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

package org.laokou.common.log.event;

import java.io.Serial;
import java.io.Serializable;

/**
 * 操作事件.
 *
 * @param name
 * @param moduleName
 * @param uri
 * @param methodName
 * @param requestType
 * @param requestParams
 * @param userAgent
 * @param address
 * @param status
 * @param operator
 * @param errorMessage
 * @param costTime
 * @param ip
 * @param serviceId
 * @param serviceAddress
 * @param profile
 * @param stackTrace
 * @author laokou
 */
public record OperateEvent(String name, String moduleName, String uri, String methodName, String requestType,
		String requestParams, String userAgent, String ip, String address, Integer status, String operator,
		String errorMessage, Long costTime, String serviceId, String serviceAddress, String profile,
		String stackTrace) implements Serializable {

	@Serial
	private static final long serialVersionUID = -6523521638764501311L;

}
