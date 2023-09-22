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

package org.laokou.admin.gatewayimpl.feign.fallback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.dto.definition.DefinitionListQry;
import org.laokou.admin.dto.definition.clientobject.DefinitionCO;
import org.laokou.admin.gatewayimpl.feign.DefinitionsFeignClient;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public class DefinitionsFeignClientFallback implements DefinitionsFeignClient {

	private final Throwable throwable;

	@Override
	public Result<Boolean> insert(MultipartFile file) {
		errLog();
		return Result.fail("新增流程失败，请联系管理员");
	}

	@Override
	public Result<Datas<DefinitionCO>> list(DefinitionListQry qry) {
		errLog();
		return Result.of(Datas.of());
	}

	@Override
	public Result<Boolean> suspend(String definitionId) {
		errLog();
		return Result.fail("挂起流程失败，请联系管理员");
	}

	@Override
	public Result<Boolean> activate(String definitionId) {
		errLog();
		return Result.fail("激活流程失败，请联系管理员");
	}

	@Override
	public Result<String> diagram(String definitionId) {
		errLog();
		return Result.fail("查看流程图失败，请联系管理员");
	}

	@Override
	public Result<Boolean> delete(String deploymentId) {
		errLog();
		return Result.fail("删除流程失败，请联系管理员");
	}

	private void errLog() {
		log.error("服务调用失败，报错原因：{}", throwable.getMessage());
	}

}
