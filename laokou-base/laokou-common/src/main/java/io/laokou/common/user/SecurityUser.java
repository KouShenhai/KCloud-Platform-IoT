/**
 * Copyright 2020-2022 Kou Shenhai
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
package io.laokou.common.user;
import io.laokou.common.constant.Constant;
import io.laokou.common.exception.CustomException;
import io.laokou.common.exception.ErrorCode;
import io.laokou.common.utils.TokenUtil;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;
public class SecurityUser {

    public static Long getUserId(HttpServletRequest request) {
        String userId = request.getHeader(Constant.USER_KEY_HEAD);
        if (StringUtils.isBlank(userId)) {
            String authHeader = getAuthorization(request);
            if (StringUtils.isBlank(authHeader)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }
            if (TokenUtil.isExpiration(authHeader)) {
                throw new CustomException(ErrorCode.AUTHORIZATION_INVALID);
            }
            return TokenUtil.getUserId(authHeader);
        }
        return Long.valueOf(userId);
    }

    public static String getUsername(HttpServletRequest request) {
        String username = request.getHeader(Constant.USERNAME_HEAD);
        if (StringUtils.isBlank(username)) {
            String authHeader = getAuthorization(request);
            if (StringUtils.isBlank(authHeader)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }
            if (TokenUtil.isExpiration(authHeader)) {
                throw new CustomException(ErrorCode.AUTHORIZATION_INVALID);
            }
            return TokenUtil.getUsername(authHeader);
        }
        return username;
    }

    /**
     * 获取请求的token
     */
    public static String getAuthorization(HttpServletRequest request){
        //从header中获取token
        String Authorization = request.getHeader(Constant.AUTHORIZATION_HEAD);
        //如果header中不存在Authorization，则从参数中获取Authorization
        if(org.apache.commons.lang3.StringUtils.isBlank(Authorization)){
            Authorization = request.getParameter(Constant.AUTHORIZATION_HEAD);
        }
        return Authorization;
    }

}
