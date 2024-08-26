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

package org.laokou.admin.source.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.source.convertor.SourceConvertor;
import org.laokou.admin.source.dto.SourceGetQry;
import org.laokou.admin.source.dto.clientobject.SourceCO;
import org.laokou.admin.source.gatewayimpl.database.SourceMapper;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 查看数据源请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceGetQryExe {

	private final SourceMapper sourceMapper;

	public Result<SourceCO> execute(SourceGetQry qry) {
		return Result.ok(SourceConvertor.toClientObject(sourceMapper.selectById(qry.getId())));
	}

}
