/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.SysPackageApplicationService;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.enums.CacheEnum;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.common.tenant.dto.SysPackageDTO;
import org.laokou.common.tenant.qo.SysPackageQo;
import org.laokou.common.tenant.vo.SysPackageVO;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author laokou
 */
@RestController
@RequestMapping("/sys/package/api")
@Tag(name = "Sys Package Api", description = "系统套餐API")
@RequiredArgsConstructor
public class SysPackageApiController {

	private final SysPackageApplicationService sysPackageApplicationService;

	@TraceLog
	@PostMapping("/query")
	@Operation(summary = "系统套餐>查询", description = "系统套餐>查询")
	@PreAuthorize("hasAuthority('sys:package:query')")
	public HttpResult<IPage<SysPackageVO>> query(@RequestBody SysPackageQo qo) {
		return new HttpResult<IPage<SysPackageVO>>().ok(sysPackageApplicationService.queryPackagePage(qo));
	}

	@TraceLog
	@PostMapping("/insert")
	@Operation(summary = "系统套餐>新增", description = "系统套餐>新增")
	@OperateLog(module = "系统套餐", name = "套餐新增")
	@PreAuthorize("hasAuthority('sys:package:insert')")
	public HttpResult<Boolean> insert(@RequestBody SysPackageDTO dto) {
		return new HttpResult<Boolean>().ok(sysPackageApplicationService.insertPackage(dto));
	}

	@TraceLog
	@GetMapping("/detail")
	@Operation(summary = "系统套餐>查看", description = "系统套餐>查看")
	@DataCache(name = "package", key = "#id")
	public HttpResult<SysPackageVO> detail(@RequestParam("id") Long id) {
		return new HttpResult<SysPackageVO>().ok(sysPackageApplicationService.getPackageById(id));
	}

	@TraceLog
	@PutMapping("/update")
	@Operation(summary = "系统套餐>修改", description = "系统套餐>修改")
	@OperateLog(module = "系统套餐", name = "套餐修改")
	@PreAuthorize("hasAuthority('sys:package:update')")
	@DataCache(name = "package", key = "#dto.id", type = CacheEnum.DEL)
	public HttpResult<Boolean> update(@RequestBody SysPackageDTO dto) {
		return new HttpResult<Boolean>().ok(sysPackageApplicationService.updatePackage(dto));
	}

	@TraceLog
	@DeleteMapping("/delete")
	@Operation(summary = "系统套餐>删除", description = "系统套餐>删除")
	@OperateLog(module = "系统套餐", name = "套餐删除")
	@PreAuthorize("hasAuthority('sys:package:delete')")
	@DataCache(name = "package", key = "#id", type = CacheEnum.DEL)
	public HttpResult<Boolean> delete(@RequestParam("id") Long id) {
		return new HttpResult<Boolean>().ok(sysPackageApplicationService.deletePackage(id));
	}

	@TraceLog
	@GetMapping("/option/list")
	@Operation(summary = "系统套餐>下拉框列表", description = "系统套餐>下拉框列表")
	public HttpResult<List<OptionVO>> optionList() {
		return new HttpResult<List<OptionVO>>().ok(sysPackageApplicationService.getOptionList());
	}

}
