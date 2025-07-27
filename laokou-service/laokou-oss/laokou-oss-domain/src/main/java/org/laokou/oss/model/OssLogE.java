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

package org.laokou.oss.model;

import lombok.Getter;
import lombok.Setter;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.dto.Identifier;

import java.time.Instant;

/**
 * OSS日志领域对象【实体】.
 *
 * @author laokou
 */
@Entity
public class OssLogE extends Identifier {

	/**
	 * 文件名称.
	 */
	@Setter
	@Getter
	private String name;

	/**
	 * 文件的MD5标识.
	 */
	@Setter
	@Getter
	private String md5;

	/**
	 * 文件的URL.
	 */
	@Setter
	@Getter
	private String url;

	/**
	 * 文件大小.
	 */
	@Setter
	@Getter
	private Long size;

	/**
	 * OSS存储ID.
	 */
	@Setter
	@Getter
	private Long ossId;

	/**
	 * 文件类型.
	 */
	@Setter
	@Getter
	private String contentType;

	/**
	 * 文件格式.
	 */
	@Setter
	@Getter
	private String format;

	/**
	 * 上传时间.
	 */
	@Setter
	@Getter
	private Instant uploadTime;

	/**
	 * 租户ID.
	 */
	@Setter
	@Getter
	private Long tenantId;

	/**
	 * 用户ID.
	 */
	@Setter
	@Getter
	private Long userId;

}
