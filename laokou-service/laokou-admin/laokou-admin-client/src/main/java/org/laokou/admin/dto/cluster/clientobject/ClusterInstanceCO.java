/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.dto.cluster.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * @author laokou
 */
@Data
@Schema(name = "ClusterInstanceCO", description = "集群服务实例")
public class ClusterInstanceCO extends ClientObject {

	@Schema(name = "router", description = "路由")
	private String router;

	@Schema(name = "host", description = "主机")
	private String host;

	@Schema(name = "port", description = "端口")
	private Integer port;

	public ClusterInstanceCO() {
	}

	public ClusterInstanceCO(String router, String host, Integer port) {
		this.router = router;
		this.host = host;
		this.port = port;
	}

}
