/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.auth.server.interfaces.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.client.constant.AuthConstant;
import org.laokou.auth.server.application.service.SysAuthApplicationService;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.i18n.core.HttpResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统认证控制器
 * @author laokou
 */
@RestController
@Tag(name = "Sys Auth API",description = "系统认证API")
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class SysAuthApiController {

    private final SysAuthApplicationService sysAuthApplicationService;

    /**
     * 配置服务限流
     * @param request
     * @return
     */
    @GetMapping("/captcha")
    @Operation(summary = "系统认证>账号密码登录>验证码",description = "系统认证>账号密码登录>验证码")
    @Parameter(name = AuthConstant.UUID,description = "唯一标识",example = "1111")
    public HttpResult<String> captcha(HttpServletRequest request) {
        return new HttpResult<String>().ok(sysAuthApplicationService.captcha(request));
    }

    @GetMapping("/logout")
    @Operation(summary = "系统认证>注销",description = "系统认证>注销")
    public HttpResult<Boolean> logout(HttpServletRequest request) {
        return new HttpResult<Boolean>().ok(sysAuthApplicationService.logout(request));
    }

    @GetMapping("/tenant")
    @Operation(summary = "系统认证>租户",description = "系统认证>租户")
    public HttpResult<List<OptionVO>> optionList() {
        return new HttpResult<List<OptionVO>>().ok(sysAuthApplicationService.getOptionList());
    }

}
