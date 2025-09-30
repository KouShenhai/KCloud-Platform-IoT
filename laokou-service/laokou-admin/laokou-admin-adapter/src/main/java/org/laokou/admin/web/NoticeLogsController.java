/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.noticeLog.api.NoticeLogsServiceI;
import org.laokou.admin.noticeLog.dto.NoticeLogExportCmd;
import org.laokou.admin.noticeLog.dto.NoticeLogGetQry;
import org.laokou.admin.noticeLog.dto.NoticeLogImportCmd;
import org.laokou.admin.noticeLog.dto.NoticeLogModifyCmd;
import org.laokou.admin.noticeLog.dto.NoticeLogPageQry;
import org.laokou.admin.noticeLog.dto.NoticeLogRemoveCmd;
import org.laokou.admin.noticeLog.dto.NoticeLogSaveCmd;
import org.laokou.admin.noticeLog.dto.clientobject.NoticeLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.secret.annotation.ApiSecret;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通知日志管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "通知日志管理", description = "通知日志管理")
public class NoticeLogsController {

	private final NoticeLogsServiceI noticeLogsServiceI;

	@ApiSecret
	@Idempotent
	@PostMapping("/v1/notice-logs")
	@PreAuthorize("hasAuthority('sys:notice-log:save')")
	@OperateLog(module = "通知日志管理", operation = "保存通知日志")
	@Operation(summary = "保存通知日志", description = "保存通知日志")
	public void saveNoticeLog(@RequestBody NoticeLogSaveCmd cmd) {
		noticeLogsServiceI.saveNoticeLog(cmd);
	}

	@ApiSecret
	@PutMapping("/v1/notice-logs")
	@PreAuthorize("hasAuthority('sys:notice-log:modify')")
	@OperateLog(module = "通知日志管理", operation = "修改通知日志")
	@Operation(summary = "修改通知日志", description = "修改通知日志")
	public void modifyNoticeLog(@RequestBody NoticeLogModifyCmd cmd) {
		noticeLogsServiceI.modifyNoticeLog(cmd);
	}

	@DeleteMapping("/v1/notice-logs")
	@PreAuthorize("hasAuthority('sys:notice-log:remove')")
	@OperateLog(module = "通知日志管理", operation = "删除通知日志")
	@Operation(summary = "删除通知日志", description = "删除通知日志")
	public void removeNoticeLog(@RequestBody Long[] ids) {
		noticeLogsServiceI.removeNoticeLog(new NoticeLogRemoveCmd(ids));
	}

	@ApiSecret
	@PostMapping(value = "/v1/notice-logs/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:notice-log:import')")
	@OperateLog(module = "通知日志管理", operation = "导入通知日志")
	@Operation(summary = "导入通知日志", description = "导入通知日志")
	public void importNoticeLog(@RequestPart("files") MultipartFile[] files) {
		noticeLogsServiceI.importNoticeLog(new NoticeLogImportCmd(files));
	}

	@PostMapping("/v1/notice-logs/export")
	@PreAuthorize("hasAuthority('sys:notice-log:export')")
	@OperateLog(module = "通知日志管理", operation = "导出通知日志")
	@Operation(summary = "导出通知日志", description = "导出通知日志")
	public void exportNoticeLog(@RequestBody NoticeLogExportCmd cmd) {
		noticeLogsServiceI.exportNoticeLog(cmd);
	}

	@TraceLog
	@PostMapping("/v1/notice-logs/page")
	@PreAuthorize("hasAuthority('sys:notice-log:page')")
	@Operation(summary = "分页查询通知日志列表", description = "分页查询通知日志列表")
	public Result<Page<NoticeLogCO>> pageNoticeLog(@Validated @RequestBody NoticeLogPageQry qry) {
		return noticeLogsServiceI.pageNoticeLog(qry);
	}

	@TraceLog
	@GetMapping("/v1/notice-logs/{id}")
	@PreAuthorize("hasAuthority('sys:notice-log:detail')")
	@Operation(summary = "查看通知日志详情", description = "查看通知日志详情")
	public Result<NoticeLogCO> getNoticeLogById(@PathVariable("id") Long id) {
		return noticeLogsServiceI.getNoticeLogById(new NoticeLogGetQry(id));
	}

}
