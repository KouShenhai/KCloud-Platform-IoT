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
package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.api.MessagesServiceI;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "MessagesController", description = "消息管理")
public class MessagesController {

	private final MessagesServiceI messagesServiceI;

	@TraceLog
	@PostMapping("v1/messages")
	@Operation(summary = "消息管理", description = "新增消息")
	@OperateLog(module = "消息管理", operation = "新增消息")
	@PreAuthorize("hasAuthority('messages:insert')")
	public Result<Boolean> insert() throws IOException {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping("v1/messages/list")
	@Operation(summary = "消息管理", description = "查询消息列表")
	@PreAuthorize("hasAuthority('messages:list')")
	public Result<?> list() {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping("v1/messages/{detailId}")
	@Operation(summary = "消息管理", description = "查看消息")
	@OperateLog(module = "消息管理", operation = "查看消息")
	@DataCache(name = "messages", key = "#id")
	public Result<?> read(@PathVariable("detailId") Long detailId) {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/messages/{id}")
	@Operation(summary = "消息管理", description = "查看消息")
	@PreAuthorize("hasAuthority('messages:detail')")
	@DataCache(name = "messages", key = "#id")
	public Result<?> get(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping("v1/messages/unread-list")
	@Operation(summary = "消息管理", description = "未读消息列表")
	public Result<?> unreadList() {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/message/unread-count")
	@Operation(summary = "消息管理", description = "未读消息数量")
	public Result<Long> unreadCount() {
		return Result.of(null);
	}

}
