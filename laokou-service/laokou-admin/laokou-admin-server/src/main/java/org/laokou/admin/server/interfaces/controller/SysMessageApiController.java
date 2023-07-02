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
import org.laokou.admin.server.application.service.SysMessageApplicationService;
import org.laokou.admin.client.dto.MessageDTO;
import org.laokou.admin.server.interfaces.qo.SysMessageQo;
import org.laokou.admin.client.vo.MessageDetailVO;
import org.laokou.admin.client.vo.SysMessageVO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Sys Message API", description = "系统消息API")
@RequestMapping("/sys/message/api")
public class SysMessageApiController {

	private final SysMessageApplicationService sysMessageApplicationService;

	@TraceLog
	@PostMapping("/insert")
	@Operation(summary = "系统消息>新增", description = "系统消息>新增")
	@OperateLog(module = "系统消息", name = "消息新增")
	@PreAuthorize("hasAuthority('sys:message:insert')")
	public HttpResult<Boolean> insert(@RequestBody MessageDTO dto) throws IOException {
		return new HttpResult<Boolean>().ok(sysMessageApplicationService.insertMessage(dto));
	}

	@TraceLog
	@PostMapping("/query")
	@Operation(summary = "系统消息>查询", description = "系统消息>查询")
	@PreAuthorize("hasAuthority('sys:message:query')")
	public HttpResult<IPage<SysMessageVO>> query(@RequestBody SysMessageQo qo) {
		return new HttpResult<IPage<SysMessageVO>>().ok(sysMessageApplicationService.queryMessagePage(qo));
	}

	@TraceLog
	@GetMapping("/get")
	@Operation(summary = "系统消息>查看", description = "系统消息>查看")
	@OperateLog(module = "系统消息", name = "消息查看")
	@DataCache(name = "message", key = "#id")
	public HttpResult<MessageDetailVO> get(@RequestParam("id") Long id) {
		return new HttpResult<MessageDetailVO>().ok(sysMessageApplicationService.getMessageByDetailId(id));
	}

	@TraceLog
	@GetMapping("/detail")
	@Operation(summary = "系统消息>详情", description = "系统消息>详情")
	@PreAuthorize("hasAuthority('sys:message:detail')")
	@DataCache(name = "message", key = "#id")
	public HttpResult<MessageDetailVO> detail(@RequestParam("id") Long id) {
		return new HttpResult<MessageDetailVO>().ok(sysMessageApplicationService.getMessageById(id));
	}

	@TraceLog
	@PostMapping("/unread/list")
	@Operation(summary = "系统消息>未读", description = "系统消息>未读")
	public HttpResult<IPage<SysMessageVO>> unread(@RequestBody SysMessageQo qo) {
		return new HttpResult<IPage<SysMessageVO>>().ok(sysMessageApplicationService.getUnReadList(qo));
	}

	@TraceLog
	@GetMapping("/count")
	@Operation(summary = "系统消息>统计", description = "系统消息>统计")
	public HttpResult<Long> count() {
		return new HttpResult<Long>().ok(sysMessageApplicationService.unReadCount());
	}

}
