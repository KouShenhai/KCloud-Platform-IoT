package io.laokou.admin.interfaces.controller;
import io.laokou.admin.application.service.WorkflowTaskApplicationService;
import io.laokou.admin.interfaces.dto.AuditDTO;
import io.laokou.admin.interfaces.dto.ClaimDTO;
import io.laokou.admin.interfaces.dto.UnClaimDTO;
import io.laokou.common.utils.HttpResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @author Kou Shenhai
 */
@RestController
@Api(value = "流程任务API",protocols = "http",tags = "流程任务API")
@RequestMapping("/workflow/task/api")
public class WorkflowTaskApiController {

    @Autowired
    private WorkflowTaskApplicationService workflowTaskApplicationService;

    @PostMapping(value = "/audit")
    @ApiOperation(value = "流程任务>审批")
    public HttpResultUtil<Boolean> audit(@RequestBody AuditDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(workflowTaskApplicationService.auditTask(dto,request));
    }

    @PostMapping(value = "/claim")
    @ApiOperation(value = "流程任务>签收")
    public HttpResultUtil<Boolean> claim(@RequestBody ClaimDTO dto,HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(workflowTaskApplicationService.claimTask(dto, request));
    }

    @PostMapping(value = "/unClaim")
    @ApiOperation(value = "流程任务>取消签收")
    public HttpResultUtil<Boolean> unClaim(@RequestBody UnClaimDTO dto) {
        return new HttpResultUtil<Boolean>().ok(workflowTaskApplicationService.unClaimTask(dto));
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "流程任务>删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("taskId")String taskId) {
        return new HttpResultUtil<Boolean>().ok(workflowTaskApplicationService.deleteTask(taskId));
    }

    @GetMapping(value = "/diagram")
    @ApiOperation(value = "流程任务>流程图")
    public void diagram(@RequestParam("processInstanceId")String processInstanceId, HttpServletResponse response) throws IOException {
        workflowTaskApplicationService.diagramProcess(processInstanceId, response);
    }

}
