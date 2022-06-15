package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.UserDTO;
import io.laokou.admin.interfaces.qo.UserQO;
import io.laokou.admin.interfaces.vo.UserVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.admin.application.service.SysUserApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SysUserApiController {

    @Autowired
    private SysUserApplicationService sysUserApplicationService;

    @PutMapping("/update")
    @ApiOperation("系统用户>修改")
    @CrossOrigin()
    public HttpResultUtil<Boolean> update(@RequestBody UserDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysUserApplicationService.updateUser(dto,request));
    }

    @PostMapping("/page")
    @ApiOperation("系统用户>分页")
    @CrossOrigin()
    public HttpResultUtil<IPage<UserVO>> page(@RequestBody UserQO qo) {
        return new HttpResultUtil<IPage<UserVO>>().ok(sysUserApplicationService.getUserPage(qo));
    }

}
