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

package org.laokou.common.log4j2.model;

import lombok.Getter;

/**
 * @author laokou
 */
@Getter
public enum MqEnum {

	;

	private final String code;

	private final String desc;

	MqEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static final String DISTRIBUTED_IDENTIFIER_TRACE_LOG_TOPIC = "distributed-identifier-trace-log";

	public static final String GATEWAY_TRACE_LOG_TOPIC = "gateway-trace-log";

	public static final String AUTH_TRACE_LOG_TOPIC = "auth-trace-log";

	public static final String ADMIN_TRACE_LOG_TOPIC = "admin-trace-log";

	public static final String IOT_TRACE_LOG_TOPIC = "iot-trace-log";

	public static final String OSS_TRACE_LOG_TOPIC = "oss-trace-log";

	public static final String GENERATOR_TRACE_LOG_TOPIC = "generator-trace-log";

	public static final String MQTT_TRACE_LOG_TOPIC = "mqtt-trace-log";

	public static final String UDP_TRACE_LOG_TOPIC = "udp-trace-log";

	public static final String HTTP_TRACE_LOG_TOPIC = "http-trace-log";

	public static final String TCP_TRACE_LOG_TOPIC = "tcp-trace-log";

	public static final String REPORT_TRACE_LOG_TOPIC = "report-trace-log";

}
