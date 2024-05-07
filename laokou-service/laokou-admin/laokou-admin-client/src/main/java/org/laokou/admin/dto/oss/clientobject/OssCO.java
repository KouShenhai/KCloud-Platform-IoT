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

package org.laokou.admin.dto.oss.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * @author laokou
 */
@Data
@Schema(name = "OssCO", description = "OSS")
public class OssCO extends ClientObject {

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "name", description = "OSS名称")
	private String name;

	@Schema(name = "endpoint", description = "OSS的终端地址")
	private String endpoint;

	@Schema(name = "region", description = "OSS的区域")
	private String region;

	@Schema(name = "accessKey", description = "OSS的访问密钥")
	private String accessKey;

	@Schema(name = "secretKey", description = "OSS的用户密钥")
	private String secretKey;

	@Schema(name = "bucketName", description = "OSS的桶名")
	private String bucketName;

	@Schema(name = "pathStyleAccessEnabled", description = "路径样式访问 1已开启 0未启用")
	private Integer pathStyleAccessEnabled;

}
