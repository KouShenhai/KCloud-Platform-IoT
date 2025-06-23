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

package org.laokou.common.oss.model;

import lombok.Data;

/**
 * @author laokou
 */

@Data
public final class MinIO extends BaseOss {

	/**
	 * 终端地址.
	 */
	private String endpoint;

	/**
	 * 区域.
	 */
	private String region;

	/**
	 * 访问密钥.
	 */
	private String accessKey;

	/**
	 * 用户密钥.
	 */
	private String secretKey;

	/**
	 * 桶名.
	 */
	private String bucketName;

	@Override
	public StoragePolicyEnum getStoragePolicy() {
		return StoragePolicyEnum.MINIO;
	}

}
