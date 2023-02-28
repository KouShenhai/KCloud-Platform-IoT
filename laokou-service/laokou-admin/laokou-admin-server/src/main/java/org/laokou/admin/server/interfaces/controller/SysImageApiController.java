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
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.SysResourceApplicationService;
import org.laokou.admin.server.application.service.WorkflowTaskApplicationService;
import org.laokou.admin.client.dto.SysResourceAuditDTO;
import org.laokou.admin.server.interfaces.qo.SysResourceQo;
import org.laokou.admin.client.vo.SysResourceVO;
import org.laokou.common.log.vo.SysAuditLogVO;
import org.laokou.common.i18n.core.HttpResult;
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
import java.util.*;
/**
 * @author laokou
 */
@RestController
@Tag(name = "Sys Resource Image API",description = "图片管理API")
@RequestMapping("/sys/resource/image/api")
@RequiredArgsConstructor
public class SysImageApiController {

    private final SysResourceApplicationService sysResourceApplicationService;

    private final WorkflowTaskApplicationService workflowTaskApplicationService;

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "图片管理>上传",description = "图片管理>上传")
    public HttpResult<UploadVO> upload(@RequestPart("file") MultipartFile file, @RequestParam("md5")String md5) throws Exception {
        return new HttpResult<UploadVO>().ok(sysResourceApplicationService.uploadResource("image",file,md5));
    }

    @PostMapping("/syncIndex")
    @Operation(summary = "图片管理>同步索引",description = "图片管理>同步索引")
    @OperateLog(module = "图片管理",name = "同步索引")
    @Lock4j(key = "image_sync_index_lock", scope = LockScope.DISTRIBUTED_LOCK)
    @PreAuthorize("hasAuthority('sys:resource:image:syncIndex')")
    public HttpResult<Boolean> syncIndex() throws InterruptedException {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.syncResource("image", RedisKeyUtil.getSyncIndexKey("image")));
    }

    @PostMapping("/query")
    @Operation(summary = "图片管理>查询",description = "图片管理>查询")
    @PreAuthorize("hasAuthority('sys:resource:image:query')")
    public HttpResult<IPage<SysResourceVO>> query(@RequestBody SysResourceQo qo) {
        return new HttpResult<IPage<SysResourceVO>>().ok(sysResourceApplicationService.queryResourcePage(qo));
    }

    @GetMapping(value = "/detail")
    @Operation(summary = "图片管理>详情",description = "图片管理>详情")
    @PreAuthorize("hasAuthority('sys:resource:image:detail')")
    public HttpResult<SysResourceVO> detail(@RequestParam("id") Long id) {
        return new HttpResult<SysResourceVO>().ok(sysResourceApplicationService.getResourceById(id));
    }

    @PostMapping(value = "/insert")
    @Operation(summary = "图片管理>新增",description = "图片管理>新增")
    @OperateLog(module = "图片管理",name = "图片新增")
    @PreAuthorize("hasAuthority('sys:resource:image:insert')")
    public HttpResult<Boolean> insert(@RequestBody SysResourceAuditDTO dto) throws IOException {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.insertResource(dto));
    }

    @PutMapping(value = "/update")
    @Operation(summary = "图片管理>修改",description = "图片管理>修改")
    @OperateLog(module = "图片管理",name = "图片修改")
    @PreAuthorize("hasAuthority('sys:resource:image:update')")
    public HttpResult<Boolean> update(@RequestBody SysResourceAuditDTO dto) throws IOException {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.updateResource(dto));
    }

    @DeleteMapping(value = "/delete")
    @Operation(summary = "图片管理>删除",description = "图片管理>删除")
    @OperateLog(module = "图片管理",name = "图片删除")
    @PreAuthorize("hasAuthority('sys:resource:image:delete')")
    public HttpResult<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.deleteResource(id));
    }

    @GetMapping(value = "/diagram")
    @Operation(summary = "图片管理>流程图",description = "图片管理>流程图")
    @PreAuthorize("hasAuthority('sys:resource:image:diagram')")
    public HttpResult<String> diagram(@RequestParam("processInstanceId")String processInstanceId) throws IOException {
        return new HttpResult<String>().ok(workflowTaskApplicationService.diagramProcess(processInstanceId));
    }

    @GetMapping("/auditLog")
    @Operation(summary = "图片管理>审批日志",description = "图片管理>审批日志")
    @PreAuthorize("hasAuthority('sys:resource:image:auditLog')")
    public HttpResult<List<SysAuditLogVO>> auditLog(@RequestParam("businessId") Long businessId) {
        return new HttpResult<List<SysAuditLogVO>>().ok(sysResourceApplicationService.queryAuditLogList(businessId));
    }

}
