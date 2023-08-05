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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.dto.SysResourceAuditDTO;
import org.laokou.admin.client.enums.ResourceTypeEnum;
import org.laokou.admin.client.vo.SysResourceVO;
import org.laokou.admin.server.application.service.SysResourceApplicationService;
import org.laokou.admin.server.application.service.WorkflowTaskApplicationService;
import org.laokou.admin.server.interfaces.qo.SysResourceQo;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.lock.annotation.Lock4j;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.log.vo.SysAuditLogVO;
import org.laokou.common.oss.vo.UploadVO;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author laokou
 */
@RestController
@Tag(name = "Sys Resource Audio API", description = "音频管理API")
@RequestMapping("audio")
@RequiredArgsConstructor
public class SysAudioApiController {

	private static final String AUDIO_CODE = ResourceTypeEnum.AUDIO.getCode();

	private final SysResourceApplicationService sysResourceApplicationService;

	private final WorkflowTaskApplicationService workflowTaskApplicationService;

	@GetMapping("v1/audit_log/{businessId}")
	@TraceLog
	@Operation(summary = "日志", description = "日志")
	@PreAuthorize("hasAuthority('audio:audit:log')")
	public Result<List<SysAuditLogVO>> auditLog(@PathVariable("businessId") Long businessId) {
		return Result.of(sysResourceApplicationService.queryAuditLogList(businessId));
	}

	@PostMapping("v1/sync")
	@TraceLog
	@Operation(summary = "同步", description = "同步")
	@OperateLog(module = "音频管理", name = "同步")
	@Lock4j(key = "audio_sync_lock_")
	@PreAuthorize("hasAuthority('audio:sync')")
	public Result<Boolean> sync() throws InterruptedException {
		return Result.of(sysResourceApplicationService.syncResource(AUDIO_CODE, RedisKeyUtil.getSyncIndexKey(AUDIO_CODE)));
	}

	@PostMapping(value = "v1/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@TraceLog
	@Operation(summary = "上传", description = "上传")
	public Result<UploadVO> upload(@RequestPart("file") MultipartFile file, @RequestParam("md5") String md5)
			throws Exception {
		return Result.of(sysResourceApplicationService.uploadResource(AUDIO_CODE, file, md5));
	}

	@PostMapping("/query")
	@Operation(summary = "音频管理>查询", description = "音频管理>查询")
	@TraceLog
	@PreAuthorize("hasAuthority('sys:resource:audio:query')")
	public Result<IPage<SysResourceVO>> query(@RequestBody SysResourceQo qo) {
		return Result.of(sysResourceApplicationService.queryResourcePage(qo));
	}

	@GetMapping(value = "/detail")
	@Operation(summary = "音频管理>详情", description = "音频管理>详情")
	@TraceLog
	@PreAuthorize("hasAuthority('sys:resource:audio:detail')")
	public Result<SysResourceVO> detail(@RequestParam("id") Long id) {
		return Result.of(sysResourceApplicationService.getResourceById(id));
	}

	@GetMapping(value = "/download")
	@TraceLog
	@Operation(summary = "音频管理>下载", description = "音频管理>下载")
	@PreAuthorize("hasAuthority('sys:resource:audio:download')")
	public void download(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
		sysResourceApplicationService.downLoadResource(id, response);
	}

	@PostMapping(value = "/insert")
	@TraceLog
	@Operation(summary = "音频管理>新增", description = "音频管理>新增")
	@OperateLog(module = "音频管理", name = "音频新增")
	@PreAuthorize("hasAuthority('sys:resource:audio:insert')")
	public Result<Boolean> insert(@RequestBody SysResourceAuditDTO dto) throws IOException {
		return Result.of(sysResourceApplicationService.insertResource(dto));
	}

	@PutMapping(value = "/update")
	@TraceLog
	@Operation(summary = "音频管理>修改", description = "音频管理>修改")
	@OperateLog(module = "音频管理", name = "音频修改")
	@PreAuthorize("hasAuthority('sys:resource:audio:update')")
	public Result<Boolean> update(@RequestBody SysResourceAuditDTO dto) throws IOException {
		return Result.of(sysResourceApplicationService.updateResource(dto));
	}

	@DeleteMapping(value = "/delete")
	@TraceLog
	@Operation(summary = "音频管理>删除", description = "音频管理>删除")
	@OperateLog(module = "音频管理", name = "音频删除")
	@PreAuthorize("hasAuthority('sys:resource:audio:delete')")
	public Result<Boolean> delete(@RequestParam("id") Long id) {
		return Result.of(sysResourceApplicationService.deleteResource(id));
	}

	@GetMapping(value = "/diagram")
	@TraceLog
	@Operation(summary = "音频管理>流程图", description = "音频管理>流程图")
	@PreAuthorize("hasAuthority('sys:resource:audio:diagram')")
	public Result<String> diagram(@RequestParam("processInstanceId") String processInstanceId) throws IOException {
		return Result.of(workflowTaskApplicationService.diagramProcess(processInstanceId));
	}

}
