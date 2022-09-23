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
package org.laokou.gateway.feign.auth;
import org.laokou.common.constant.Constant;
import org.laokou.common.constant.ServiceConstant;
import org.laokou.common.user.UserDetail;
import org.laokou.common.utils.HttpResultUtil;
import org.laokou.gateway.feign.auth.factory.AuthApiFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @author Kou Shenhai
 */
@FeignClient(name = ServiceConstant.LAOKOU_AUTH, fallbackFactory = AuthApiFeignClientFallbackFactory.class)
@Service
public interface AuthApiFeignClient {

    /**
     * 根据token获取用户信息
     * @param Authorization
     * @param uri
     * @param method
     * @param language
     * @return
     */
    @GetMapping("/sys/auth/api/resource")
    HttpResultUtil<UserDetail> resource(
                             @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language,
                             @RequestParam(Constant.AUTHORIZATION_HEAD) String Authorization,
                             @RequestParam(Constant.URI)String uri,
                             @RequestParam(Constant.METHOD)String method);
}
