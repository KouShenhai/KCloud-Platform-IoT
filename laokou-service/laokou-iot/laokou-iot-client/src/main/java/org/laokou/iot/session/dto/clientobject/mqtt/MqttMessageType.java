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

package org.laokou.iot.session.dto.clientobject.mqtt;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/***
 * mqtt消息类型枚举.
 *
 * @author laokou
 */
@Getter
public enum MqttMessageType {

	DOWN_COMMAND_GATEWAY_MESSAGE("down_command_gateway_message", "网关指令【下行】") {
		@Override
		public String getTopic() {
			return "down/+/+/command";
		}

		@Override
		public String getMqTopic() {
			return "iot-down-command-gateway-message";
		}

		@Override
		MqttQos getMqttQos() {
			return MqttQos.AT_LEAST_ONCE;
		}

		@Override
		public int getNumPartitions() {
			return 2;
		}

	},

	UP_COMMAND_REPLY_GATEWAY_MESSAGE("up_command_reply_gateway_message", "网关指令回复【上行】") {
		@Override
		public String getTopic() {
			return "up/+/+/command/reply";
		}

		@Override
		public String getMqTopic() {
			return "iot-up-command-reply-gateway-message";
		}

		@Override
		MqttQos getMqttQos() {
			return MqttQos.AT_LEAST_ONCE;
		}

		@Override
		public int getNumPartitions() {
			return 2;
		}

	},

	UP_REPORT_OTA_GATEWAY_MESSAGE("up_report_ota_gateway_message", "上报网关固件信息【上行】") {
		@Override
		public String getTopic() {
			return "up/+/+/ota/report";
		}

		@Override
		public String getMqTopic() {
			return "iot-up-report-ota-gateway-message";
		}

		@Override
		MqttQos getMqttQos() {
			return MqttQos.AT_LEAST_ONCE;
		}

		@Override
		public int getNumPartitions() {
			return 2;
		}

	},

	DOWN_REPORT_OTA_REPLY_GATEWAY_MESSAGE("down_report_ota_reply_gateway_message", "上报网关固件信息回复【下行】") {
		@Override
		public String getTopic() {
			return "up/+/+/ota/report/reply";
		}

		@Override
		public String getMqTopic() {
			return "iot-down-report-ota-reply-gateway-message";
		}

		@Override
		MqttQos getMqttQos() {
			return MqttQos.AT_LEAST_ONCE;
		}

		@Override
		public int getNumPartitions() {
			return 2;
		}

	},

	UP_UPGRADE_OTA_GATEWAY_MESSAGE("up_upgrade_ota_gateway_message", "升级网关固件【上行】") {
		@Override
		public String getTopic() {
			return "up/+/+/ota/upgrade";
		}

		@Override
		public String getMqTopic() {
			return "iot-up-upgrade-ota-gateway-message";
		}

		@Override
		MqttQos getMqttQos() {
			return MqttQos.AT_LEAST_ONCE;
		}

		@Override
		public int getNumPartitions() {
			return 2;
		}

	},

	DOWN_UPGRADE_OTA_REPLY_GATEWAY_MESSAGE("down_upgrade_ota_reply_gateway_message", "升级网关固件回复【下行】") {
		@Override
		public String getTopic() {
			return "down/+/+/ota/upgrade/reply";
		}

		@Override
		public String getMqTopic() {
			return "iot-down-upgrade-ota-reply-gateway-message";
		}

		@Override
		MqttQos getMqttQos() {
			return MqttQos.AT_LEAST_ONCE;
		}

		@Override
		public int getNumPartitions() {
			return 2;
		}

	},

	UP_UPGRADE_OTA_PROGRESS_GATEWAY_MESSAGE("up_upgrade_ota_progress_gateway_message", "升级网关固件进度【上行】") {
		@Override
		public String getTopic() {
			return "up/+/+/ota/upgrade/process";
		}

		@Override
		public String getMqTopic() {
			return "iot-up-upgrade-ota-process-gateway-message";
		}

		@Override
		MqttQos getMqttQos() {
			return MqttQos.AT_LEAST_ONCE;
		}

		@Override
		public int getNumPartitions() {
			return 2;
		}

	},

	UP_HEARTBEAT_GATEWAY_MESSAGE("up_heartbeat_gateway_message", "网关心跳【上行】") {
		@Override
		public String getTopic() {
			return "up/+/+/heartbeat";
		}

		@Override
		public String getMqTopic() {
			return "iot-up-heartbeat-gateway-message";
		}

		@Override
		MqttQos getMqttQos() {
			return MqttQos.AT_MOST_ONCE;
		}

		@Override
		public int getNumPartitions() {
			return 4;
		}

	},

	UP_REPORT_PROPERTIES_DEVICE_MESSAGE("up_report_properties_device_message", "上报设备属性【上行】") {
		@Override
		public String getTopic() {
			return "up/+/+/+/properties/report";
		}

		@Override
		public String getMqTopic() {
			return "iot-up-report-properties-device-message";
		}

		@Override
		MqttQos getMqttQos() {
			return MqttQos.AT_MOST_ONCE;
		}

		@Override
		public int getNumPartitions() {
			return 8;
		}

	},

	UP_ALARM_EVENT_DEVICE_MESSAGE("up_event_device_message", "设备报警事件【上行】") {

		@Override
		public String getTopic() {
			return "up/+/+/+/event/alarm";
		}

		@Override
		public String getMqTopic() {
			return "iot-up-alarm-event-device-message";
		}

		@Override
		MqttQos getMqttQos() {
			return MqttQos.AT_LEAST_ONCE;
		}

		@Override
		public int getNumPartitions() {
			return 4;
		}

	};

	private final String code;

	private final String desc;

	MqttMessageType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract String getTopic();

	public abstract String getMqTopic();

	abstract MqttQos getMqttQos();

	public abstract int getNumPartitions();

	public static Map<String, Integer> getTopics(String tenantCode) {
		return Arrays.stream(values())
			.collect(Collectors.toMap(
					k -> String.format("$share/iot/%s",
							k.getTopic().replaceFirst("\\+", Matcher.quoteReplacement(tenantCode))),
					v -> v.getMqttQos().getCode()));
	}

}
