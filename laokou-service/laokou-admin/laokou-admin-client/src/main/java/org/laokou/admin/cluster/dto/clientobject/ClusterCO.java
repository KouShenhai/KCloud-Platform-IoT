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

package org.laokou.admin.cluster.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;
import java.time.Instant;

/**
 *
 * 集群客户端对象.
 *
 * @author laokou
 */
@Data
@Schema(name = "集群客户端对象", description = "集群客户端对象")
public class ClusterCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "集群名称", description = "集群名称")
	private String name;

	@Schema(name = "集群编码", description = "集群编码")
	private String code;

	@Schema(name = "集群备注", description = "集群备注")
	private String remark;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createTime;

}
