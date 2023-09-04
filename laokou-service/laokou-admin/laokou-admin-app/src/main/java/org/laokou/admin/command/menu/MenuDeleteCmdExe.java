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

package org.laokou.admin.command.menu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.client.dto.menu.MenuDeleteCmd;
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MenuDeleteCmdExe {

	private final MenuMapper menuMapper;
	private final TransactionalUtil transactionalUtil;

	public Result<Boolean> execute(MenuDeleteCmd cmd) {
		boolean result = transactionalUtil.execute(rollback -> {
			try {
				return menuMapper.deleteById(cmd.getId()) > 0;
			} catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				rollback.setRollbackOnly();
				return false;
			}
		});
		return Result.of(result);
	}

}
