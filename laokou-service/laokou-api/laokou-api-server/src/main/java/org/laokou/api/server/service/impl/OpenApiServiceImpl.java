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

package org.laokou.api.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.laokou.api.client.ApiConstant;
import org.laokou.api.client.ParamDTO;
import org.laokou.api.server.service.OpenApiService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class OpenApiServiceImpl implements OpenApiService {

    private final WebClient webClient;

    @Override
    public Mono<String> doGet(String uri) {
        return webClient.get().uri(uri)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public Mono<String> doPost(ParamDTO dto) {
        return webClient.post()
                .uri(dto.getUri(), dto.getUuid())
                .bodyValue(dto.getParams())
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public Mono<String> toPost(ParamDTO dto) {
        return webClient.post()
                .uri(dto.getUri(), dto.getUuid())
                .bodyValue(dto.getParams())
                .retrieve()
                .bodyToMono(String.class);
    }
}
