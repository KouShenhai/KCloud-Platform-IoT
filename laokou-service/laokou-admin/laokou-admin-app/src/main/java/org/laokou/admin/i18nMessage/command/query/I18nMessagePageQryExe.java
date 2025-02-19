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

package org.laokou.admin.i18nMessage.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.i18nMessage.convertor.I18nMessageConvertor;
import org.laokou.admin.i18nMessage.dto.I18nMessagePageQry;
import org.laokou.admin.i18nMessage.dto.clientobject.I18nMessageCO;
import org.laokou.admin.i18nMessage.gatewayimpl.database.I18nMessageMapper;
import org.laokou.admin.i18nMessage.gatewayimpl.database.dataobject.I18nMessageDO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分页查询国际化消息请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class I18nMessagePageQryExe {

	private final I18nMessageMapper i18nMessageMapper;

	public Result<Page<I18nMessageCO>> execute(I18nMessagePageQry qry) {
		List<I18nMessageDO> list = i18nMessageMapper.selectObjectPage(qry);
		long total = i18nMessageMapper.selectObjectCount(qry);
		return Result.ok(Page.create(list.stream().map(I18nMessageConvertor::toClientObject).toList(), total));
	}

}
