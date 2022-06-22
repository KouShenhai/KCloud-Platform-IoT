package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.UserDTO;
import io.laokou.admin.interfaces.qo.UserQO;
import io.laokou.admin.interfaces.vo.UserVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.admin.application.service.SysUserApplicationService;
import io.laokou.log.annotation.OperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
/**
 * 系统用户控制器
 * @author Kou Shenhai
 */
@RestController
@Api(value = "系统用户API",protocols = "http",tags = "系统用户API")
@RequestMapping("/sys/user/api")
public class SysUserApiController {

    @Autowired
    private SysUserApplicationService sysUserApplicationService;

    @PutMapping("/update")
    @ApiOperation("系统用户>修改")
    @CrossOrigin()
    @OperateLog(module = "系统用户",name = "修改用户")
    public HttpResultUtil<Boolean> update(@RequestBody UserDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysUserApplicationService.updateUser(dto,request));
    }

    @PutMapping("/updateInfo")
    @ApiOperation("系统用户>修改个人信息")
    @CrossOrigin()
    public HttpResultUtil<Boolean> updateInfo(@RequestBody UserDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysUserApplicationService.updateUser(dto,request));
    }

    @PutMapping("/password")
    @ApiOperation("系统用户>重置")
    @CrossOrigin()
    @OperateLog(module = "系统用户",name = "重置密码")
    public HttpResultUtil<Boolean> password(@RequestBody UserDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysUserApplicationService.updateUser(dto,request));
    }

    @PostMapping("/insert")
    @ApiOperation("系统用户>新增")
    @CrossOrigin()
    @OperateLog(module = "系统用户",name = "新增用户")
    public HttpResultUtil<Boolean> insert(@RequestBody UserDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysUserApplicationService.insertUser(dto,request));
    }

    @GetMapping("/detail")
    @ApiOperation("系统用户>详情")
    @CrossOrigin()
    public HttpResultUtil<UserVO> detail(@RequestParam("id") Long id) {
        return new HttpResultUtil<UserVO>().ok(sysUserApplicationService.getUserById(id));
    }

    @DeleteMapping("/delete")
    @ApiOperation("系统用户>删除")
    @CrossOrigin()
    @OperateLog(module = "系统用户",name = "删除用户")
    public HttpResultUtil<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResultUtil<Boolean>().ok(sysUserApplicationService.deleteUser(id));
    }

    @PostMapping("/query")
    @ApiOperation("系统用户>查询")
    @CrossOrigin()
    public HttpResultUtil<IPage<UserVO>> query(@RequestBody UserQO qo) {
        return new HttpResultUtil<IPage<UserVO>>().ok(sysUserApplicationService.queryUserPage(qo));
    }

}
