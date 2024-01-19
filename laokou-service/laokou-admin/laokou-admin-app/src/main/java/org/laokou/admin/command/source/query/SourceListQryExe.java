/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.source.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.SourceConvertor;
import org.laokou.admin.domain.gateway.SourceGateway;
import org.laokou.admin.domain.source.Source;
import org.laokou.admin.dto.source.SourceListQry;
import org.laokou.admin.dto.source.clientobject.SourceCO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 查询数据源列表执行器
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceListQryExe {

	private final SourceGateway sourceGateway;

	private final SourceConvertor sourceConvertor;

	/**
	 * 执行查询数据源列表
	 * @param qry 查询数据源列表参数
	 * @return 数据源列表
	 */
	public Result<Datas<SourceCO>> execute(SourceListQry qry) {
		Source source = ConvertUtil.sourceToTarget(qry, Source.class);
		Datas<Source> newPage = sourceGateway.list(source, qry);
		Datas<SourceCO> datas = new Datas<>();
		datas.setRecords(sourceConvertor.convertClientObjectList(newPage.getRecords()));
		datas.setTotal(newPage.getTotal());
		return Result.of(datas);
	}

}
