package io.laokou.admin.interfaces.controller;
import io.laokou.admin.application.service.WorkflowTaskApplicationService;
import io.laokou.admin.interfaces.dto.AuditDTO;
import io.laokou.common.utils.HttpResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

}
