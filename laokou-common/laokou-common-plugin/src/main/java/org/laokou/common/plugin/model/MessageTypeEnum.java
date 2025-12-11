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
public enum MessageTypeEnum {

	UP_PROPERTY_REPORT("up_property_report", "上报属性【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/property/report";
		}

		@Override
		public String getMqTopic() {
			return "up-property-report";
		}
	},

	DOWN_PROPERTY_READ("down_property_read", "读取属性【下行】") {
		@Override
		public String getTopic() {
			return "/+/+/down/property/read";
		}

		@Override
		public String getMqTopic() {
			return "down-property-read";
		}
	},

	UP_PROPERTY_READ_REPLY("up_property_read_reply", "读取属性回复【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/property/read/reply";
		}

		@Override
		public String getMqTopic() {
			return "up-property-read-reply";
		}
	},

	DOWN_PROPERTY_WRITE("down_property_write", "修改属性【下行】") {
		@Override
		public String getTopic() {
			return "/+/+/down/property/write";
		}

		@Override
		public String getMqTopic() {
			return "down-property-write";
		}
	},

	UP_PROPERTY_WRITE_REPLY("up_property_write_reply", "修改属性回复【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/property/write/reply";
		}

		@Override
		public String getMqTopic() {
			return "up-property-write-reply";
		}
	},

	UP_FIRMWARE_REPORT("up_firmware_report", "上报固件版本【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/firmware/report";
		}

		@Override
		public String getMqTopic() {
			return "up-firmware-report";
		}
	},

	UP_FIRMWARE_PULL("up_firmware_pull", "拉取固件【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/firmware/pull";
		}

		@Override
		public String getMqTopic() {
			return "up-firmware-pull";
		}
	},

	DOWN_FIRMWARE_PULL_REPLY("up_firmware_pull_reply", "拉取固件回复【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/down/firmware/pull/reply";
		}

		@Override
		public String getMqTopic() {
			return "down-firmware-pull-reply";
		}
	},

	DOWN_FIRMWARE_UPGRADE("down_firmware_upgrade", "升级固件【下行】") {
		@Override
		public String getTopic() {
			return "/+/+/down/firmware/upgrade";
		}

		@Override
		public String getMqTopic() {
			return "down-firmware-upgrade";
		}
	},

	UP_FIRMWARE_UPGRADE_REPLY("up_firmware_upgrade_reply", "升级固件回复【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/firmware/upgrade/reply";
		}

		@Override
		public String getMqTopic() {
			return "up-firmware-upgrade-reply";
		}
	},

	UP_FIRMWARE_UPGRADE_PROGRESS("up_firmware_upgrade_progress", "升级固件进度【上行】") {
		@Override
		public String getTopic() {
			return "/+/+/up/firmware/upgrade/process";
		}

		@Override
		public String getMqTopic() {
			return "up-firmware-upgrade-process";
		}
	};

	private final String code;

	private final String desc;

	MessageTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract String getTopic();

	public abstract String getMqTopic();

}
