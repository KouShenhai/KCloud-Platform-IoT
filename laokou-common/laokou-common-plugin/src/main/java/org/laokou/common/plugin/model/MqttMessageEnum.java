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

package org.laokou.common.plugin.model;

import lombok.Getter;

/***
 * mqtt消息枚举.
 */
@Getter
public enum MqttMessageEnum {

	UP_PROPERTY_REPORT(ReportPropertyMessage.class, "up_property_report", "上报属性【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/property/report";
		}

		@Override
		public String getMqTopic() {
			return "up-property-report";
		}
	},

	DOWN_PROPERTY_READ(ReadPropertyMessage.class, "down_property_read", "读取属性【下行】") {
		@Override
		public String getTopic() {
			return "/+/+/down/property/read";
		}

		@Override
		public String getMqTopic() {
			return "down-property-read";
		}
	},

	UP_PROPERTY_READ_REPLY(null, "up_property_read_reply", "读取属性回复【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/property/read/reply";
		}

		@Override
		public String getMqTopic() {
			return "up-property-read-reply";
		}
	},

	DOWN_PROPERTY_WRITE(null, "down_property_write", "修改属性【下行】") {
		@Override
		public String getTopic() {
			return "/+/+/down/property/write";
		}

		@Override
		public String getMqTopic() {
			return "down-property-write";
		}
	},

	UP_PROPERTY_WRITE_REPLY(null, "up_property_write_reply", "修改属性回复【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/property/write/reply";
		}

		@Override
		public String getMqTopic() {
			return "up-property-write-reply";
		}
	},

	UP_OTA_UPGRADE_REPORT(null, "up_ota_upgrade_report", "升级OTA固件上报【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/ota/upgrade/report";
		}

		@Override
		public String getMqTopic() {
			return "up-ota-upgrade-report";
		}
	},

	UP_OTA_UPGRADE_SET(null, "up_ota_upgrade_set", "升级OTA固件推送【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/ota/upgrade/set";
		}

		@Override
		public String getMqTopic() {
			return "up-ota-upgrade-set";
		}
	},

	DOWN_OTA_UPGRADE_GET(null, "down_ota_upgrade_get", "升级OTA固件拉取【下行】") {
		@Override
		public String getTopic() {
			return "/+/+/down/ota/upgrade/get";
		}

		@Override
		public String getMqTopic() {
			return "down-ota-upgrade-get";
		}
	};

	private final String code;

	private final String desc;

	private final Class<? extends DeviceMessage> clazz;

	MqttMessageEnum(Class<? extends DeviceMessage> clazz, String code, String desc) {
		this.code = code;
		this.desc = desc;
		this.clazz = clazz;
	}

	public abstract String getTopic();

	public abstract String getMqTopic();

}
