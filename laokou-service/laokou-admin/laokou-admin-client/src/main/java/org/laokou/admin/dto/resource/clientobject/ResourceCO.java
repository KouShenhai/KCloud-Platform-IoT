/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * @author laokou
 */
@Data
public class ResourceCO extends ClientObject {

	private Long id;

	@NotBlank(message = "标题不能为空")
	private String title;

	@NotBlank(message = "资源地址不能为空")
	@URL(message = "资源地址错误", regexp = "^(http|https)://([\\w.]+)(:([0-9]+))?(/.*)$")
	private String url;

	@NotNull(message = "状态不能为空")
	private Integer status;

	@NotBlank(message = "编码不能为空")
	private String code;

	@NotBlank(message = "备注不能为空")
	private String remark;

	private String instanceId;

}
