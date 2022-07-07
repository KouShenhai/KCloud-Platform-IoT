package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.WorkflowProcessApplicationService;
import io.laokou.admin.interfaces.qo.TaskQO;
import io.laokou.admin.interfaces.vo.TaskVO;
import io.laokou.common.utils.HttpResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/task")
    @ApiOperation("流程处理>任务")
    public HttpResultUtil<IPage<TaskVO>> task(@RequestBody TaskQO qo, HttpServletRequest request) {
        return new HttpResultUtil<IPage<TaskVO>>().ok(workflowProcessApplicationService.taskPage(qo,request));
    }

}
