/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.dto.monitor.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.io.Serial;
import java.util.List;
import java.util.Map;

/**
 * @author laokou
 */
@Data
@Schema(name = "RedisCacheCO", description = "Redis缓存")
public class RedisCacheCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = 9153324620769020304L;

	@Schema(name = "keysSize", description = "RedisKey大小")
	private Long keysSize;

	@Schema(name = "info", description = "Redis信息")
	private Map<String, String> info;

	@Schema(name = "commandStats", description = "Redis命令统计信息")
	private List<Map<String, String>> commandStats;

	public RedisCacheCO(Long keysSize, Map<String, String> info, List<Map<String, String>> commandStats) {
		this.keysSize = keysSize;
		this.info = info;
		this.commandStats = commandStats;
	}

}
