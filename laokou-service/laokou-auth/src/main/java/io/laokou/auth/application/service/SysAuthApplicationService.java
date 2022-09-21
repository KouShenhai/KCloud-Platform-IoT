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
package io.laokou.auth.application.service;

import com.alipay.api.AlipayApiException;
import io.laokou.auth.interfaces.dto.LoginDTO;
import io.laokou.common.user.BaseUserVO;
import io.laokou.auth.interfaces.vo.LoginVO;
import io.laokou.auth.interfaces.vo.UserInfoVO;
import io.laokou.common.user.UserDetail;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * auth服务
 * @author Kou Shenhai
 */
public interface SysAuthApplicationService {

    /**
     * 登录
     * @param loginDTO
     * @return
     * @throws Exception
     */
    LoginVO login(LoginDTO loginDTO) throws Exception;

    /**
     * 退出
     * @param userId
     * @return
     */
    Boolean logout(Long userId);

    /**
     * 生成验证码
     * @param uuid
     * @param response
     * @throws IOException
     */
    void captcha(String uuid, HttpServletResponse response) throws IOException;

    /**
     * 访问资源权限
     * @param Authorization
     * @param uri
     * @param method
     * @return
     */
    UserDetail resource(String Authorization, String uri, String method);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    UserInfoVO userInfo(Long userId);

    /**
     * 获取对外开放用户信息
     * @param userId
     * @return
     */
    BaseUserVO openUserInfo(Long userId);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    UserDetail getUserDetail(Long userId);

    /**
     * 支付宝登录
     * @param request
     * @param response
     * @throws AlipayApiException
     * @throws IOException
     */
    void zfbLogin(HttpServletRequest request,HttpServletResponse response) throws Exception;

    void zfbBind(HttpServletRequest request,HttpServletResponse response) throws IOException;

    /**
     * 开放登录
     * @param response
     * @param request
     * @throws Exception
     */
    void openLogin(HttpServletResponse response, HttpServletRequest request) throws Exception;
}
