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

package org.laokou.oss.dto.domainevent;

import lombok.Getter;
import org.laokou.common.i18n.dto.DomainEvent;

import java.time.Instant;

/**
 * @author laokou
 */
@Getter
public class OssUploadEvent extends DomainEvent {

	/**
	 * 文件名称.
	 */
	private final String name;

	/**
	 * 文件的MD5标识.
	 */
	private final String md5;

	/**
	 * 文件的URL.
	 */
	private final String url;

	/**
	 * 文件大小.
	 */
	private final Long size;

	/**
	 * OSS存储ID.
	 */
	private final Long ossId;

	/**
	 * 文件类型.
	 */
	private final String contentType;

	/*
	 * 类型.
	 */
	private final String type;

	/**
	 * 文件格式.
	 */
	private final String format;

	/**
	 * 上传时间.
	 */
	private final Instant uploadTime;

	public OssUploadEvent(final Long id,
						  final String name,
						  final String md5,
						  final  String url,
						  final  Long size,
						  final  Long ossId,
						  final  String contentType,
						  final  String format,
						  final Instant uploadTime,
						  final Long tenantId,
						  final Long userId,
						  String type) {
		this.type = type;
		this.id = id;
		this.name = name;
		this.md5 = md5;
		this.url = url;
		this.size = size;
		this.ossId = ossId;
		this.contentType = contentType;
		this.format = format;
		this.uploadTime = uploadTime;
		this.tenantId = tenantId;
		this.userId = userId;
	}
}
