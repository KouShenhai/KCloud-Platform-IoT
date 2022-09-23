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
package org.laokou.oauth2.controller;
import org.laokou.common.user.BaseUserVO;
import org.laokou.common.utils.HttpResultUtil;
import org.laokou.oauth2.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/4/16 0016 上午 11:19
 */
@RestController
@RequestMapping("/oauth2")
public class ResourceController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取用户名
     * @param principal
     * @return
     */
    @GetMapping("/userInfo")
    public HttpResultUtil<BaseUserVO> getUserInfo(Principal principal) {
        return new HttpResultUtil<BaseUserVO>().ok(sysUserService.getUserInfo(principal.getName()));
    }

}
