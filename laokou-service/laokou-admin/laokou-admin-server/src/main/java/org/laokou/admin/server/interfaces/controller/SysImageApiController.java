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
import org.laokou.admin.server.application.service.SysResourceApplicationService;
import org.laokou.admin.server.application.service.WorkflowTaskApplicationService;
import org.laokou.admin.client.dto.SysResourceAuditDTO;
import org.laokou.admin.server.interfaces.qo.SysResourceQo;
import org.laokou.admin.client.vo.SysResourceVO;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.lock.annotation.Lock4j;
import org.laokou.common.log.vo.SysAuditLogVO;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.oss.vo.UploadVO;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

/**
 * @author laokou
 */
@RestController
@Tag(name = "Sys Resource Image API", description = "图片管理API")
@RequestMapping("/sys/resource/image/api")
@RequiredArgsConstructor
public class SysImageApiController {

	private final SysResourceApplicationService sysResourceApplicationService;

	private final WorkflowTaskApplicationService workflowTaskApplicationService;

	@TraceLog
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "图片管理>上传", description = "图片管理>上传")
	public Result<UploadVO> upload(@RequestPart("file") MultipartFile file, @RequestParam("md5") String md5)
			throws Exception {
		return Result.of(sysResourceApplicationService.uploadResource("image", file, md5));
	}

	@TraceLog
	@PostMapping("/syncIndex")
	@Operation(summary = "图片管理>同步索引", description = "图片管理>同步索引")
	@OperateLog(module = "图片管理", name = "同步索引")
	@Lock4j(key = "image_sync_index_lock_")
	@PreAuthorize("hasAuthority('sys:resource:image:syncIndex')")
	public Result<Boolean> syncIndex() throws InterruptedException {
		return Result.of(sysResourceApplicationService.syncResource("image", RedisKeyUtil.getSyncIndexKey("image")));
	}

	@TraceLog
	@PostMapping("/query")
	@Operation(summary = "图片管理>查询", description = "图片管理>查询")
	@PreAuthorize("hasAuthority('sys:resource:image:query')")
	public Result<IPage<SysResourceVO>> query(@RequestBody SysResourceQo qo) {
		return Result.of(sysResourceApplicationService.queryResourcePage(qo));
	}

	@TraceLog
	@GetMapping(value = "/detail")
	@Operation(summary = "图片管理>详情", description = "图片管理>详情")
	@PreAuthorize("hasAuthority('sys:resource:image:detail')")
	public Result<SysResourceVO> detail(@RequestParam("id") Long id) {
		return Result.of(sysResourceApplicationService.getResourceById(id));
	}

	@TraceLog
	@GetMapping(value = "/download")
	@Operation(summary = "图片管理>下载", description = "图片管理>下载")
	@PreAuthorize("hasAuthority('sys:resource:image:download')")
	public void download(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
		sysResourceApplicationService.downLoadResource(id, response);
	}

	@TraceLog
	@PostMapping(value = "/insert")
	@Operation(summary = "图片管理>新增", description = "图片管理>新增")
	@OperateLog(module = "图片管理", name = "图片新增")
	@PreAuthorize("hasAuthority('sys:resource:image:insert')")
	public Result<Boolean> insert(@RequestBody SysResourceAuditDTO dto) throws IOException {
		return Result.of(sysResourceApplicationService.insertResource(dto));
	}

	@TraceLog
	@PutMapping(value = "/update")
	@Operation(summary = "图片管理>修改", description = "图片管理>修改")
	@OperateLog(module = "图片管理", name = "图片修改")
	@PreAuthorize("hasAuthority('sys:resource:image:update')")
	public Result<Boolean> update(@RequestBody SysResourceAuditDTO dto) throws IOException {
		return Result.of(sysResourceApplicationService.updateResource(dto));
	}

	@TraceLog
	@DeleteMapping(value = "/delete")
	@Operation(summary = "图片管理>删除", description = "图片管理>删除")
	@OperateLog(module = "图片管理", name = "图片删除")
	@PreAuthorize("hasAuthority('sys:resource:image:delete')")
	public Result<Boolean> delete(@RequestParam("id") Long id) {
		return Result.of(sysResourceApplicationService.deleteResource(id));
	}

	@TraceLog
	@GetMapping(value = "/diagram")
	@Operation(summary = "图片管理>流程图", description = "图片管理>流程图")
	@PreAuthorize("hasAuthority('sys:resource:image:diagram')")
	public Result<String> diagram(@RequestParam("processInstanceId") String processInstanceId) throws IOException {
		return Result.of(workflowTaskApplicationService.diagramProcess(processInstanceId));
	}

	@TraceLog
	@GetMapping("/auditLog")
	@Operation(summary = "图片管理>审批日志", description = "图片管理>审批日志")
	@PreAuthorize("hasAuthority('sys:resource:image:auditLog')")
	public Result<List<SysAuditLogVO>> auditLog(@RequestParam("businessId") Long businessId) {
		return Result.of(sysResourceApplicationService.queryAuditLogList(businessId));
	}

}
