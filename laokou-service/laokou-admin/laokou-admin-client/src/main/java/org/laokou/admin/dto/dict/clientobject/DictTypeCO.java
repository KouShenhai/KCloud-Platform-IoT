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

package org.laokou.admin.dto.dict.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Data
@Schema(name = "DictTypeCO", description = "字典类型")
public class DictTypeCO extends ClientObject {

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "name", description = "字典名称")
	private String name;

	@Schema(name = "type", description = "字典类型")
	private String type;

        @Schema(name = "状态", description = "字典状态 0启用 1停用")
	private Integer status;

	@Schema(name = "remark", description = "字典备注")
	private String remark;

	@Schema(name = "createDate", description = "创建时间")
	private LocalDateTime createDate;

}
