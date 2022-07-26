package io.laokou.admin.interfaces.controller;
import io.laokou.admin.application.service.SysDeptApplicationService;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.common.vo.SysDeptVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
/**
 * 系统部门
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:28
 */
@RestController
@Api(value = "系统部门API",protocols = "http",tags = "系统部门API")
@RequestMapping("/sys/dept/api")
public class SysDeptApiController {

    @Autowired
    private SysDeptApplicationService sysDeptApplicationService;

    @GetMapping("/list")
    @ApiOperation("系统部门>列表")
    public HttpResultUtil<SysDeptVO> list() {
        return new HttpResultUtil<SysDeptVO>().ok(sysDeptApplicationService.getDeptList());
    }

    @PostMapping("/query")
    @ApiOperation("系统部门>查询")
    public HttpResultUtil<List<SysDeptVO>> query() {
        return new HttpResultUtil<List<SysDeptVO>>().ok(sysDeptApplicationService.queryDeptList());
    }

}
