/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.thingModel.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;
import java.time.Instant;

/**
 *
 * 模型客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "模型客户端对象", description = "模型客户端对象")
public class ThingModelCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "模型名称", description = "模型名称")
	private String name;

	@Schema(name = "模型编码", description = "模型编码")
	private String code;

	@Schema(name = "数据类型 integer string decimal boolean double",
			description = "数据类型 integer string decimal boolean double")
	private String dataType;

	@Schema(name = "模型类别 1属性 2事件", description = "模型类别 1属性 2事件")
	private Integer category;

	@Schema(name = "读写类型 read读 write写", description = "读写类型 read读 write写")
	private String rwType;

	@Schema(name = "表达式", description = "表达式")
	private String expression;

	@Schema(name = "排序", description = "排序")
	private Integer sort;

	@Schema(name = "规则说明", description = "规则说明")
	private String specs;

	@Schema(name = "备注", description = "备注")
	private String remark;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createTime;

}
