/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.network.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.laokou.network.connection.api.ConnectionsServiceI;
import org.laokou.network.connection.dto.ConnectionGetQry;
import org.laokou.network.connection.dto.ConnectionModifyCmd;
import org.laokou.network.connection.dto.ConnectionPageQry;
import org.laokou.network.connection.dto.ConnectionRemoveCmd;
import org.laokou.network.connection.dto.ConnectionSaveCmd;
import org.laokou.network.connection.dto.clientobject.ConnectionCO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网络连接管理.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "网络连接管理", description = "网络连接管理")
public class ConnectionsController {

	private final ConnectionsServiceI connectionsServiceI;

	@Idempotent
	@PostMapping("/v1/connections")
	@PreAuthorize("hasAuthority('write') and hasAuthority('network:connection:save')")
	@OperateLog(module = "网络连接管理", operation = "保存网络连接")
	@Operation(summary = "保存网络连接", description = "保存网络连接")
	public void saveConnection(@RequestBody ConnectionSaveCmd cmd) {
		connectionsServiceI.saveConnection(cmd);
	}

	@PutMapping("/v1/connections")
	@PreAuthorize("hasAuthority('write') and hasAuthority('network:connection:modify')")
	@OperateLog(module = "网络连接管理", operation = "修改网络连接")
	@Operation(summary = "修改网络连接", description = "修改网络连接")
	public void modifyConnection(@RequestBody ConnectionModifyCmd cmd) {
		connectionsServiceI.modifyConnection(cmd);
	}

	@DeleteMapping("/v1/connections")
	@PreAuthorize("hasAuthority('write') and hasAuthority('network:connection:remove')")
	@OperateLog(module = "网络连接管理", operation = "删除网络连接")
	@Operation(summary = "删除网络连接", description = "删除网络连接")
	public void removeConnection(@RequestBody Long[] ids) {
		connectionsServiceI.removeConnection(new ConnectionRemoveCmd(ids));
	}

	@TraceLog
	@PostMapping("/v1/connections/page")
	@PreAuthorize("hasAuthority('read') and hasAuthority('network:connection:page')")
	@Operation(summary = "查询网络连接列表", description = "查询网络连接列表")
	public Result<Page<ConnectionCO>> pageConnection(@Validated @RequestBody ConnectionPageQry qry) {
		return connectionsServiceI.pageConnection(qry);
	}

	@TraceLog
	@GetMapping("/v1/connections/{id}")
	@PreAuthorize("hasAuthority('read') and hasAuthority('network:connection:detail')")
	@Operation(summary = "查看网络连接详情", description = "查看网络连接详情")
	public Result<ConnectionCO> getConnectionById(@PathVariable("id") Long id) {
		return connectionsServiceI.getConnectionById(new ConnectionGetQry(id));
	}

}
