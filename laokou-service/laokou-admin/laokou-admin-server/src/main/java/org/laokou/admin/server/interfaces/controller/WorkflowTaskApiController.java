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
import org.laokou.admin.client.vo.SysResourceVO;
import org.laokou.admin.server.application.service.SysResourceApplicationService;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.admin.server.interfaces.qo.TaskQo;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.flowable.client.dto.AuditDTO;
import org.laokou.flowable.client.vo.TaskVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
/**
 * @author laokou
 */
@RestController
@Tag(name = "Workflow Task API",description = "流程任务API")
@RequestMapping("/workflow/task/api")
@RequiredArgsConstructor
public class WorkflowTaskApiController {

    private final SysResourceApplicationService sysResourceApplicationService;

    @PostMapping(value = "/resource/query")
    @Operation(summary = "流程任务>资源查询",description = "流程任务>资源查询")
    @PreAuthorize("hasAuthority('workflow:task:resource:query')")
    public HttpResult<IPage<TaskVO>> queryResource(@RequestBody TaskQo qo) {
        return new HttpResult<IPage<TaskVO>>().ok(sysResourceApplicationService.queryResourceTask(qo));
    }

    @PostMapping(value = "/resource/audit")
    @Operation(summary = "流程任务>资源审批",description = "流程任务>资源审批")
    @OperateLog(module = "流程任务",name = "资源审批")
    @PreAuthorize("hasAuthority('workflow:task:resource:audit')")
    public HttpResult<Boolean> auditResource(@RequestBody AuditDTO dto) {
        return new HttpResult<Boolean>().ok(sysResourceApplicationService.auditResourceTask(dto));
    }

    @GetMapping(value = "/resource/detail")
    @Operation(summary = "流程任务>资源详情",description = "流程任务>资源详情")
    @PreAuthorize("hasAuthority('workflow:task:resource:detail')")
    public HttpResult<SysResourceVO> detailResource(@RequestParam("id") Long id) {
        return new HttpResult<SysResourceVO>().ok(sysResourceApplicationService.getResourceAuditByResourceId(id));
    }

}
