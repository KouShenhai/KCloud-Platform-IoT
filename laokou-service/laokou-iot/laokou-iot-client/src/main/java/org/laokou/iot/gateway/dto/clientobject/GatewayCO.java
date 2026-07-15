/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.gateway.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;
import java.time.Instant;

/**
 *
 * 网关客户端对象.
 *
 * @author laokou
 */
@Data
@Schema(name = "网关客户端对象", description = "网关客户端对象")
public class GatewayCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "网关标识", description = "网关标识")
	private String gatewayKey;

	@Schema(name = "网关名称", description = "网关名称")
	private String name;

	@Schema(name = "网关状态 0在线 1离线", description = "网关状态 0在线 1离线")
	private Integer status;

	@Schema(name = "产品ID", description = "产品ID")
	private Long productId;

	@Schema(name = "网关地址", description = "网关地址")
	private String address;

	@Schema(name = "网关经度", description = "网关经度")
	private Double longitude;

	@Schema(name = "网关纬度", description = "网关纬度")
	private Double latitude;

	@Schema(name = "网关备注", description = "网关备注")
	private String remark;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createTime;

}
