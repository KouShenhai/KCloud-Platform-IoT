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
import org.laokou.admin.api.IpsServiceI;
import org.laokou.admin.dto.ip.IpCreateCmd;
import org.laokou.admin.dto.ip.IpListQry;
import org.laokou.admin.dto.ip.IpRefreshCmd;
import org.laokou.admin.dto.ip.IpRemoveCmd;
import org.laokou.admin.dto.ip.clientobject.IpCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.lock.annotation.Lock4j;
import org.laokou.common.log.annotation.OperateLog;
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

	@PostMapping("black/list")
	@TraceLog
	@Operation(summary = "黑名单", description = "查询IP列表")
	@PreAuthorize("hasAuthority('ips:black-list')")
	public Result<Datas<IpCO>> findBlacklist(@RequestBody IpListQry qry) {
		return ipsServiceI.findList(qry);
	}

	@Idempotent
	@PostMapping("black")
	@Operation(summary = "黑名单", description = "新增IP")
	@OperateLog(module = "黑名单", operation = "新增IP")
	@PreAuthorize("hasAuthority('ips:create-black')")
	public void createBlack(@Validated @RequestBody IpCreateCmd cmd) {
		ipsServiceI.create(cmd);
	}

	@DeleteMapping("black")
	@Operation(summary = "黑名单", description = "删除IP")
	@OperateLog(module = "黑名单", operation = "删除IP")
	@PreAuthorize("hasAuthority('ips:remove-black')")
	public void removeBlack(@RequestBody Long[] ids) {
		ipsServiceI.remove(new IpRemoveCmd(ids));
	}

	@PostMapping("white/list")
	@TraceLog
	@Operation(summary = "白名单", description = "查询IP列表")
	@PreAuthorize("hasAuthority('ips:white-list')")
	public Result<Datas<IpCO>> findWhitelist(@RequestBody IpListQry qry) {
		return ipsServiceI.findList(qry);
	}

	@Idempotent
	@PostMapping("white")
	@Operation(summary = "白名单", description = "新增IP")
	@OperateLog(module = "白名单", operation = "新增IP")
	@PreAuthorize("hasAuthority('ips:create-white')")
	public void createWhite(@Validated @RequestBody IpCreateCmd cmd) {
		ipsServiceI.create(cmd);
	}

	@DeleteMapping("white")
	@Operation(summary = "白名单", description = "删除IP")
	@OperateLog(module = "白名单", operation = "删除IP")
	@PreAuthorize("hasAuthority('ips:remove-white')")
	public void removeWhite(@RequestBody Long[] ids) {
		ipsServiceI.remove(new IpRemoveCmd(ids));
	}

	@GetMapping("white/refresh/{label}")
	@Operation(summary = "白名单", description = "刷新IP")
	@OperateLog(module = "白名单", operation = "刷新IP")
	@PreAuthorize("hasAuthority('ips:refresh-white')")
	@Lock4j(key = "refresh_white_ip_lock", expire = 60000)
	public void refreshWhite(@PathVariable("label") String label) {
		ipsServiceI.refresh(new IpRefreshCmd(label));
	}

	@GetMapping("black/refresh/{label}")
	@Operation(summary = "黑名单", description = "刷新IP")
	@OperateLog(module = "黑名单", operation = "刷新IP")
	@PreAuthorize("hasAuthority('ips:refresh-black')")
	@Lock4j(key = "refresh_black_ip_lock", expire = 60000)
	public void refreshBlack(@PathVariable("label") String label) {
		ipsServiceI.refresh(new IpRefreshCmd(label));
	}

}
