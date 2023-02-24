/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.flowable.server.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.flowable.client.dto.AuditDTO;
import org.laokou.flowable.client.dto.ProcessDTO;
import org.laokou.flowable.client.dto.TaskDTO;
import org.laokou.flowable.client.vo.AssigneeVO;
import org.laokou.flowable.client.vo.PageVO;
import org.laokou.flowable.client.vo.TaskVO;
import org.laokou.flowable.server.service.WorkTaskService;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

/**
 * @author laokou
 */
@RestController
@Tag(name = "Work Task API",description = "流程任务API")
@RequestMapping("/work/task/api")
@RequiredArgsConstructor
public class WorkTaskApiController {

    private final WorkTaskService workTaskService;

    @PostMapping(value = "/query")
    @Operation(summary = "流程任务>任务查询",description = "流程任务>任务查询")
    public HttpResult<PageVO<TaskVO>> query(@RequestBody TaskDTO dto) {
        return new HttpResult<PageVO<TaskVO>>().ok(workTaskService.queryTaskPage(dto));
    }

    @PostMapping(value = "/audit")
    @Operation(summary = "流程任务>任务审批",description = "流程任务>任务审批")
    public HttpResult<AssigneeVO> audit(@RequestBody AuditDTO dto) {
        return new HttpResult<AssigneeVO>().ok(workTaskService.auditTask(dto));
    }

    @PostMapping(value = "/start")
    @Operation(summary = "流程任务>任务开始",description = "流程任务>任务开始")
    public HttpResult<AssigneeVO> start(@RequestBody ProcessDTO dto) {
        return new HttpResult<AssigneeVO>().ok(workTaskService.startTask(dto));
    }

    @GetMapping(value = "/diagram")
    @Operation(summary = "流程任务>任务流程",description = "流程任务>任务流程")
    public void diagram(@RequestParam("processInstanceId")String processInstanceId, HttpServletResponse response) throws IOException {
        workTaskService.diagramTask(processInstanceId, response);
    }

}