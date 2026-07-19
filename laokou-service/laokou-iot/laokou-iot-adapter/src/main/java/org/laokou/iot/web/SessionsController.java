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

package org.laokou.iot.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.laokou.iot.session.api.SessionsServiceI;
import org.laokou.iot.session.dto.SessionGetQry;
import org.laokou.iot.session.dto.SessionModifyCmd;
import org.laokou.iot.session.dto.SessionPageQry;
import org.laokou.iot.session.dto.SessionRemoveCmd;
import org.laokou.iot.session.dto.SessionSaveCmd;
import org.laokou.iot.session.dto.clientobject.SessionCO;
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
 * 会话管理.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "会话管理", description = "会话管理")
public class SessionsController {

	private final SessionsServiceI sessionsServiceI;

	@Idempotent
	@PostMapping("/v1/sessions")
	@PreAuthorize("hasAuthority('write') and hasAuthority('iot:session:save')")
	@OperateLog(module = "会话管理", operation = "保存会话")
	@Operation(summary = "保存会话", description = "保存会话")
	public void saveConnection(@RequestBody SessionSaveCmd cmd) {
		sessionsServiceI.saveSession(cmd);
	}

	@PutMapping("/v1/sessions")
	@PreAuthorize("hasAuthority('write') and hasAuthority('iot:session:modify')")
	@OperateLog(module = "会话管理", operation = "修改会话")
	@Operation(summary = "修改会话", description = "修改会话")
	public void modifyConnection(@RequestBody SessionModifyCmd cmd) {
		sessionsServiceI.modifySession(cmd);
	}

	@DeleteMapping("/v1/sessions")
	@PreAuthorize("hasAuthority('write') and hasAuthority('iot:session:remove')")
	@OperateLog(module = "会话管理", operation = "删除会话")
	@Operation(summary = "删除会话", description = "删除会话")
	public void removeConnection(@RequestBody Long[] ids) {
		sessionsServiceI.removeSession(new SessionRemoveCmd(ids));
	}

	@TraceLog
	@PostMapping("/v1/sessions/page")
	@PreAuthorize("hasAuthority('read') and hasAuthority('iot:session:page')")
	@Operation(summary = "分页查询会话列表", description = "分页查询会话列表")
	public Result<Page<SessionCO>> pageConnection(@Validated @RequestBody SessionPageQry qry) {
		return sessionsServiceI.pageSession(qry);
	}

	@TraceLog
	@GetMapping("/v1/sessions/{id}")
	@PreAuthorize("hasAuthority('read') and hasAuthority('iot:session:detail')")
	@Operation(summary = "查看会话详情", description = "查看会话详情")
	public Result<SessionCO> getConnectionById(@PathVariable("id") Long id) {
		return sessionsServiceI.getSessionById(new SessionGetQry(id));
	}

}
