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

package org.laokou.iot.thingModel.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

/**
 *
 * 物模型数据对象.
 *
 * @author laokou
 */
@Data
@TableName("boot_iot_thing_model")
public class ThingModelDO extends BaseDO {

	/**
	 * 模型名称.
	 */
	private String name;

	/**
	 * 模型编码.
	 */
	private String code;

	/**
	 * 数据类型 integer string decimal boolean.
	 */
	private String dataType;

	/**
	 * 模型类别 1属性 2事件.
	 */
	private Integer category;

	/**
	 * 模型类型 read读 write写 report上报.
	 */
	private String type;

	/**
	 * 表达式.
	 */
	private String expression;

	/**
	 * 排序.
	 */
	private Integer sort;

	/**
	 * 规则说明.
	 */
	private String specs;

	/**
	 * 物模型备注.
	 */
	private String remark;

	/**
	 * 表达式标识 0否 1是.
	 */
	private Integer expressionFlag;

}
