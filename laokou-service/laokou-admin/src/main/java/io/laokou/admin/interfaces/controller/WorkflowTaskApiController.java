/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package io.laokou.admin.interfaces.controller;
import io.laokou.admin.application.service.WorkflowTaskApplicationService;
import io.laokou.admin.interfaces.dto.ClaimDTO;
import io.laokou.admin.interfaces.dto.UnClaimDTO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.annotation.OperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
/**
 * @author Kou Shenhai
 */
@RestController
@Api(value = "流程任务API",protocols = "http",tags = "流程任务API")
@RequestMapping("/workflow/task/api")
public class WorkflowTaskApiController {

    @Autowired
    private WorkflowTaskApplicationService workflowTaskApplicationService;

    @PostMapping(value = "/claim")
    @ApiOperation(value = "流程任务>签收")
    @OperateLog(module = "流程任务",name = "任务签收")
    public HttpResultUtil<Boolean> claim(@RequestBody ClaimDTO dto,HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(workflowTaskApplicationService.claimTask(dto, request));
    }

    @PostMapping(value = "/unClaim")
    @ApiOperation(value = "流程任务>取消签收")
    @OperateLog(module = "流程任务",name = "取消签收")
    public HttpResultUtil<Boolean> unClaim(@RequestBody UnClaimDTO dto) {
        return new HttpResultUtil<Boolean>().ok(workflowTaskApplicationService.unClaimTask(dto));
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "流程任务>删除")
    @OperateLog(module = "流程任务",name = "任务删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("taskId")String taskId) {
        return new HttpResultUtil<Boolean>().ok(workflowTaskApplicationService.deleteTask(taskId));
    }

}
