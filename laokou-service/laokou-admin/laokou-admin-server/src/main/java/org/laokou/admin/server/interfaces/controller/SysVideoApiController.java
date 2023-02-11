/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.SysResourceApplicationService;
import org.laokou.admin.server.application.service.WorkflowTaskApplicationService;
import org.laokou.admin.client.dto.SysResourceAuditDTO;
import org.laokou.admin.server.interfaces.qo.SysResourceQo;
import org.laokou.admin.client.vo.SysResourceVO;
import org.laokou.common.core.utils.DateUtil;
import org.laokou.common.log.vo.SysAuditLogVO;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.oss.client.vo.UploadVO;
import org.laokou.redis.annotation.Lock4j;
import org.laokou.redis.enums.LockScope;
import org.laokou.redis.utils.RedisKeyUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.List;
/**
 * @author laokou
 */
@RestController
@Tag(name = "Sys Resource Video API",description = "视频管理API")
@RequestMapping("/sys/resource/video/api")
@RequiredArgsConstructor
public class SysVideoApiController {

    private final SysResourceApplicationService sysResourceApplicationService;

    private final WorkflowTaskApplicationService workflowTaskApplicationService;

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "视频管理>上传",description = "视频管理>上传")
    public HttpResult<UploadVO> upload(@RequestPart("file") MultipartFile file, @RequestParam("md5")String md5) throws Exception {
        return new HttpResult<UploadVO>().ok(sysResourceApplicationService.uploadResource("video",file,md5));
    }

    @PostMapping("/query")
    @Operation(summary = "视频管理>查询",description = "视频管理>查询")
    @PreAuthorize("hasAuthority('sys:resource:video:query')")
    public HttpResult<IPage<SysResourceVO>> query(@RequestBody SysResourceQo qo) {
        return new HttpResult<IPage<SysResourceVO>>().ok(sysResourceApplicationService.queryResourcePage(qo));
    }

    @PostMapping("/syncIndex")
    @Operation(summary = "视频管理>同步索引",description = "视频管理>同步索引")
    @OperateLog(module = "视频管理",name = "同步索引")
    @Lock4j(key = "video_sync_index_lock", scope = LockScope.DISTRIBUTED_LOCK)
    @PreAuthorize("hasAuthority('sys:resource:video:syncIndex')")
    public HttpResult<Boolean> syncIndex() throws InterruptedException {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.syncResource("video", RedisKeyUtil.getSyncIndexKey("video")));
    }

    @GetMapping(value = "/detail")
    @Operation(summary = "视频管理>详情",description = "视频管理>详情")
    @PreAuthorize("hasAuthority('sys:resource:video:detail')")
    public HttpResult<SysResourceVO> detail(@RequestParam("id") Long id) {
        return new HttpResult<SysResourceVO>().ok(sysResourceApplicationService.getResourceById(id));
    }

    @PostMapping(value = "/insert")
    @Operation(summary = "视频管理>新增",description = "视频管理>新增")
    @OperateLog(module = "视频管理",name = "视频新增")
    @PreAuthorize("hasAuthority('sys:resource:video:insert')")
    public HttpResult<Boolean> insert(@RequestBody SysResourceAuditDTO dto) throws IOException {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.insertResource(dto));
    }

    @PutMapping(value = "/update")
    @Operation(summary = "视频管理>修改",description = "视频管理>修改")
    @OperateLog(module = "视频管理",name = "视频修改")
    @PreAuthorize("hasAuthority('sys:resource:video:update')")
    public HttpResult<Boolean> update(@RequestBody SysResourceAuditDTO dto) throws IOException {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.updateResource(dto));
    }

    @DeleteMapping(value = "/delete")
    @Operation(summary = "视频管理>删除",description = "视频管理>删除")
    @OperateLog(module = "视频管理",name = "视频删除")
    @PreAuthorize("hasAuthority('sys:resource:video:delete')")
    public HttpResult<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.deleteResource(id));
    }

    @GetMapping(value = "/diagram")
    @Operation(summary = "视频管理>流程图",description = "视频管理>流程图")
    @PreAuthorize("hasAuthority('sys:resource:video:diagram')")
    public void diagram(@RequestParam("processInstanceId")String processInstanceId, HttpServletResponse response) throws IOException {
        workflowTaskApplicationService.diagramProcess(processInstanceId, response);
    }

    @GetMapping("/auditLog")
    @Operation(summary = "视频管理>审批日志",description = "视频管理>审批日志")
    @PreAuthorize("hasAuthority('sys:resource:video:auditLog')")
    public HttpResult<List<SysAuditLogVO>> auditLog(@RequestParam("businessId") Long businessId) {
        return new HttpResult<List<SysAuditLogVO>>().ok(sysResourceApplicationService.queryAuditLogList(businessId));
    }

}
