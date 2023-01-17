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
import org.laokou.admin.client.constant.CacheConstant;
import org.laokou.admin.client.enums.CacheEnum;
import org.laokou.admin.server.application.service.SysResourceApplicationService;
import org.laokou.admin.server.application.service.WorkflowTaskApplicationService;
import org.laokou.admin.client.dto.SysResourceDTO;
import org.laokou.admin.server.infrastructure.annotation.DataCache;
import org.laokou.admin.server.interfaces.qo.SysResourceQo;
import org.laokou.admin.client.vo.SysAuditLogVO;
import org.laokou.admin.client.vo.SysResourceVO;
import org.laokou.common.core.utils.DateUtil;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.admin.server.infrastructure.annotation.OperateLog;
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
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:56
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

    @PostMapping("/complete/syncIndex")
    @Operation(summary = "图片管理>全量同步",description = "图片管理>全量同步")
    @OperateLog(module = "图片管理",name = "全量同步")
    @Lock4j(key = "complete_image_sync_index_lock", scope = LockScope.DISTRIBUTED_LOCK)
    @PreAuthorize("hasAuthority('sys:resource:image:complete:syncIndex')")
    public HttpResult<Boolean> complete() throws InterruptedException {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.syncResource("image","", RedisKeyUtil.getSyncIndexCompleteKey("image")));
    }

    @PostMapping("/increment/syncIndex")
    @Operation(summary = "图片管理>增量同步",description = "图片管理>增量同步")
    @OperateLog(module = "图片管理",name = "增量同步")
    @Lock4j(key = "increment_image_sync_index_lock", scope = LockScope.DISTRIBUTED_LOCK)
    @PreAuthorize("hasAuthority('sys:resource:image:increment:syncIndex')")
    public HttpResult<Boolean> increment() throws InterruptedException {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.syncResource("image", DateUtil.format(new Date(),DateUtil.YM_DATE_TIME), RedisKeyUtil.getSyncIndexIncrementKey("image")));
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
    @DataCache(name = CacheConstant.IMAGE,key = "#id")
    public HttpResult<SysResourceVO> detail(@RequestParam("id") Long id) {
        return new HttpResult<SysResourceVO>().ok(sysResourceApplicationService.getResourceById(id));
    }

    @PostMapping(value = "/insert")
    @Operation(summary = "图片管理>新增",description = "图片管理>新增")
    @OperateLog(module = "图片管理",name = "图片新增")
    @PreAuthorize("hasAuthority('sys:resource:image:insert')")
    @DataCache(name = CacheConstant.IMAGE,key = "#dto.id",type = CacheEnum.DEL)
    public HttpResult<Boolean> insert(@RequestBody SysResourceDTO dto) throws IOException {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.insertResource(dto));
    }

    @PutMapping(value = "/update")
    @Operation(summary = "图片管理>修改",description = "图片管理>修改")
    @OperateLog(module = "图片管理",name = "图片修改")
    @PreAuthorize("hasAuthority('sys:resource:image:update')")
    @DataCache(name = CacheConstant.IMAGE,key = "#dto.id",type = CacheEnum.DEL)
    public HttpResult<Boolean> update(@RequestBody SysResourceDTO dto) throws IOException {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.updateResource(dto));
    }

    @DeleteMapping(value = "/delete")
    @Operation(summary = "图片管理>删除",description = "图片管理>删除")
    @OperateLog(module = "图片管理",name = "图片删除")
    @PreAuthorize("hasAuthority('sys:resource:image:delete')")
    @DataCache(name = CacheConstant.IMAGE,key = "#id",type = CacheEnum.DEL)
    public HttpResult<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.deleteResource(id));
    }

    @GetMapping(value = "/diagram")
    @Operation(summary = "图片管理>流程图",description = "图片管理>流程图")
    @PreAuthorize("hasAuthority('sys:resource:image:diagram')")
    public void diagram(@RequestParam("processInstanceId")String processInstanceId, HttpServletResponse response) throws IOException {
        workflowTaskApplicationService.diagramProcess(processInstanceId, response);
    }

    @GetMapping("/auditLog")
    @Operation(summary = "图片管理>审批日志",description = "图片管理>审批日志")
    @PreAuthorize("hasAuthority('sys:resource:image:auditLog')")
    public HttpResult<List<SysAuditLogVO>> auditLog(@RequestParam("businessId") Long businessId) {
        return new HttpResult<List<SysAuditLogVO>>().ok(sysResourceApplicationService.queryAuditLogList(businessId));
    }

}
