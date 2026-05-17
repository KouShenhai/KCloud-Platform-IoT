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

package org.laokou.network.config.http.router;

import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.network.config.http.MessageRouter;
import org.laokou.network.model.enums.HttpMessageType;
import org.springframework.stereotype.Component;

/**
 * 网关心跳消息路由器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HeartbeatGatewayMessageRouter implements MessageRouter {

	@Override
	public String route() {
		return HttpMessageType.HEARTBEAT_GATEWAY_MESSAGE.getRoute();
	}

	@Override
	public void handle(RoutingContext ctx) {

	}

}
