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

package org.laokou.iot.product.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;
import java.time.Instant;

/**
 *
 * 产品客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "产品客户端对象", description = "产品客户端对象")
public class ProductCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "产品名称", description = "产品名称")
	private String name;

	@Schema(name = "产品类别", description = "产品类别")
	private Long categoryId;

	@Schema(name = "设备类型 1直连设备 2网关设备 3监控设备", description = "设备类型 1直连设备 2网关设备 3监控设备")
	private Integer deviceType;

	@Schema(name = "产品图片URL", description = "产品图片URL")
	private String imgUrl;

	@Schema(name = "通讯协议ID", description = "通讯协议ID")
	private Long cpId;

	@Schema(name = "传输协议ID", description = "传输协议ID")
	private Long tpId;

	@Schema(name = "备注", description = "备注")
	private String remark;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createTime;

}
