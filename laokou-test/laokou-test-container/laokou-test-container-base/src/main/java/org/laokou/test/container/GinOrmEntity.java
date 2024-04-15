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
		while ((str = br.readLine()) != null) {
			String[] s = str.split(" ");
			log.info(StringUtil.convertUnder("_" + s[0]) + " " + MAP.get(s[1]) + " //" + s[2]);
		}
	}

	private static String from() {
		return """
				mark varchar 标识
				name varchar 名称
				status tinyint 状态
				protocol_id bigint 协议ID
				protocol_name varchar 协议名称
				type varchar 类型
				read_timeout int 读超时时间
				write_timeout int 写超时时间
				read_interval int 读间隔时间
				write_interval int 写间隔时间
				conn_params varchar 连接参数
				remote_directory varchar 远程目录
				file_name varchar 文件名
				databse varchar 数据库
				addr varchar 服务器地址
				username varchar 用户名
				password varchar 密码
				ip varchar 服务端IP
				port int 服务端端口
				serial_name varchar 串口名称
				baud_rate varchar 波特率
				check_bit varchar 校验位
				data_bit varchar 数据位
				stop_bit varchar 停止位
							""";
	}

}
