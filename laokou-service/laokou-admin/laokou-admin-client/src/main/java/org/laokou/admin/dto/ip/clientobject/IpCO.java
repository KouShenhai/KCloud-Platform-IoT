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

package org.laokou.admin.dto.ip.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import static org.laokou.common.core.utils.RegexUtil.IPV4_REGEX;

/**
 * @author laokou
 */
@Data
@Schema(name = "IpCO", description = "IP")
public class IpCO extends ClientObject {

	@Schema(name = "id", description = "ID")
	private Long id;

	@NotBlank(message = "IP不能为空")
	@Schema(name = "value", description = "值")
	@Pattern(regexp = IPV4_REGEX, message = "IP错误")
	private String value;

	@NotBlank(message = "标签不能为空")
	@Schema(name = "label", description = "标签")
	private String label;

}
