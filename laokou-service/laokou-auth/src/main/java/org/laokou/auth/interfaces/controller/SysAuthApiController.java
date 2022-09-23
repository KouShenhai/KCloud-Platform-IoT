/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.auth.interfaces.controller;
import org.laokou.auth.application.service.SysAuthApplicationService;
import org.laokou.auth.interfaces.dto.LoginDTO;
import org.laokou.common.user.BaseUserVO;
import org.laokou.auth.interfaces.vo.LoginVO;
import org.laokou.auth.interfaces.vo.UserInfoVO;
import org.laokou.common.constant.Constant;
import org.laokou.common.user.SecurityUser;
import org.laokou.common.user.UserDetail;
import org.laokou.common.utils.HttpResultUtil;
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
    @ApiOperation("系统认证>登录")
    public HttpResultUtil<LoginVO> login(@RequestBody LoginDTO loginDTO) throws Exception {
        return new HttpResultUtil<LoginVO>().ok(sysAuthApplicationService.login(loginDTO));
    }

    @GetMapping("/sys/auth/api/zfbBind")
    @ApiOperation("系统认证>支付宝绑定")
    public void zfbBind(HttpServletRequest request,HttpServletResponse response) throws Exception {
        sysAuthApplicationService.zfbBind(request, response);
    }

    @GetMapping("/sys/auth/api/zfbLogin")
    @ApiOperation("系统认证>支付宝登录")
    public void zfbLogin(HttpServletRequest request,HttpServletResponse response) throws Exception {
        sysAuthApplicationService.zfbLogin(request, response);
    }

    @GetMapping("/sys/auth/api/resource")
    @ApiOperation("系统认证>资源权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.AUTHORIZATION_HEAD,value = "授权码",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = Constant.URI,value = "请求路径",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = Constant.METHOD,value = "请求方法",required = true,paramType = "query",dataType = "String")
    })
    //@HystrixCommand(fallbackMethod = "fallback",ignoreExceptions = {CustomException.class})
    public HttpResultUtil<UserDetail>  resource(@RequestParam(Constant.AUTHORIZATION_HEAD) String Authorization,
                                                @RequestParam(Constant.URI)String uri,
                                                @RequestParam(Constant.METHOD)String method) {
        return new HttpResultUtil<UserDetail>().ok(sysAuthApplicationService.resource(Authorization, uri, method));
    }
    public HttpResultUtil<UserDetail> fallback(String Authorization, String uri, String method) {return new HttpResultUtil<UserDetail>().error("服务已被降级熔断");}

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

    @PostMapping("/sys/auth/api/open/login")
    @ApiOperation("系统认证>对外开放登录")
    public void openLogin(HttpServletResponse response, HttpServletRequest request) throws Exception {
        sysAuthApplicationService.openLogin(response, request);
    }

    @GetMapping("/sys/auth/api/open/userInfo")
    @ApiOperation("系统认证>对外开放用户信息")
    public HttpResultUtil<BaseUserVO> openUserInfo(HttpServletRequest request) {
        return new HttpResultUtil<BaseUserVO>().ok(sysAuthApplicationService.openUserInfo(SecurityUser.getUserId(request)));
    }

}
