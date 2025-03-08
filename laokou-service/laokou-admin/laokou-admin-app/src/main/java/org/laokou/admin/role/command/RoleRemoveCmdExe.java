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

package org.laokou.admin.role.command;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.role.ability.RoleDomainService;
import org.laokou.admin.role.dto.RoleRemoveCmd;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * 删除角色命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleRemoveCmdExe {

	private final RoleDomainService roleDomainService;

	private final TransactionalUtil transactionalUtil;

	public Flux<Void> executeVoid(RoleRemoveCmd cmd) {
		// 校验参数
		return transactionalUtil.executeResultInTransaction(() -> roleDomainService.delete(cmd.getIds()));
	}

}
