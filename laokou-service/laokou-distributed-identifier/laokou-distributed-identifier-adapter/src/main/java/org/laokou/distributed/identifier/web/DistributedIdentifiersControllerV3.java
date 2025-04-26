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

package org.laokou.distributed.identifier.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.laokou.distributed.identifier.api.DistributedIdentifierServiceI;
import org.laokou.distributed.identifier.dto.clientobject.DistributedIdentifierCO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/distributed-identifiers")
@Tag(name = "分布式ID管理", description = "分布式ID管理")
public class DistributedIdentifiersControllerV3 {

	private final DistributedIdentifierServiceI distributedIdentifierServiceI;

	@TraceLog
	@PostMapping("snowflake")
	@Operation(summary = "生成雪花ID", description = "生成雪花ID")
	public Result<DistributedIdentifierCO> generateSnowflakeV3() {
		return distributedIdentifierServiceI.generateSnowflake();
	}

}
