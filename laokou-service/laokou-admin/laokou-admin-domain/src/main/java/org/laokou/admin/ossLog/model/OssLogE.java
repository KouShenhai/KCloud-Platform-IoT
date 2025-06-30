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

package org.laokou.admin.ossLog.model;

import lombok.Data;

/**
 * OSS日志领域对象【实体】.
 *
 * @author laokou
 */
@Data
public class OssLogE {

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

}
