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

package org.laokou.common.modbus4j.config;

import lombok.Getter;
import org.laokou.common.i18n.util.EnumParser;

/**
 * @author laokou
 */
@Getter
public enum ModbusTypeEnum {

	RTU_MASTER("rtu_master", "RTU MASTER"),

	TCP_MASTER("tcp_master", "TCP MASTER"),

	ASCII_MASTER("ascii_master", "ASCII MASTER"),

	UDP_MASTER("udp_master", "UDP MASTER");

	private final String code;

	private final String desc;

	ModbusTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static ModbusTypeEnum getByCode(String code) {
		return EnumParser.parse(ModbusTypeEnum.class, ModbusTypeEnum::getCode, code);
	}

}
