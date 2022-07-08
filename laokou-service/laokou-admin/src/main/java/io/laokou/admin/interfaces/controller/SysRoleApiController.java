package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.SysRoleApplicationService;
import io.laokou.admin.interfaces.dto.RoleDTO;
import io.laokou.admin.interfaces.qo.RoleQO;
import io.laokou.admin.interfaces.vo.RoleVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.annotation.OperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统角色控制器
 * @author Kou Shenhai
 */
@RestController
@Api(value = "系统角色API",protocols = "http",tags = "系统角色API")
@RequestMapping("/sys/role/api")
public class SysRoleApiController {

    @Autowired
    private SysRoleApplicationService sysRoleApplicationService;

    @PostMapping("/query")
    @ApiOperation("系统角色>查询")
    public HttpResultUtil<IPage<RoleVO>> query(@RequestBody RoleQO qo) {
        return new HttpResultUtil<IPage<RoleVO>>().ok(sysRoleApplicationService.queryRolePage(qo));
    }

    @PostMapping("/list")
    @ApiOperation("系统角色>列表")
    public HttpResultUtil<List<RoleVO>> list(@RequestBody RoleQO qo) {
        return new HttpResultUtil<List<RoleVO>>().ok(sysRoleApplicationService.getRoleList(qo));
    }

    @GetMapping("/detail")
    @ApiOperation("系统角色>详情")
    public HttpResultUtil<RoleVO> detail(@RequestParam("id") Long id) {
        return new HttpResultUtil<RoleVO>().ok(sysRoleApplicationService.getRoleById(id));
    }

    @PostMapping("/insert")
    @ApiOperation("系统角色>新增")
    @OperateLog(module = "系统角色",name = "角色新增")
    public HttpResultUtil<Boolean> insert(@RequestBody RoleDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysRoleApplicationService.insertRole(dto, request));
    }

    @PutMapping("/update")
    @ApiOperation("系统角色>修改")
    @OperateLog(module = "系统角色",name = "角色修改")
    public HttpResultUtil<Boolean> update(@RequestBody RoleDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysRoleApplicationService.updateRole(dto, request));
    }

    @DeleteMapping("/delete")
    @ApiOperation("系统角色>删除")
    @OperateLog(module = "系统角色",name = "角色删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("id") Long id,HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysRoleApplicationService.deleteRole(id));
    }

}
