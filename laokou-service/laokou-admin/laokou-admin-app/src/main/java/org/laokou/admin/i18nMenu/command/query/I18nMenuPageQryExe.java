/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.i18nMenu.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.i18nMenu.convertor.I18nMenuConvertor;
import org.laokou.admin.i18nMenu.dto.I18nMenuPageQry;
import org.laokou.admin.i18nMenu.dto.clientobject.I18nMenuCO;
import org.laokou.admin.i18nMenu.gatewayimpl.database.I18nMenuMapper;
import org.laokou.admin.i18nMenu.gatewayimpl.database.dataobject.I18nMenuDO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分页查询国际化菜单请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class I18nMenuPageQryExe {

	private final I18nMenuMapper i18nMenuMapper;

	public Result<Page<I18nMenuCO>> execute(I18nMenuPageQry qry) {
		List<I18nMenuDO> list = i18nMenuMapper.selectObjectPage(qry);
		long total = i18nMenuMapper.selectObjectCount(qry);
		return Result.ok(Page.create(I18nMenuConvertor.toClientObjectList(list), total));
	}

}
