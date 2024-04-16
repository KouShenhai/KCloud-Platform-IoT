/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.test.container;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.StringUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class GinOrmEntity {

	/**
	 * SELECT column_name, data_type, column_comment FROM information_schema. COLUMNS
	 * WHERE table_name = "xx" AND table_schema = (SELECT DATABASE()) and column_name not
	 * in ("id","del_flag","creator","editor","create_date","update_date","version") ORDER
	 * BY ordinal_position;
	 */
	private static final Map<String, String> MAP = Map.of("bigint", "int64", "varchar", "string", "tinyint", "int",
			"int", "int", "datetime", "time.Time");

	public static void main(String[] args) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(from().getBytes(StandardCharsets.UTF_8));
		BufferedReader br = new BufferedReader(new InputStreamReader(bis));
		String str;
		StringBuilder sb = new StringBuilder();
		while ((str = br.readLine()) != null) {
			String[] s = str.split(" ");
			sb.append(StringUtil.convertUnder("_" + s[0]))
				.append(" ")
				.append(MAP.get(s[1]))
				.append(" //")
				.append(s[2])
				.append("\n");
		}
		log.info("\n{}", sb);
	}

	private static String from() {
		return """
				status tinyint 状态
				mark varchar 标识
				name varchar 名称
				channel_id bigint 通道ID
				channel_name varchar 通道名称
				model_id bigint 模型ID
				model_name varchar 模型名称
				device_group varchar 设备组
				device_id varchar 设备ID
				type_id bigint 设备类型ID
				type_name varchar 设备类型名称
				mfr_id bigint 厂商ID
				mfr_name varchar 厂商名称
				mod_id bigint 设备型号ID
				mod_mark varchar 设备型号标识
				control_mfr varchar 控制器厂商
				control_model varchar 控制器型号
				desc varchar 模型描述
											""";
	}

}
