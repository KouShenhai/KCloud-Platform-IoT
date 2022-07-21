package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.SysRoleApplicationService;
import io.laokou.admin.interfaces.dto.SysRoleDTO;
import io.laokou.admin.interfaces.qo.SysRoleQO;
import io.laokou.common.vo.SysRoleVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.annotation.OperateLog;
import io.laokou.security.annotation.PreAuthorize;
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
    @PreAuthorize("sys:role:query")
    public HttpResultUtil<IPage<SysRoleVO>> query(@RequestBody SysRoleQO qo) {
        return new HttpResultUtil<IPage<SysRoleVO>>().ok(sysRoleApplicationService.queryRolePage(qo));
    }

    @PostMapping("/list")
    @ApiOperation("系统角色>列表")
    public HttpResultUtil<List<SysRoleVO>> list(@RequestBody SysRoleQO qo) {
        return new HttpResultUtil<List<SysRoleVO>>().ok(sysRoleApplicationService.getRoleList(qo));
    }

    @GetMapping("/detail")
    @ApiOperation("系统角色>详情")
    public HttpResultUtil<SysRoleVO> detail(@RequestParam("id") Long id) {
        return new HttpResultUtil<SysRoleVO>().ok(sysRoleApplicationService.getRoleById(id));
    }

    @PostMapping("/insert")
    @ApiOperation("系统角色>新增")
    @PreAuthorize("sys:role:insert")
    @OperateLog(module = "系统角色",name = "角色新增")
    public HttpResultUtil<Boolean> insert(@RequestBody SysRoleDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysRoleApplicationService.insertRole(dto, request));
    }

    @PutMapping("/update")
    @PreAuthorize("sys:role:update")
    @ApiOperation("系统角色>修改")
    @OperateLog(module = "系统角色",name = "角色修改")
    public HttpResultUtil<Boolean> update(@RequestBody SysRoleDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysRoleApplicationService.updateRole(dto, request));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("sys:role:delete")
    @ApiOperation("系统角色>删除")
    @OperateLog(module = "系统角色",name = "角色删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("id") Long id,HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysRoleApplicationService.deleteRole(id));
    }

}
