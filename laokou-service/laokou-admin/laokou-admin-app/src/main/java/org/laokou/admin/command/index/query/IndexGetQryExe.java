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
import org.laokou.admin.dto.index.IndexGetQry;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 查看索引执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class IndexGetQryExe {

	/**
	 * 执行查看索引.
	 * @param qry 查看索引参数
	 * @return 索引
	 */
	public Result<Map<String, Object>> execute(IndexGetQry qry) {
		return null;
		// return Result.ok(elasticsearchTemplate.getIndexProperties(qry.getIndexName()));
	}

}
