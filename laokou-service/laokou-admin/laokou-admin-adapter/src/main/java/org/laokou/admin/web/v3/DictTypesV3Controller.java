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

package org.laokou.admin.web.v3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.DictsServiceI;
import org.laokou.admin.dto.dict.DictListQry;
import org.laokou.admin.dto.dict.clientobject.DictTypeCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/dict-type")
@Tag(name = "DictTypesV3Controller", description = "字典类型管理")
public class DictTypesV3Controller {

	private final DictsServiceI dictsServiceI;

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('dict-type:page')")
	@Operation(summary = "字典类型管理", description = "分页查询字典类型列表")
	public Result<Datas<DictTypeCO>> pageV3(@Validated @RequestBody DictListQry qry) {
		return dictsServiceI.page(qry);
	}

}
