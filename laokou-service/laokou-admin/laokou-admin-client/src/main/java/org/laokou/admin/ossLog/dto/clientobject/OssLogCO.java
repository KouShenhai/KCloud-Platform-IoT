/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.ossLog.dto.clientobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * OSS日志客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
	 * 上传状态 0成功 1失败.
	 */
	private Integer status;

	/**
	 * 错误信息.
	 */
	private String errorMessage;

}
