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

package org.laokou.generator.column.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;
import java.time.Instant;

/**
 *
 * 代码生成器字段客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "代码生成器字段客户端对象", description = "代码生成器字段客户端对象")
public class ColumnCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "列名", description = "列名")
	private String name;

	@Schema(name = "描述", description = "描述")
	private String comment;

	@Schema(name = "类型", description = "类型")
	private String type;

	@Schema(name = "必填标识 0否 1是", description = "必填标识 0否 1是")
	private Integer requiredFlag;

	@Schema(name = "保存标识 0否 1是", description = "保存标识 0否 1是")
	private Integer saveFlag;

	@Schema(name = "修改标识 0否 1是", description = "修改标识 0否 1是")
	private Integer modifyFlag;

	@Schema(name = "查询标识 0否 1是", description = "查询标识 0否 1是")
	private Integer queryFlag;

	@Schema(name = "分页标识 0否 1是", description = "分页标识 0否 1是")
	private Integer pageFlag;

	@Schema(name = "查询类型", description = "查询类型")
	private String queryType;

	@Schema(name = "组件类型", description = "组件类型")
	private String componentType;

	@Schema(name = "字典类型", description = "字典类型")
	private String dictType;

	@Schema(name = "字段名称", description = "字段名称")
	private String fieldName;

	@Schema(name = "字段类型", description = "字段类型")
	private String fieldType;

	@Schema(name = "代码生成信息ID", description = "代码生成信息ID")
	private Long infoId;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createTime;

}
