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

package org.laokou.mcp.client.command.query;

import com.alibaba.cloud.ai.mcp.discovery.client.transport.LoadbalancedMcpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.mcp.client.dto.DevicePropertyGetQry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 查看设备属性请求执行器.
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DevicePropertyGetQryExe {

	private final List<LoadbalancedMcpSyncClient> loadbalancedMcpSyncClients;

	public Result<String> execute(DevicePropertyGetQry qry) {
		McpSchema.CallToolResult callToolResult = loadbalancedMcpSyncClients.getFirst().callTool(new McpSchema.CallToolRequest("根据设备序列号查看设备属性", Map.of("sn", qry.getSn())));
		List<String> list = callToolResult.content().stream().map(content -> {
			if (content instanceof McpSchema.TextContent textContent) {
				return textContent.text();
			}
			return StringConstants.EMPTY;
		}).filter(StringUtils::hasText).toList();
		if (CollectionUtils.isNotEmpty(list)) {
			return Result.ok(StringUtils.collectionToDelimitedString(list, StringConstants.DROP));
		}
		return Result.ok(StringConstants.EMPTY);
	}

}
