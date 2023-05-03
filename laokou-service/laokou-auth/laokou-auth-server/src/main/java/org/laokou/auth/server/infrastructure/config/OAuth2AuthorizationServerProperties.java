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

package org.laokou.auth.server.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Set;

/**
 * {@link ConfigurationSettingNames}
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = OAuth2AuthorizationServerProperties.PREFIX)
public class OAuth2AuthorizationServerProperties {
    public static final String PREFIX = "spring.security.oauth2.authorization-server";

    private boolean enabled = true;
    private Token token;
    private Client client;
    private Registration registration;

    @Data
    public static class Token {

        /**
         * Set the time-to-live for a refresh token.
         */
        private Duration refreshTokenTimeToLive;

        /**
         * Set the time-to-live for an access token.
         */
        private Duration accessTokenTimeToLive;

        /**
         * Set the time-to-live for an authorization code.
         */
        private Duration authorizationCodeTimeToLive;
    }

    @Data
    public static class Client {
        /**
         * Set to {@code true} if authorization consent is required when the client requests access.
         * This applies to all interactive flows (e.g. {@code authorization_code} and {@code device_code}).
         */
        private boolean requireAuthorizationConsent;
    }

    /**
     * {@link RegisteredClient}
     */
    @Data
    public static class Registration {
        private String id;
        private String clientId;
        private String clientName;
        private String clientSecret;
        private Set<String> clientAuthenticationMethods;
        private Set<String> authorizationGrantTypes;
        private Set<String> scopes;
        private Set<String> redirectUris;
    }

}
