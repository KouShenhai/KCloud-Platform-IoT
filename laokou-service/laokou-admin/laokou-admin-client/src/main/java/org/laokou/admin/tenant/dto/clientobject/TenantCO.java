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

package org.laokou.admin.tenant.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * 租户客户端对象.
 *
 * @author laokou
 */
@Data
@Schema(name = "租户客户端对象", description = "租户客户端对象")
public class TenantCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "租户名称", description = "租户名称")
	private String name;

	@Schema(name = "租户编码", description = "租户编码")
	private String code;

	@Schema(name = "数据源ID", description = "数据源ID")
	private Long sourceId;

	@Schema(name = "套餐ID", description = "套餐ID")
	private Long packageId;

}
