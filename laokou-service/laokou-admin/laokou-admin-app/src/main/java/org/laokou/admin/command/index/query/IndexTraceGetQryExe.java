/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.dto.index.IndexTraceGetQry;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 查看分布式链路索引执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class IndexTraceGetQryExe {

	/**
	 * 执行查看分布式链路索引.
	 * @param qry 查看分布式链路索引
	 * @return 分布式链路索引
	 */
	public Result<Map<String, Object>> execute(IndexTraceGetQry qry) {
//		Search search = new Search();
//		search.setIndexNames(new String[] { TRACE });
//		search.setPageSize(1);
//		search.setPageNum(1);
//		search.setOrQueryList(Collections.singletonList(new Search.Query("id", qry.getId())));
		return null;
		// return
		// Result.ok(elasticsearchTemplate.highlightSearchIndex(search).getRecords().getFirst());
	}

}
