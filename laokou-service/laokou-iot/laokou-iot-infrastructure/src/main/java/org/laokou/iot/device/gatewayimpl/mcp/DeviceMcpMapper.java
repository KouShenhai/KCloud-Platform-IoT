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
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author laokou
 */
@Slf4j
public class DeviceMcpMapper {

	private final ToolCallbackProvider tools;

	public DeviceMcpMapper(@Qualifier("distributedSyncToolCallback") ToolCallbackProvider tools) {
		this.tools = tools;
	}

	public void getDeviceInfo() throws InterruptedException {
		while (true) {
			for (ToolCallback toolCallback : tools.getToolCallbacks()) {
				log.info(">>>>>>>>>>>>>>>>>>> -> {}", toolCallback.getClass().getSimpleName());
			}
			Thread.sleep(3000);
		}
	}

}
