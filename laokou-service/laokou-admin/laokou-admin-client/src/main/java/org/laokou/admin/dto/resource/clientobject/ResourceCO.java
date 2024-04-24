/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.dto.resource.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * @author laokou
 */
@Data
@Schema(name = "ResourceCO", description = "资源")
public class ResourceCO extends ClientObject {

	@Schema(name = "id", description = "ID")
	private Long id;

	@NotBlank(message = "名称不能为空")
	@Schema(name = "title", description = "资源名称")
	private String title;

	@NotBlank(message = "资源地址不能为空")
	@Schema(name = "url", description = "资源的URL")
	@URL(message = "资源地址错误", regexp = "^(http|https)://([\\w.]+)(:([0-9]+))?(/.*)$")
	private String url;

	@NotNull(message = "状态不能为空")
	@Schema(name = "status", description = "资源审批状态 0待审批 1审批中 -1驳回审批 2通过审批")
	private Integer status;

	@NotBlank(message = "编码不能为空")
	@Schema(name = "code", description = "资源类型 audio音频 video视频  image图片")
	private String code;

	@NotBlank(message = "备注不能为空")
	@Schema(name = "remark", description = "资源备注")
	private String remark;

	@Schema(name = "instanceId", description = "实例ID")
	private String instanceId;

}
