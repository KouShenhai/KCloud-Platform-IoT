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

package org.laokou.network.model.enums;

import lombok.Getter;

/**
 * http消息类型枚举.
 *
 * @author laokou
 */
@Getter
public enum HttpMessageType {

	REPORT_PROPERTIES_GATEWAY_MESSAGE("report_properties_gateway_message", "上报网关属性") {
		@Override
		public String getRoute() {
			return "/:tenantId/:gatewayId/properties/report";
		}

		@Override
		public String getMqTopic() {
			return "iot-report-properties-gateway-message";
		}
	},

	HEARTBEAT_GATEWAY_MESSAGE("heartbeat_gateway_message", "网关心跳") {
		@Override
		public String getRoute() {
			return "/:tenantId/:gatewayId/heartbeat";
		}

		@Override
		public String getMqTopic() {
			return "iot-heartbeat-gateway-message";
		}
	},

	REPORT_PROPERTIES_DEVICE_MESSAGE("report_properties_device_message", "上报设备属性") {
		@Override
		public String getRoute() {
			return "/:tenantId/:gatewayId/:productId/:deviceId/properties/report";
		}

		@Override
		public String getMqTopic() {
			return "iot-report-properties-device-message";
		}
	},

	STATUS_DEVICE_MESSAGE("status_device_message", "设备状态") {
		@Override
		public String getRoute() {
			return "/:tenantId/:gatewayId/:productId/:deviceId/status";
		}

		@Override
		public String getMqTopic() {
			return "iot-status-device-message";
		}
	},

	EVENT_DEVICE_MESSAGE("event_device_message", "设备事件") {

		@Override
		public String getRoute() {
			return "/:tenantId/:gatewayId/:productId/:deviceId/event/+";
		}

		@Override
		public String getMqTopic() {
			return "iot-event-device-message";
		}
	};

	private final String code;

	private final String desc;

	HttpMessageType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract String getRoute();

	public abstract String getMqTopic();

}
