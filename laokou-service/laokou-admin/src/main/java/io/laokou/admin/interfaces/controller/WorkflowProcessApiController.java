package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.WorkflowProcessApplicationService;
import io.laokou.admin.interfaces.dto.AuditDTO;
import io.laokou.admin.interfaces.qo.TaskQO;
import io.laokou.admin.interfaces.vo.TaskVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.annotation.OperateLog;
import io.laokou.redis.annotation.Lock4j;
import io.laokou.security.annotation.PreAuthorize;
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

    @PostMapping("/resource/query")
    @ApiOperation("流程处理>资源查询")
    @PreAuthorize("workflow:process:resource:query")
    public HttpResultUtil<IPage<TaskVO>> queryResource(@RequestBody TaskQO qo, HttpServletRequest request) {
        return new HttpResultUtil<IPage<TaskVO>>().ok(workflowProcessApplicationService.queryResourceTaskPage(qo,request));
    }

    @PostMapping(value = "/resource/audit")
    @ApiOperation(value = "流程处理>资源审批")
    @OperateLog(module = "流程处理",name = "资源审批")
    @PreAuthorize("workflow:process:resource:audit")
    @Lock4j(expire=3000)
    public HttpResultUtil<Boolean> auditResource(@RequestBody AuditDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(workflowProcessApplicationService.auditResourceTask(dto,request));
    }

}
