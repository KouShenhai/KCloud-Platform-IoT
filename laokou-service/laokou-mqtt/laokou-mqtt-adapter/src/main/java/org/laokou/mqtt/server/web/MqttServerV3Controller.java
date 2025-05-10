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

package org.laokou.mqtt.server.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.laokou.mqtt.server.api.MqttServerServiceI;
import org.laokou.mqtt.server.dto.MqttServerLoadbalancerGetQry;
import org.laokou.mqtt.server.dto.clientobject.InstanceCO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/mqtt-servers")
@Tag(name = "MQTT服务器管理", description = "MQTT服务器管理")
public class MqttServerV3Controller {

	private final MqttServerServiceI mqttServerService;

	@TraceLog
	@GetMapping("loadbalancer/{type}")
	@Operation(summary = "查看节点【负载均衡】", description = "查看节点【负载均衡】")
	public Mono<Result<InstanceCO>> loadbalancerV3(@PathVariable("type") String type) {
		return mqttServerService.loadbalancer(new MqttServerLoadbalancerGetQry(Mono.just(type)));
	}

}
