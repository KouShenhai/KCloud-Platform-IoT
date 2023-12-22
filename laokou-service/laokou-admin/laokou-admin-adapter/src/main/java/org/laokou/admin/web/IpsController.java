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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.IpsServiceI;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.admin.dto.ip.IpDeleteCmd;
import org.laokou.admin.dto.ip.IpInsertCmd;
import org.laokou.admin.dto.ip.IpListQry;
import org.laokou.admin.dto.ip.clientobject.IpCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author laokou
 */
@RestController
@Tag(name = "IpsController", description = "IP管理")
@RequiredArgsConstructor
@RequestMapping("v1/ips")
public class IpsController {

	private final IpsServiceI ipsServiceI;

	@PostMapping(value = "black/list")
	@TraceLog
	@Operation(summary = "黑名单", description = "查询IP列表")
	@PreAuthorize("hasAuthority('ips:black:list')")
	public Result<Datas<IpCO>> blacklist(@RequestBody IpListQry qry) {
		return ipsServiceI.list(qry);
	}

	@Idempotent
	@TraceLog
	@PostMapping("black")
	@Operation(summary = "黑名单", description = "新增IP")
	@OperateLog(module = "黑名单", operation = "新增IP")
	@PreAuthorize("hasAuthority('ips:black:insert')")
	public Result<Boolean> insertBlack(@Validated @RequestBody IpInsertCmd cmd) {
		return ipsServiceI.insert(cmd);
	}

	@TraceLog
	@DeleteMapping(value = "black/{id}")
	@Operation(summary = "黑名单", description = "删除IP")
	@OperateLog(module = "黑名单", operation = "删除IP")
	@PreAuthorize("hasAuthority('ips:black:delete')")
	public Result<Boolean> deleteBlackById(@PathVariable("id") Long id) {
		return ipsServiceI.deleteById(new IpDeleteCmd(id));
	}

	@PostMapping(value = "white/list")
	@TraceLog
	@Operation(summary = "白名单", description = "查询IP列表")
	@PreAuthorize("hasAuthority('ips:white:list')")
	public Result<Datas<IpCO>> whitelist(@RequestBody IpListQry qry) {
		return ipsServiceI.list(qry);
	}

	@Idempotent
	@TraceLog
	@PostMapping("white")
	@Operation(summary = "白名单", description = "新增IP")
	@OperateLog(module = "白名单", operation = "新增IP")
	@PreAuthorize("hasAuthority('ips:white:insert')")
	public Result<Boolean> insertWhite(@Validated @RequestBody IpInsertCmd cmd) {
		return ipsServiceI.insert(cmd);
	}

	@TraceLog
	@DeleteMapping(value = "white/{id}")
	@Operation(summary = "白名单", description = "删除IP")
	@OperateLog(module = "白名单", operation = "删除IP")
	@PreAuthorize("hasAuthority('ips:white:delete')")
	public Result<Boolean> deleteWhiteById(@PathVariable("id") Long id) {
		return ipsServiceI.deleteById(new IpDeleteCmd(id));
	}

}
