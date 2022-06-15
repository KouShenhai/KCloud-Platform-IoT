package io.laokou.admin.interfaces.controller;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.vo.LoginVO;
import io.laokou.common.constant.Constant;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.admin.interfaces.vo.UserInfoVO;
import io.laokou.admin.application.service.SysAuthApplicationService;
import io.laokou.admin.interfaces.dto.LoginDTO;
import io.laokou.admin.interfaces.dto.CodeAuthDTO;
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
    @CrossOrigin()
    @ApiOperation("系统认证>验证码")
    @ApiImplicitParam(name = "uuid",value = "唯一标识",required = true,paramType = "query",dataType = "String")
    public void captcha(@RequestParam(Constant.UUID)String uuid, HttpServletResponse response) throws IOException {
        sysAuthApplicationService.captcha(uuid,response);
    }

    @GetMapping("/weixin/callback")
    @CrossOrigin()
    @ApiOperation("系统认证>微信回调")
    public void callback(HttpServletRequest request,HttpServletResponse response) throws IOException {
        sysAuthApplicationService.wxLogin(request,response);
    }

    @PostMapping("/sys/auth/api/login")
    @ApiOperation("登录API")
    @CrossOrigin()
    public HttpResultUtil<LoginVO> login(@RequestBody LoginDTO loginDTO) throws Exception {
        return new HttpResultUtil<LoginVO>().ok(sysAuthApplicationService.login(loginDTO));
    }

    @PostMapping("/sys/auth/api/codeLogin")
    @ApiOperation("系统认证>登录(邮箱号/手机号)API")
    @CrossOrigin()
    public HttpResultUtil<LoginVO> codeLogin(@RequestBody CodeAuthDTO codeAuthDTO) {
        return new HttpResultUtil<LoginVO>().ok(sysAuthApplicationService.codeLogin(codeAuthDTO));
    }

    @GetMapping("/sys/auth/api/zfbLogin")
    @ApiOperation("系统认证>支付宝登录")
    @CrossOrigin()
    public void zfbLogin(HttpServletRequest request,HttpServletResponse response) throws IOException, AlipayApiException {
        sysAuthApplicationService.zfbLogin(request, response);
    }

    @RequestMapping(value = "/sys/auth/api/wxgzhSign",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation("系统认证>微信公众号签名")
    @CrossOrigin()
    public String wxgzhSign(HttpServletRequest request) throws IOException {
        return sysAuthApplicationService.wxgzhSign(request);
    }

    @GetMapping("/sys/auth/api/wxgzhLogin")
    @ApiOperation("系统认证>微信公众号登录")
    @CrossOrigin()
    public HttpResultUtil<LoginVO> wxgzhLogin(HttpServletRequest request) throws IOException {
        return new HttpResultUtil<LoginVO>().ok(sysAuthApplicationService.wxgzhLogin(request));
    }

    @GetMapping("/sys/auth/api/wxgzhTicket")
    @ApiOperation("系统认证>微信公众号凭证")
    @CrossOrigin()
    public HttpResultUtil<JSONObject> wxgzhTicket() throws IOException {
        return new HttpResultUtil<JSONObject>().ok(sysAuthApplicationService.getWxgzhTicket());
    }

     @GetMapping("/sys/auth/api/wxQRCode")
     @ApiOperation("系统认证>微信二维码")
     @CrossOrigin()
     public void wxQRCode(HttpServletResponse response) throws IOException {
         sysAuthApplicationService.getWxQRCode(response);
     }

    @GetMapping("/sys/auth/api/resource")
    @CrossOrigin()
    @ApiOperation("系统认证>资源权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.AUTHORIZATION_HEADER,value = "授权码",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = Constant.URI,value = "请求路径",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = Constant.METHOD,value = "请求方法",required = true,paramType = "query",dataType = "String")
    })
    public HttpResultUtil<UserDetail>  resource(@RequestParam(Constant.AUTHORIZATION_HEADER) String Authorization,
                                                @RequestParam(Constant.URI)String uri,
                                                @RequestParam(Constant.METHOD)String method) {
        return new HttpResultUtil<UserDetail>().ok(sysAuthApplicationService.resource(Authorization, uri, method));
    }

    @GetMapping("/sys/auth/api/logout")
    @ApiOperation("系统认证>退出登录")
    @CrossOrigin()
    public HttpResultUtil<Boolean> logout(HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysAuthApplicationService.logout(SecurityUser.getUserId(request)));
    }

    @GetMapping("/sys/auth/api/userInfo")
    @CrossOrigin
    @ApiOperation("系统认证>用户信息")
    public HttpResultUtil<UserInfoVO> userInfo(HttpServletRequest request) {
        return new HttpResultUtil<UserInfoVO>().ok(sysAuthApplicationService.userInfo(SecurityUser.getUserId(request)));
    }

}
