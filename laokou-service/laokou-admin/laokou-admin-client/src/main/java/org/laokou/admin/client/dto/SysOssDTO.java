/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.admin.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author laokou
 */
@Data
public class SysOssDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 2765572823094626152L;

	private Long id;

	@NotBlank(message = "请输入名称")
	private String name;

	@NotBlank(message = "请输入终端地址")
	private String endpoint;

	private String region;

	@NotBlank(message = "请输入访问密钥")
	private String accessKey;

	@NotBlank(message = "请输入用户密钥")
	private String secretKey;

	@NotBlank(message = "请输入桶名")
	private String bucketName;

	@NotNull(message = "请选择状态")
	private Integer status;

	@NotNull(message = "请选择路径样式访问")
	private Integer pathStyleAccessEnabled;

}
