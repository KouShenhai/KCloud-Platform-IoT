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

package org.laokou.admin.oss.dto.clientobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * OSS客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OssCO extends ClientObject {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * OSS名称.
	 */
	private String name;

	/**
	 * OSS的终端地址.
	 */
	private String endpoint;

	/**
	 * OSS的区域.
	 */
	private String region;

	/**
	 * OSS的访问密钥.
	 */
	private String accessKey;

	/**
	 * OSS的用户密钥.
	 */
	private String secretKey;

	/**
	 * OSS的桶名.
	 */
	private String bucketName;

	/**
	 * 路径样式访问 1已开启 0未启用.
	 */
	private Integer pathStyleAccessEnabled;

}
