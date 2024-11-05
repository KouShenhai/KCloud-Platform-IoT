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

package org.laokou.iot.product.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

/**
 *
 * 产品数据对象.
 *
 * @author laokou
 */
@Data
@TableName("boot_iot_product")
public class ProductDO extends BaseDO {

	/**
	 * 产品名称.
	 */
	private String name;

	/**
	 * 产品类别.
	 */
	private Long categoryId;

	/**
	 * 设备类型 1直连设备 2网关设备 3监控设备.
	 */
	private Integer deviceType;

	/**
	 * 产品图片URL.
	 */
	private String imgUrl;

	/**
	 * 通讯协议ID.
	 */
	private Long cpId;

	/**
	 * 传输协议ID.
	 */
	private Long tpId;

	/**
	 * 备注.
	 */
	private String remark;

}
