/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.gatewayimpl.rpc;

/**
 * 定义流程.
 *
 * @author laokou
 */
/*
 * @FeignClient(contextId = "definitions", name = LAOKOU_FLOWABLE_SERVICE, path =
 * "v1/definitions", fallbackFactory = DefinitionsFeignClientFallbackFactory.class) public
 * interface DefinitionsFeignClient {
 *
 */
/**
 * 新增流程.
 * @param file 文件
 * @return 新增结果
 *//*
	 *
	 * @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE) Result<Boolean>
	 * insert(@RequestPart("file") MultipartFile file);
	 *
	 */
/**
 * 查询任务流程列表.
 * @param qry 查询任务流程列表参数
 * @return 流程列表
 *//*
	 *
	 * @PostMapping("list") Result<Datas<DefinitionCO>> list(@RequestBody
	 * DefinitionListQry qry);
	 *
	 */
/**
 * 挂起流程.
 * @param definitionId 定义ID
 * @return 挂起结果
 *//*
	 *
	 * @PutMapping("{definitionId}/suspend") Result<Boolean>
	 * suspend(@PathVariable("definitionId") String definitionId);
	 *
	 */
/**
 * 激活流程.
 * @param definitionId 定义ID
 * @return 激活结果
 *//*
	 *
	 * @PutMapping("{definitionId}/activate") Result<Boolean>
	 * activate(@PathVariable("definitionId") String definitionId);
	 *
	 */
/**
 * 查看流程图.
 * @param definitionId 定义ID
 * @return 流程图
 *//*
	 *
	 * @GetMapping("{definitionId}/diagram") Result<String>
	 * diagram(@PathVariable("definitionId") String definitionId);
	 *
	 */
/**
 * 删除流程.
 * @param deploymentId 定义ID
 * @return 删除结果
 *//*
	 *
	 * @DeleteMapping("{deploymentId}") Result<Boolean>
	 * delete(@PathVariable("deploymentId") String deploymentId);
	 *
	 * }
	 */
