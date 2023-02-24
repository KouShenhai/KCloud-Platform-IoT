/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.auth.client.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.core.HttpResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.util.MimeTypeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public class CustomAuthExceptionHandler {

    public static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    public static void handleException(HttpServletResponse response, int code, String message) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(JacksonUtil.toJsonStr(new HttpResult().error(code,message)));
        writer.flush();
        writer.close();
    }

    public static void throwError(int errorCode,String description) {
        throwError("" + errorCode,description,"");
    }

    public static void throwError(String errorCode,String description) {
        throwError(errorCode,description,"");
    }

    public static void throwError(String errorCode,String description,String uri) {
        OAuth2Error error = new OAuth2Error(errorCode, description,uri);
        throw new OAuth2AuthenticationException(error);
    }

    public static OAuth2AuthenticationException getError(String errorCode,String description,String uri) {
        OAuth2Error error = new OAuth2Error(errorCode, description,uri);
        return new OAuth2AuthenticationException(error);
    }

}