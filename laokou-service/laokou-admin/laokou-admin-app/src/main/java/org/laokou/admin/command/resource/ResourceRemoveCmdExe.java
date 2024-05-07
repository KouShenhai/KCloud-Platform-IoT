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

package org.laokou.admin.command.resource;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.ResourceGateway;
import org.laokou.admin.dto.resource.ResourceRemoveCmd;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 删除资源执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ResourceRemoveCmdExe {

	private final ResourceGateway resourceGateway;

	/**
	 * 执行删除资源.
	 * @param cmd 删除资源参数
	 * @return 执行删除结果
	 */
	public Result<Boolean> execute(ResourceRemoveCmd cmd) {
		return Result.ok(resourceGateway.deleteById(cmd.getId()));
	}

}
