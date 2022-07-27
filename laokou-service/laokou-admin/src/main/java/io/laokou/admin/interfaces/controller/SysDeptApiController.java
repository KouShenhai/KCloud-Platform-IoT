package io.laokou.admin.interfaces.controller;
import io.laokou.admin.application.service.SysDeptApplicationService;
import io.laokou.admin.interfaces.dto.SysDeptDTO;
import io.laokou.admin.interfaces.qo.SysDeptQO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.common.vo.SysDeptVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
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
    public HttpResultUtil<List<SysDeptVO>> query(@RequestBody SysDeptQO qo) {
        return new HttpResultUtil<List<SysDeptVO>>().ok(sysDeptApplicationService.queryDeptList(qo));
    }

    @PostMapping("/insert")
    @ApiOperation("系统部门>新增")
    public HttpResultUtil<Boolean> insert(@RequestBody SysDeptDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysDeptApplicationService.insertDept(dto,request));
    }

    @PutMapping("/update")
    @ApiOperation("系统部门>修改")
    public HttpResultUtil<Boolean> update(@RequestBody SysDeptDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysDeptApplicationService.updateDept(dto,request));
    }

    @GetMapping("/detail")
    @ApiOperation("系统部门>详情")
    public HttpResultUtil<SysDeptVO> detail(@RequestParam("id")Long id) {
        return new HttpResultUtil<SysDeptVO>().ok(sysDeptApplicationService.getDept(id));
    }

    @DeleteMapping("/delete")
    @ApiOperation("系统部门>删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("id")Long id) {
        return new HttpResultUtil<Boolean>().ok(sysDeptApplicationService.deleteDept(id));
    }

    @GetMapping("/get")
    @ApiOperation("系统部门>部门树ids")
    public HttpResultUtil<List<Long>> get(@RequestParam(value = "roleId")Long roleId) {
        return new HttpResultUtil<List<Long>>().ok(sysDeptApplicationService.getDeptIdsByRoleId(roleId));
    }

}
