package io.laokou.admin.interfaces.controller;

import io.laokou.admin.application.service.SysLogApplicationService;
import io.laokou.common.dto.OperateLogDTO;
import io.laokou.common.utils.HttpResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统日志控制器
 * @author Kou Shenhai
 */
@RestController
@Api(value = "系统日志API",protocols = "http",tags = "系统日志API")
@RequestMapping("/sys/log/api")
public class SysLogApiController {

    @Autowired
    private SysLogApplicationService sysLogApplicationService;

    @PostMapping(value = "/operate/insert")
    @CrossOrigin()
    @ApiOperation("系统日志>操作日志>新增")
    public HttpResultUtil<Boolean> insertOperateLog(@RequestBody OperateLogDTO dto) {
        return new HttpResultUtil<Boolean>().ok(sysLogApplicationService.insertOperateLog(dto));
    }

}
