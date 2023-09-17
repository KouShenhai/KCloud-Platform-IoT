/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.admin.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.database.dataobject.BaseDO;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@TableName("boot_sys_oss")
@Schema(name = "OssDO", description = "存储")
public class OssDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 7064643286240062439L;

	@Schema(name = "name", description = "名称")
	private String name;

	@Schema(name = "endpoint", description = "终端地址")
	private String endpoint;

	@Schema(name = "region", description = "区域")
	private String region;

	@Schema(name = "accessKey", description = "访问密钥")
	private String accessKey;

	@Schema(name = "secretKey", description = "用户密钥")
	private String secretKey;

	@Schema(name = "bucketName", description = "桶名")
	private String bucketName;

	@Schema(name = "pathStyleAccessEnabled", description = "路径样式访问 0未启用 1已开启")
	private Integer pathStyleAccessEnabled;

}
