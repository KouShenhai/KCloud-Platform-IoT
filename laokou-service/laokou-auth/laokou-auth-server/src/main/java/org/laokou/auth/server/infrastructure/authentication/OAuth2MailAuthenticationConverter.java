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

package org.laokou.auth.server.infrastructure.authentication;

import org.springframework.security.core.Authentication;

import java.util.Map;

/**
 * @author laokou
 */
public class OAuth2MailAuthenticationConverter extends AbstractOAuth2BaseAuthenticationConverter {
    @Override
    String getGrantType() {
        return OAuth2MailAuthenticationProvider.GRANT_TYPE;
    }

    @Override
    Authentication convert(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        return new OAuth2MailAuthenticationToken(clientPrincipal,additionalParameters);
    }
}
