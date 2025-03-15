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

package org.laokou.common.oss.entity;

import lombok.Data;

/**
 * @author laokou
 */
@Data
public class OssInfo {

	/**
	 * OSS类型【本地/云端】.
	 */
	private Type type;

	/**
	 * OSS的目录【本地】.
	 */
	private String directory;

	/**
	 * OSS的路径【本地】.
	 */
	private String path;

	/**
	 * OSS的域名【本地】.
	 */
	private String domain;

	/**
	 * OSS的终端地址【云端】.
	 */
	private String endpoint;

	/**
	 * OSS的区域【云端】.
	 */
	private String region;

	/**
	 * OSS的访问密钥【云端】.
	 */
	private String accessKey;

	/**
	 * OSS的用户密钥【云端】.
	 */
	private String secretKey;

	/**
	 * OSS的桶名【云端】.
	 */
	private String bucketName;

	/**
	 * 路径样式访问 1已开启 0未启用【云端】.
	 */
	private Integer pathStyleAccessEnabled;

}
