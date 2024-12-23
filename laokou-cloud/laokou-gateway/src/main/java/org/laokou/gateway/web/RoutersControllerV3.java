/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.gateway.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.gateway.annotation.Api;
import org.laokou.gateway.repository.NacosRouteDefinitionRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 路由管理.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/routers")
@Tag(name = "路由管理", description = "路由管理")
public class RoutersControllerV3 {

	private final NacosRouteDefinitionRepository nacosRouteDefinitionRepository;

	@Api
	@PostMapping
	@Operation(summary = "保存路由", description = "保存路由")
	public Flux<Boolean> saveV3() {
		return nacosRouteDefinitionRepository.saveRouters();
	}

	@Api
	@DeleteMapping
	@Operation(summary = "删除路由", description = "删除路由")
	public Mono<Boolean> removeV3() {
		return nacosRouteDefinitionRepository.removeRouters();
	}

}
