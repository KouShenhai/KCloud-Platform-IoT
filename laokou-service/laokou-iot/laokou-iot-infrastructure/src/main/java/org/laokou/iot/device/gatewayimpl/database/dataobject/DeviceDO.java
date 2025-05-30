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

package org.laokou.iot.device.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

/**
 *
 * 设备数据对象.
 *
 * @author laokou
 */
@Data
@TableName("iot_device")
public class DeviceDO extends BaseDO {

	/**
	 * 设备序列号.
	 */
	private String sn;

	/**
	 * 设备名称.
	 */
	private String name;

	/**
	 * 设备状态 0在线 1离线.
	 */
	private Integer status;

	/**
	 * 设备经度.
	 */
	private Double longitude;

	/**
	 * 设备纬度.
	 */
	private Double latitude;

	/**
	 * 设备图片URL.
	 */
	private String imgUrl;

	/**
	 * 设备地址.
	 */
	private String address;

	/**
	 * 设备备注.
	 */
	private String remark;

	/**
	 * 产品ID.
	 */
	private Long productId;

}
