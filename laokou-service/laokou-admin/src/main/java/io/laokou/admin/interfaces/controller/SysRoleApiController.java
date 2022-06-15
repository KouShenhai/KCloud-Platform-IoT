package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.SysRoleApplicationService;
import io.laokou.admin.interfaces.dto.RoleDTO;
import io.laokou.admin.interfaces.qo.RoleQO;
import io.laokou.admin.interfaces.vo.RoleVO;
import io.laokou.common.utils.HttpResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统角色控制器
 * @author Kou Shenhai
 */
@RestController
@Api(value = "系统角色API",protocols = "http",tags = "系统角色API")
@RequestMapping("/sys/role/api")
@Slf4j
public class SysRoleApiController {

    @Autowired
    private SysRoleApplicationService sysRoleApplicationService;

    @PostMapping("/page")
    @ApiOperation("系统角色>分页")
    @CrossOrigin()
    public HttpResultUtil<IPage<RoleVO>> page(@RequestBody RoleQO qo) {
        return new HttpResultUtil<IPage<RoleVO>>().ok(sysRoleApplicationService.getRolePage(qo));
    }


    @GetMapping("/detail")
    @ApiOperation("系统角色>详情")
    @CrossOrigin()
    public HttpResultUtil<RoleVO> detail(@RequestParam("id") Long id) {
        return new HttpResultUtil<RoleVO>().ok(sysRoleApplicationService.getRoleById(id));
    }

    @PostMapping("/insert")
    @ApiOperation("系统角色>新增")
    @CrossOrigin()
    public HttpResultUtil<Boolean> insert(@RequestBody RoleDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysRoleApplicationService.insertRole(dto, request));
    }

    @PutMapping("/update")
    @ApiOperation("系统角色>修改")
    @CrossOrigin()
    public HttpResultUtil<Boolean> update(@RequestBody RoleDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysRoleApplicationService.updateRole(dto, request));
    }

    @DeleteMapping("/delete")
    @ApiOperation("系统角色>删除")
    @CrossOrigin()
    public HttpResultUtil<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResultUtil<Boolean>().ok(sysRoleApplicationService.deleteRole(id));
    }

}
