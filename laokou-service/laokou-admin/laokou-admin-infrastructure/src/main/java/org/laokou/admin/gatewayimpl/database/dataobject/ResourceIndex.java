/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author laokou
 */
@Data
@Schema(name = "ResourceIndex", description = "资源索引")
public class ResourceIndex implements Serializable {

	@Serial
	private static final long serialVersionUID = -3715061850731611381L;

	@Schema(name = "id", description = "ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	@Schema(name = "title", description = "资源名称")
	private String title;

	@Schema(name = "code", description = "资源类型 audio音频 video视频  image图片")
	private String code;

	@Schema(name = "remark", description = "资源备注")
	private String remark;

	@Schema(name = "ym", description = "年月")
	private String ym;

}
