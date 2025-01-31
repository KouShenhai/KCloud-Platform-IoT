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

package org.laokou.admin.menu.command;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.menu.ability.MenuDomainService;
import org.laokou.admin.menu.convertor.MenuConvertor;
import org.laokou.admin.menu.dto.MenuModifyCmd;
import org.laokou.admin.menu.model.MenuE;
import org.laokou.admin.menu.service.extensionpoint.MenuParamValidatorExtPt;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.constant.Constant.*;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;

/**
 * 修改菜单命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuModifyCmdExe {

	private final MenuDomainService menuDomainService;

	private final TransactionalUtil transactionalUtil;

	private final ExtensionExecutor extensionExecutor;

	public void executeVoid(MenuModifyCmd cmd) {
		// 校验参数
		MenuE menuE = MenuConvertor.toEntity(cmd.getCo());
		extensionExecutor.executeVoid(MenuParamValidatorExtPt.class, BizScenario.valueOf(MODIFY, MENU, SCENARIO),
				extension -> extension.validate(menuE));
		transactionalUtil.executeInTransaction(() -> menuDomainService.update(menuE));
	}

}
