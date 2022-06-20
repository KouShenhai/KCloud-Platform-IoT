package io.laokou.admin.interfaces.controller;
import io.laokou.admin.application.service.SysMenuApplicationService;
import io.laokou.admin.interfaces.dto.MenuDTO;
import io.laokou.admin.interfaces.qo.MenuQO;
import io.laokou.admin.interfaces.vo.MenuVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.annotation.OperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * 系统菜单控制器
 * @author Kou Shenhai
 */
@RestController
@Api(value = "系统菜单API",protocols = "http",tags = "系统菜单API")
@RequestMapping("/sys/menu/api")
@Slf4j
public class SysMenuApiController {

    @Autowired
    private SysMenuApplicationService sysMenuApplicationService;

    @GetMapping("/list")
    @ApiOperation("系统菜单>列表")
    @CrossOrigin()
    public HttpResultUtil<MenuVO> list(HttpServletRequest request) {
        return new HttpResultUtil<MenuVO>().ok(sysMenuApplicationService.getMenuList(request));
    }

    @PostMapping("/query")
    @ApiOperation("系统菜单>查询")
    @CrossOrigin()
    @OperateLog(module = "系统菜单",name = "查询")
    public HttpResultUtil<List<MenuVO>> query(@RequestBody MenuQO qo) {
        return new HttpResultUtil<List<MenuVO>>().ok(sysMenuApplicationService.queryMenuList(qo));
    }

    @GetMapping("/detail")
    @ApiOperation("系统菜单>详情")
    @CrossOrigin()
    public HttpResultUtil<MenuVO> detail(@RequestParam("id")Long id) {
        return new HttpResultUtil<MenuVO>().ok(sysMenuApplicationService.getMenuById(id));
    }

    @PutMapping("/update")
    @ApiOperation("系统菜单>修改")
    @CrossOrigin()
    public HttpResultUtil<Boolean> update(@RequestBody MenuDTO dto,HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysMenuApplicationService.updateMenu(dto,request));
    }

    @PostMapping("/insert")
    @ApiOperation("系统菜单>新增")
    @CrossOrigin()
    public HttpResultUtil<Boolean> insert(@RequestBody MenuDTO dto,HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysMenuApplicationService.insertMenu(dto,request));
    }

    @DeleteMapping("/delete")
    @ApiOperation("系统菜单>删除")
    @CrossOrigin()
    public HttpResultUtil<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResultUtil<Boolean>().ok(sysMenuApplicationService.deleteMenu(id));
    }

    @GetMapping("/tree")
    @ApiOperation("系统菜单>树菜单")
    @CrossOrigin()
    public HttpResultUtil<MenuVO> tree(@RequestParam(required = false,value = "roleId")Long roleId) {
        return new HttpResultUtil<MenuVO>().ok(sysMenuApplicationService.treeMenu(roleId));
    }

    @GetMapping("/get")
    @ApiOperation("系统菜单>菜单树ids")
    @CrossOrigin()
    public HttpResultUtil<List<Long>> get(@RequestParam(value = "roleId")Long roleId) {
        return new HttpResultUtil<List<Long>>().ok(sysMenuApplicationService.getMenuIdsByRoleId(roleId));
    }

}
