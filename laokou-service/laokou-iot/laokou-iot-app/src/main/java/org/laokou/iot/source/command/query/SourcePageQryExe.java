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

package org.laokou.iot.source.command.query;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.iot.source.convertor.SourceConvertor;
import org.laokou.iot.source.dto.SourcePageQry;
import org.laokou.iot.source.dto.clientobject.SourceCO;
import org.laokou.iot.source.gatewayimpl.database.SourceMapper;
import org.laokou.iot.source.gatewayimpl.database.dataobject.SourceDO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分页查询数据源请求执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SourcePageQryExe {

	private final SourceMapper sourceMapper;

	public Result<Page<SourceCO>> execute(SourcePageQry qry) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.IOT);
			List<SourceDO> list = sourceMapper.selectObjectPage(qry);
			long total = sourceMapper.selectObjectCount(qry);
			return Result.ok(Page.create(list.stream().map(SourceConvertor::toClientObject).toList(), total));}
		catch (Exception ex) {
			log.error("分页查询数据源失败，错误信息：{}", ex.getMessage(), ex);
			throw ex;
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
