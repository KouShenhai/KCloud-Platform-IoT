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

package org.laokou.iot.device.gatewayimpl.mcp;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.JacksonUtils;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Map;

/**
 * @author laokou
 */
@Slf4j
public class DeviceMcpMapper {

	private final ToolCallbackProvider tools;

	public DeviceMcpMapper(@Qualifier("distributedSyncToolCallback") ToolCallbackProvider tools) {
		this.tools = tools;
	}

	public void getDeviceInfo() {
		System.out.println(getDeviceInfo("TEST"));
	}

	public String getDeviceInfo(String deviceSN) {
		Assert.hasText(deviceSN, "deviceSN must not be blank");
		String toolName = McpToolUtils.prefixedToolName("laokou-ai-iot-mcp-server", "getDeviceInfo");
		ToolCallback[] callbacks = tools.getToolCallbacks();
		for (ToolCallback callback : callbacks) {
			if (toolName.equals(callback.getToolDefinition().name())) {
				return callback.call(JacksonUtils.toJsonStr(Map.of("deviceSN", deviceSN)));
			}
		}
		throw new IllegalStateException("MCP tool not found: " + toolName + ", available tools: "
				+ Arrays.stream(callbacks).map(callback -> callback.getToolDefinition().name()).toList());
	}

}
