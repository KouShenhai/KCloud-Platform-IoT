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

package org.laokou.admin.menu.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.menu.dto.MenuTreeListQry;
import org.laokou.admin.menu.dto.clientobject.MenuTreeCO;
import org.laokou.admin.menu.gatewayimpl.database.MenuMapper;
import org.laokou.admin.menu.service.extensionpoint.MenuTreeBuilderExtPt;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;
import java.util.List;

import static org.laokou.admin.common.constant.BizConstants.SCENARIO;
import static org.laokou.admin.common.constant.BizConstants.USE_CASE_MENU;

/**
 * 查询菜单请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuTreeListQryExe {

	private final MenuMapper menuMapper;

	private final ExtensionExecutor extensionExecutor;

	public Result<List<MenuTreeCO>> execute(MenuTreeListQry qry) {
		MenuTreeCO co = extensionExecutor.execute(MenuTreeBuilderExtPt.class,
				BizScenario.valueOf(String.valueOf(qry.getCode()), USE_CASE_MENU, SCENARIO),
				extension -> extension.build(qry, UserContextHolder.get().getId(), menuMapper));
		return Result.ok(co.getChildren());
	}

}
