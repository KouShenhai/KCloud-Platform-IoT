/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.admin.gatewayimpl.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.laokou.admin.gatewayimpl.feign.factory.DefinitionsFeignClientFallbackFactory;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.openfeign.constant.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author laokou
 */
@FeignClient(contextId = "definitions", value = ServiceConstant.LAOKOU_FLOWABLE, path = "v1/definitions", fallbackFactory = DefinitionsFeignClientFallbackFactory.class)
public interface DefinitionsFeignClient {

    /**
     * 新增流程
     * @param file 文件
     * @return Result<Boolean>
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "流程定义", description = "新增流程")
    Result<Boolean> insert(@RequestPart("file") MultipartFile file);

}
