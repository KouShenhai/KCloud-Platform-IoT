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

package org.laokou.iot.device.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;
import java.time.Instant;

/**
 *
 * 设备客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "设备客户端对象", description = "设备客户端对象")
public class DeviceCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "设备序列号", description = "设备序列号")
	private String sn;

	@Schema(name = "设备名称", description = "设备名称")
	private String name;

	@Schema(name = "设备状态 0在线 1离线", description = "设备状态 0在线 1离线")
	private Integer status;

	@Schema(name = "设备经度", description = "设备经度")
	private Double longitude;

	@Schema(name = "设备纬度", description = "设备纬度")
	private Double latitude;

	@Schema(name = "设备图片URL", description = "设备图片URL")
	private String imgUrl;

	@Schema(name = "设备地址", description = "设备地址")
	private String address;

	@Schema(name = "设备备注", description = "设备备注")
	private String remark;

	@Schema(name = "产品ID", description = "产品ID")
	private Long productId;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createTime;

}
