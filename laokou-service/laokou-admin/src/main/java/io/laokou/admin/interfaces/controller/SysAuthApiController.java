package io.laokou.admin.interfaces.controller;
import com.alipay.api.AlipayApiException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.vo.LoginVO;
import io.laokou.common.constant.Constant;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.admin.interfaces.vo.UserInfoVO;
import io.laokou.admin.application.service.SysAuthApplicationService;
import io.laokou.admin.interfaces.dto.LoginDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 系统认证控制器
 * @author Kou Shenhai
 */
@RestController
@Api(value = "系统认证API",protocols = "http",tags = "系统认证API")
public class SysAuthApiController {

    @Autowired
    private SysAuthApplicationService sysAuthApplicationService;

    @GetMapping("/sys/auth/api/captcha")
    @ApiOperation("系统认证>验证码")
    @ApiImplicitParam(name = "uuid",value = "唯一标识",required = true,paramType = "query",dataType = "String")
    public void captcha(@RequestParam(Constant.UUID)String uuid, HttpServletResponse response) throws IOException {
        sysAuthApplicationService.captcha(uuid,response);
    }

    @PostMapping("/sys/auth/api/login")
    @ApiOperation("登录API")
    public HttpResultUtil<LoginVO> login(@RequestBody LoginDTO loginDTO) throws Exception {
        return new HttpResultUtil<LoginVO>().ok(sysAuthApplicationService.login(loginDTO));
    }

    @GetMapping("/sys/auth/api/zfbLogin")
    @ApiOperation("系统认证>支付宝登录")
    public void zfbLogin(HttpServletRequest request,HttpServletResponse response) throws IOException, AlipayApiException {
        sysAuthApplicationService.zfbLogin(request, response);
    }

    @GetMapping("/sys/auth/api/resource")
    @ApiOperation("系统认证>资源权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.AUTHORIZATION_HEADER,value = "授权码",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = Constant.URI,value = "请求路径",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = Constant.METHOD,value = "请求方法",required = true,paramType = "query",dataType = "String")
    })
    @HystrixCommand(fallbackMethod = "fallback")
    public HttpResultUtil<UserDetail>  resource(@RequestParam(Constant.AUTHORIZATION_HEADER) String Authorization,
                                                @RequestParam(Constant.URI)String uri,
                                                @RequestParam(Constant.METHOD)String method) {
        return new HttpResultUtil<UserDetail>().ok(sysAuthApplicationService.resource(Authorization, uri, method));
    }

    @GetMapping("/sys/auth/api/logout")
    @ApiOperation("系统认证>退出登录")
    public HttpResultUtil<Boolean> logout(HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysAuthApplicationService.logout(SecurityUser.getUserId(request)));
    }

    @GetMapping("/sys/auth/api/userInfo")
    @ApiOperation("系统认证>用户信息")
    public HttpResultUtil<UserInfoVO> userInfo(HttpServletRequest request) {
        return new HttpResultUtil<UserInfoVO>().ok(sysAuthApplicationService.userInfo(SecurityUser.getUserId(request)));
    }

    public String fallback() {
        return "服务已被降级熔断";
    }

}
