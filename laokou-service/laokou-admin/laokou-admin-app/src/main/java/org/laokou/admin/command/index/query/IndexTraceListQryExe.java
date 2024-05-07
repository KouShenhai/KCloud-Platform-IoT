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

package org.laokou.admin.command.index.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.index.IndexTraceListQry;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 查询分布式链路索引列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class IndexTraceListQryExe {

	private static final String TRACE_ID = "traceId";

	/**
	 * 执行查询分布式链路索引列表.
	 * @param qry 查询分布式链路索引列表参数
	 * @return 分布式链路索引列表
	 */
	public Result<Datas<Map<String, Object>>> execute(IndexTraceListQry qry) {
		// Search search = new Search();
		// search.setIndexNames(new String[] { TRACE });
		// search.setPageSize(qry.getPageSize());
		// search.setPageNum(qry.getPageNum());
		// search.setOrQueryList(Collections.singletonList(new Search.Query(TRACE_ID,
		// qry.getTraceId())));
		return null;
		// return Result.ok(elasticsearchTemplate.highlightSearchIndex(search));
	}

}
