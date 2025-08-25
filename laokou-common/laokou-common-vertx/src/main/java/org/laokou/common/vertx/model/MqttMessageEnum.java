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

package org.laokou.common.vertx.model;

import lombok.Getter;

/***
 * mqtt消息枚举.
 */
@Getter
public enum MqttMessageEnum {

	UP_PROPERTY_REPORT("up_property_report", "属性上报【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/property/report";
		}

		@Override
		public String getMqTopic() {
			return "up-property-report";
		}
	},

	DOWN_PROPERTY_READ("down_property_read", "属性读取【下行】") {
		@Override
		public String getTopic() {
			return "/+/+/down/property/read";
		}

		@Override
		public String getMqTopic() {
			return "down-property-read";
		}
	},

	UP_PROPERTY_READ_REPLY("up_property_read_reply", "属性读取回复【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/property/read/reply";
		}

		@Override
		public String getMqTopic() {
			return "up-property-read-reply";
		}
	},

	DOWN_PROPERTY_WRITE("down_property_write", "属性修改【下行】") {
		@Override
		public String getTopic() {
			return "/+/+/down/property/write";
		}

		@Override
		public String getMqTopic() {
			return "down-property-write";
		}
	},

	UP_PROPERTY_WRITE_REPLY("up_property_write_reply", "属性修改回复【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/property/write/reply";
		}

		@Override
		public String getMqTopic() {
			return "up-property-write-reply";
		}
	},

	UP_OTA_REPORT("up_ota_report", "OTA固件升级结果上报【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/ota/report";
		}

		@Override
		public String getMqTopic() {
			return "up-ota-report";
		}
	},

	UP_OTA_UPGRADE("up_ota_upgrade", "OTA固件升级【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/ota/upgrade";
		}

		@Override
		public String getMqTopic() {
			return "up-ota-upgrade";
		}
	},

	DOWN_OTA_UPGRADE_REPLY("down_ota_upgrade_reply", "OTA固件升级回复【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/down/ota/upgrade/reply";
		}

		@Override
		public String getMqTopic() {
			return "down-ota-upgrade-reply";
		}
	};

	private final String code;

	private final String desc;

	MqttMessageEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract String getTopic();

	public abstract String getMqTopic();

}
