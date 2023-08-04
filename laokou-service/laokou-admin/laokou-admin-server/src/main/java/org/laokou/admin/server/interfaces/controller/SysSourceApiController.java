/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.admin.server.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.enums.CacheEnum;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.tenant.dto.SysSourceDTO;
import org.laokou.admin.server.application.service.SysSourceApplicationService;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.tenant.qo.SysSourceQo;
import org.laokou.common.tenant.vo.SysSourceVO;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author laokou
 */
@RestController
@RequestMapping("/sys/source/api")
@Tag(name = "Sys Source Api", description = "系统数据源API")
@RequiredArgsConstructor
public class SysSourceApiController {

	private final SysSourceApplicationService sysSourceApplicationService;

	@TraceLog
	@PostMapping("/query")
	@Operation(summary = "系统数据源>查询", description = "系统数据源>查询")
	@PreAuthorize("hasAuthority('sys:source:query')")
	public Result<IPage<SysSourceVO>> query(@RequestBody SysSourceQo qo) {
		return new Result<IPage<SysSourceVO>>().ok(sysSourceApplicationService.querySourcePage(qo));
	}

	@TraceLog
	@PostMapping("/insert")
	@Operation(summary = "系统数据源>新增", description = "系统数据源>新增")
	@OperateLog(module = "系统数据源", name = "数据源新增")
	@PreAuthorize("hasAuthority('sys:source:insert')")
	public Result<Boolean> insert(@RequestBody SysSourceDTO dto) {
		return new Result<Boolean>().ok(sysSourceApplicationService.insertSource(dto));
	}

	@TraceLog
	@GetMapping("/detail")
	@Operation(summary = "系统数据源>查看", description = "系统数据源>查看")
	@DataCache(name = "source", key = "#id")
	public Result<SysSourceVO> detail(@RequestParam("id") Long id) {
		return new Result<SysSourceVO>().ok(sysSourceApplicationService.getSourceById(id));
	}

	@TraceLog
	@PutMapping("/update")
	@Operation(summary = "系统数据源>修改", description = "系统数据源>修改")
	@OperateLog(module = "系统数据源", name = "数据源修改")
	@PreAuthorize("hasAuthority('sys:source:update')")
	@DataCache(name = "source", key = "#dto.id", type = CacheEnum.DEL)
	public Result<Boolean> update(@RequestBody SysSourceDTO dto) {
		return new Result<Boolean>().ok(sysSourceApplicationService.updateSource(dto));
	}

	@TraceLog
	@DeleteMapping("/delete")
	@Operation(summary = "系统数据源>删除", description = "系统数据源>删除")
	@OperateLog(module = "系统数据源", name = "数据源删除")
	@PreAuthorize("hasAuthority('sys:source:delete')")
	@DataCache(name = "source", key = "#id", type = CacheEnum.DEL)
	public Result<Boolean> delete(@RequestParam("id") Long id) {
		return new Result<Boolean>().ok(sysSourceApplicationService.deleteSource(id));
	}

	@TraceLog
	@GetMapping("/option/list")
	@Operation(summary = "系统数据源>下拉框列表", description = "系统数据源>下拉框列表")
	public Result<List<OptionVO>> optionList() {
		return new Result<List<OptionVO>>().ok(sysSourceApplicationService.getOptionList());
	}

}
