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

package org.laokou.mcp.server.service;

import org.laokou.mcp.server.api.DeviceServiceI;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
public class DeviceServiceImpl implements DeviceServiceI {

	@Override
	@Tool(name = "get_device_property_by_sn", description = "根据设备序列号查看设备属性")
	public List<String> getDevicePropertyBySn(@ToolParam(description = "设备序列号") String sn) {
		return List.of(String.format("设备序列号：%s，设备类型：温度传感器，时间：2025-09-12 20:30:00，温度：50°C", sn));
	}

}
