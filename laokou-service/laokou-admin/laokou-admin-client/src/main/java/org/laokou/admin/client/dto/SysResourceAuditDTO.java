/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.client.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author laokou
 */
@Data
public class SysResourceAuditDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 944715502102587415L;

	private Long resourceId;

	@NotBlank(message = "请输入标题")
	private String title;

	@NotBlank(message = "资源不存在，请重新上传")
	private String url;

	@NotBlank(message = "请输入类型")
	private String code;

	@NotBlank(message = "请输入备注")
	private String remark;

}
