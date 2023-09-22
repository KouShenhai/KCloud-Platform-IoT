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
import org.laokou.admin.dto.definition.DefinitionListQry;
import org.laokou.admin.dto.definition.clientobject.DefinitionCO;
import org.laokou.admin.gatewayimpl.feign.factory.DefinitionsFeignClientFallbackFactory;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.openfeign.constant.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author laokou
 */
@FeignClient(contextId = "definitions", value = ServiceConstant.LAOKOU_FLOWABLE, path = "v1/definitions",
		fallbackFactory = DefinitionsFeignClientFallbackFactory.class)
public interface DefinitionsFeignClient {

	/**
	 * 新增流程
	 * @param file 文件
	 * @return Result<Boolean>
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "流程定义", description = "新增流程")
	Result<Boolean> insert(@RequestPart("file") MultipartFile file);

	/**
	 * 查询流程列表
	 * @param qry 查询参数
	 * @return Result<Datas<DefinitionCO>>
	 */
	@PostMapping(value = "list")
	@Operation(summary = "流程定义", description = "查询流程列表")
	Result<Datas<DefinitionCO>> list(@RequestBody DefinitionListQry qry);

	/**
	 * 挂起流程
	 * @param definitionId 定义ID
	 * @return Result<Boolean>
	 */
	@PutMapping(value = "{definitionId}/suspend")
	@Operation(summary = "流程定义", description = "挂起流程")
	Result<Boolean> suspend(@PathVariable("definitionId") String definitionId);

	/**
	 * 激活流程
	 * @param definitionId 定义ID
	 * @return Result<Boolean>
	 */
	@PutMapping(value = "{definitionId}/activate")
	@Operation(summary = "流程定义", description = "激活流程")
	Result<Boolean> activate(@PathVariable("definitionId") String definitionId);

	/**
	 * 流程图
	 * @param definitionId 定义ID
	 * @return Result<String>
	 */
	@GetMapping(value = "{definitionId}/diagram")
	@Operation(summary = "流程定义", description = "流程图")
	Result<String> diagram(@PathVariable("definitionId") String definitionId);

	/**
	 * 删除流程
	 * @param deploymentId 定义ID
	 * @return Result<Boolean>
	 */
	@DeleteMapping(value = "{deploymentId}")
	@Operation(summary = "流程定义", description = "删除流程")
	Result<Boolean> delete(@PathVariable("deploymentId") String deploymentId);

}
