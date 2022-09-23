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
package org.laokou.oauth2.exception;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;
/**
 * 登录或者鉴权失败时的返回信息
 *
 * @author Kou Shenhai
 */
@Service
public class RenWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {
    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        Exception exception = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);
        if (exception != null) {
            return handleOAuth2Exception(new RenOAuth2Exception(e.getMessage(), e));
        }
        exception = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        if (exception != null) {
            return handleOAuth2Exception(new RenOAuth2Exception(exception.getMessage(), exception));
        }
        exception = (InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class, causeChain);
        if (exception != null) {
            return handleOAuth2Exception(new RenOAuth2Exception(exception.getMessage(), exception));
        }
        exception = (HttpRequestMethodNotSupportedException) throwableAnalyzer.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
        if (exception != null) {
            return handleOAuth2Exception(new RenOAuth2Exception(exception.getMessage(), exception));
        }
        exception = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
        if (exception != null) {
            return handleOAuth2Exception((OAuth2Exception) exception);
        }
        return handleOAuth2Exception(new RenOAuth2Exception(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));
    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CACHE_CONTROL, "no-store");
        headers.set(HttpHeaders.PRAGMA, "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set(HttpHeaders.WWW_AUTHENTICATE, String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }
        if (e instanceof ClientAuthenticationException) {
            return new ResponseEntity<>(e, headers, HttpStatus.valueOf(status));
        }
        return new ResponseEntity(new RenHttpResult(e.getOAuth2ErrorCode(),e.getMessage()), headers, HttpStatus.OK);
    }
}
