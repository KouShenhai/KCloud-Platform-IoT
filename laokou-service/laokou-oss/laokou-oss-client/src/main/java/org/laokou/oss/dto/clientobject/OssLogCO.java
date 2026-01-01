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

package org.laokou.oss.dto.clientobject;

import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.Instant;

/**
 * OSS日志客户端对象.
 *
 * @author laokou
 */
@Data
public class OssLogCO extends ClientObject {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 文件名称.
	 */
	private String name;

	/**
	 * 文件的MD5标识.
	 */
	private String md5;

	/**
	 * 文件的URL.
	 */
	private String url;

	/**
	 * 文件大小.
	 */
	private Long size;

	/**
	 * OSS存储ID.
	 */
	private Long ossId;

	/**
	 * 文件类型.
	 */
	private String contentType;

	/**
	 * 类型.
	 */
	private String type;

	/**
	 * 文件格式.
	 */
	private String format;

	/**
	 * 上传时间.
	 */
	private Instant uploadTime;

	/**
	 * 租户ID.
	 */
	private Long tenantId;

	/**
	 * 创建者.
	 */
	private Long creator;

}
