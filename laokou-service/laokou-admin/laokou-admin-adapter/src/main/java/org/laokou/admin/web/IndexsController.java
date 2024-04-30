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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.IndexsServiceI;
import org.laokou.admin.dto.index.IndexGetQry;
import org.laokou.admin.dto.index.IndexListQry;
import org.laokou.admin.dto.index.IndexTraceGetQry;
import org.laokou.admin.dto.index.IndexTraceListQry;
import org.laokou.admin.dto.index.clientobject.IndexCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author laokou
 */
@RestController
@Tag(name = "IndexsController", description = "索引管理")
@RequiredArgsConstructor
@RequestMapping("v1/indexs")
public class IndexsController {

	private final IndexsServiceI indexsServiceI;

	@PostMapping("list")
	@TraceLog
	@Operation(summary = "索引管理", description = "查询索引列表")
	@PreAuthorize("hasAuthority('indexs:list')")
	public Result<Datas<IndexCO>> findList(@RequestBody IndexListQry qry) {
		return indexsServiceI.findList(qry);
	}

	@GetMapping("{indexName}")
	@TraceLog
	@Operation(summary = "索引管理", description = "查看索引")
	@PreAuthorize("hasAuthority('indexs:info')")
	public Result<Map<String, Object>> findByIndexName(@PathVariable("indexName") String indexName) {
		return indexsServiceI.findByIndexName(new IndexGetQry(indexName));
	}

	@PostMapping("trace/list")
	@TraceLog
	@Operation(summary = "索引管理", description = "查询分布式链路索引列表")
	@PreAuthorize("hasAuthority('indexs:trace-list')")
	public Result<Datas<Map<String, Object>>> findTraceList(@RequestBody IndexTraceListQry qry) {
		return indexsServiceI.findTraceList(qry);
	}

	@GetMapping("trace/{id}")
	@TraceLog
	@Operation(summary = "索引管理", description = "查看分布式链路索引")
	@PreAuthorize("hasAuthority('indexs:trace-info')")
	public Result<Map<String, Object>> findTraceById(@PathVariable("id") String id) {
		return indexsServiceI.findTraceById(new IndexTraceGetQry(id));
	}

}
