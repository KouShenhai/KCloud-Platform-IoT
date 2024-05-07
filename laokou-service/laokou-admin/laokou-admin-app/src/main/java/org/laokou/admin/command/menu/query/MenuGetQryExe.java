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

package org.laokou.admin.command.menu.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.MenuConvertor;
import org.laokou.admin.dto.menu.MenuGetQry;
import org.laokou.admin.dto.menu.clientobject.MenuCO;
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DSConstant.TENANT;

/**
 * 查看菜单执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuGetQryExe {

	private final MenuMapper menuMapper;

	private final MenuConvertor menuConvertor;

	/**
	 * 执行查看菜单.
	 * @param qry 查看菜单参数
	 * @return 菜单
	 */
	@DS(TENANT)
	public Result<MenuCO> execute(MenuGetQry qry) {
		return Result.ok(menuConvertor.convertClientObj(menuMapper.selectById(qry.getId())));
	}

}
