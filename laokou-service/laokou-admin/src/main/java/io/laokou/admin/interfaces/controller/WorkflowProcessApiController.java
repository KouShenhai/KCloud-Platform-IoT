package io.laokou.admin.interfaces.controller;
import io.laokou.admin.application.service.WorkflowProcessApplicationService;
import io.laokou.common.utils.HttpResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Api(value = "流程处理API",protocols = "http",tags = "流程处理API")
@RequestMapping("/workflow/process/api")
public class WorkflowProcessApiController {

    @Autowired
    private WorkflowProcessApplicationService workflowProcessApplicationService;

    @PostMapping("/start")
    @ApiOperation("流程处理>开始")
    public HttpResultUtil<Boolean> start(@RequestParam("definitionId")String definitionId) {
        return new HttpResultUtil<Boolean>().ok(workflowProcessApplicationService.startProcess(definitionId));
    }

}
