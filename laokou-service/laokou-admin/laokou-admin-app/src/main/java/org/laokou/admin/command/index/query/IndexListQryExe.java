/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.admin.dto.index.IndexListQry;
import org.laokou.admin.dto.index.clientobject.IndexCO;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.laokou.common.i18n.common.IndexConstants.RESOURCE;
import static org.laokou.common.i18n.common.IndexConstants.TRACE;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class IndexListQryExe {

	private final ElasticsearchTemplate elasticsearchTemplate;

	public Result<Datas<IndexCO>> execute(IndexListQry qry) {
		Map<String, String> indexNames = elasticsearchTemplate
			.getIndexNames(new String[] { RESOURCE, TRACE });
		if (MapUtil.isEmpty(indexNames)) {
			return Result.of(Datas.of());
		}
		Integer pageNum = qry.getPageNum();
		Integer pageSize = qry.getPageSize();
		String idxName = qry.getIndexName();
		List<IndexCO> list = new ArrayList<>(indexNames.size());
		indexNames.forEach((indexName, indexAlias) -> {
			if (StringUtil.isEmpty(idxName) || indexName.contains(idxName.trim())) {
				list.add(new IndexCO(indexName, indexAlias));
			}
		});
		Datas<IndexCO> datas = new Datas<>(list.size(),
				list.stream().skip((long) (pageNum - 1) * pageSize).limit(pageSize).toList());
		return Result.of(datas);
	}

}
