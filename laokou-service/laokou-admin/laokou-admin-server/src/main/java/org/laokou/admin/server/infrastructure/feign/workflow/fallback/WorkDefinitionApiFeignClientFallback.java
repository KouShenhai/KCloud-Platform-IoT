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
 *//*

package org.laokou.admin.server.infrastructure.feign.workflow.fallback;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.server.infrastructure.feign.workflow.WorkDefinitionApiFeignClient;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.client.dto.DefinitionDTO;
import org.laokou.flowable.client.vo.DefinitionVO;
import org.laokou.flowable.client.vo.PageVO;
import org.springframework.web.multipart.MultipartFile;

*/
/**
 * 服务降级
 *
 * @author laokou
 *//*

@Slf4j
@AllArgsConstructor
public class WorkDefinitionApiFeignClientFallback implements WorkDefinitionApiFeignClient {

	private final Throwable throwable;

	@Override
	public Result<Boolean> insert(MultipartFile file) {
		log.error("服务调用失败，报错原因：{}", throwable.getMessage());
		return Result.fail("流程新增失败，请联系管理员");
	}

	@Override
	public Result<PageVO<DefinitionVO>> query(DefinitionDTO dto) {
		log.error("服务调用失败，报错原因：{}", throwable.getMessage());
		return Result.of(new PageVO<>());
	}

	@Override
	public Result<String> diagram(String definitionId) {
		log.error("服务调用失败，报错原因：{}", throwable.getMessage());
		return Result.fail("流程图查看失败，请联系管理员");
	}

	@Override
	public Result<Boolean> delete(String deploymentId) {
		log.error("服务调用失败，报错原因：{}", throwable.getMessage());
		return Result.fail("流程删除失败，请联系管理员");
	}

	@Override
	public Result<Boolean> suspend(String definitionId) {
		log.error("服务调用失败，报错原因：{}", throwable.getMessage());
		return Result.fail("流程挂起失败，请联系管理员");
	}

	@Override
	public Result<Boolean> activate(String definitionId) {
		log.error("服务调用失败，报错原因：{}", throwable.getMessage());
		return Result.fail("流程激活失败，请联系管理员");
	}

}
*/
