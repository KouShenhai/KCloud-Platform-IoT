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

package org.laokou.auth.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.api.TokensServiceI;
import org.laokou.auth.dto.TokenRemoveCmd;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/tokens")
@Tag(name = "令牌管理", description = "令牌管理")
public class TokensV3Controller {

	private final TokensServiceI tokensServiceI;

	@DeleteMapping
	@Operation(summary = "删除令牌", description = "删除令牌")
	public Mono<Void> removeTokenV3(@RequestBody TokenRemoveCmd cmd) {
		return tokensServiceI.removeToken(cmd);
	}

}
