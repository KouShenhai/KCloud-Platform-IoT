/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.api.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.laokou.api.client.ApiConstant;
import org.laokou.api.client.ParamDTO;
import org.laokou.api.server.service.OpenApiService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
/**
 * @author laokou
 */
@RestController
@RequestMapping("/open/api")
@RequiredArgsConstructor
@RefreshScope
public class OpenApiController {

    private final OpenApiService openApiService;

    @GetMapping()
    public Mono<String> doGet(HttpServletRequest request) {
        return openApiService.doGet(request.getParameter(ApiConstant.URI));
    }

    @PostMapping()
    public Mono<String> doPost(@RequestBody ParamDTO dto) {
        return openApiService.doPost(dto);
    }

    @GetMapping("/toPost")
    public Mono<String> toPost() {
        return null;
    }

}
