/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.laokou.admin.dto.definition.DefinitionListQry;
import org.laokou.admin.dto.definition.clientobject.DefinitionCO;
import org.laokou.admin.gatewayimpl.feign.factory.DefinitionsFeignClientFallbackFactory;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import static org.laokou.common.openfeign.constant.ServiceConstant.LAOKOU_FLOWABLE;

/**
 * @author laokou
 */
@FeignClient(contextId = "definitions", name = LAOKOU_FLOWABLE, path = "v1/definitions",
		fallbackFactory = DefinitionsFeignClientFallbackFactory.class)
public interface DefinitionsFeignClient {

	/**
	 * 新增流程
	 * @param file 文件
	 * @return Result<Boolean>
	 */
	@RequestLine("POST")
	@Headers("Content-Type: multipart/form-data")
	Result<Boolean> insert(@RequestPart("file") MultipartFile file);

	/**
	 * 查询流程列表
	 * @param qry 查询参数
	 * @return Result<Datas<DefinitionCO>>
	 */
	@RequestLine("POST /list")
	@Headers("Content-Type: application/json")
	Result<Datas<DefinitionCO>> list(@RequestBody DefinitionListQry qry);

	/**
	 * 挂起流程
	 * @param definitionId 定义ID
	 * @return Result<Boolean>
	 */
	@RequestLine("PUT /{definitionId}/suspend")
	@Headers("Content-Type: application/json")
	Result<Boolean> suspend(@Param("definitionId") String definitionId);

	/**
	 * 激活流程
	 * @param definitionId 定义ID
	 * @return Result<Boolean>
	 */
	@RequestLine("PUT /{definitionId}/activate")
	@Headers("Content-Type: application/json")
	Result<Boolean> activate(@Param("definitionId") String definitionId);

	/**
	 * 流程图
	 * @param definitionId 定义ID
	 * @return Result<String>
	 */
	@RequestLine("GET /{definitionId}/diagram")
	@Headers("Content-Type: application/json")
	Result<String> diagram(@Param("definitionId") String definitionId);

	/**
	 * 删除流程
	 * @param deploymentId 定义ID
	 * @return Result<Boolean>
	 */
	@RequestLine("DELETE /{deploymentId}")
	@Headers("Content-Type: application/json")
	Result<Boolean> delete(@Param("deploymentId") String deploymentId);

}
