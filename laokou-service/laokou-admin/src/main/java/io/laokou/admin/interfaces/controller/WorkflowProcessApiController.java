package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.WorkflowProcessApplicationService;
import io.laokou.admin.interfaces.qo.TodoQO;
import io.laokou.admin.interfaces.vo.TodoVO;
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

    @PostMapping("/todo")
    @ApiOperation("流程处理>待办")
    public HttpResultUtil<IPage<TodoVO>> todo(@RequestBody TodoQO qo, HttpServletRequest request) {
        return new HttpResultUtil<IPage<TodoVO>>().ok(workflowProcessApplicationService.todoTaskPage(qo,request));
    }

}
