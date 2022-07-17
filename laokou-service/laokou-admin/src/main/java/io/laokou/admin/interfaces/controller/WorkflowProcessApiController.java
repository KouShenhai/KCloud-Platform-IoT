package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.WorkflowProcessApplicationService;
import io.laokou.admin.interfaces.qo.TaskQO;
import io.laokou.admin.interfaces.vo.TaskVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.annotation.OperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
@RestController
@AllArgsConstructor
@Api(value = "流程处理API",protocols = "http",tags = "流程处理API")
@RequestMapping("/workflow/process/api")
public class WorkflowProcessApiController {

    private final WorkflowProcessApplicationService workflowProcessApplicationService;

    @PostMapping("/start")
    @ApiOperation("流程处理>开始")
    @OperateLog(module = "流程处理",name = "流程发起")
    public HttpResultUtil<Boolean> start(@RequestParam("definitionId")String definitionId) {
        return new HttpResultUtil<Boolean>().ok(workflowProcessApplicationService.startProcess(definitionId));
    }

    @PostMapping("/query")
    @ApiOperation("流程处理>任务")
    public HttpResultUtil<IPage<TaskVO>> query(@RequestBody TaskQO qo, HttpServletRequest request) {
        return new HttpResultUtil<IPage<TaskVO>>().ok(workflowProcessApplicationService.queryTaskPage(qo,request));
    }

}
