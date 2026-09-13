package org.laokou.iot.mcp.server.config;

import org.laokou.iot.mcp.server.device.tool.DeviceToolI;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class McpConfig {

	@Bean
	ToolCallbackProvider deviceToolCallbackProvider(DeviceToolI deviceTool) {
		return MethodToolCallbackProvider.builder().toolObjects(deviceTool).build();
	}

}
