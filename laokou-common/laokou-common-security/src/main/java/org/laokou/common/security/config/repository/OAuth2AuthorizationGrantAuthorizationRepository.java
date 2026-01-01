/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
 *
 */

/*
 * Copyright 2020-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.security.config.repository;

import org.laokou.common.security.config.entity.OAuth2AuthorizationCodeGrantAuthorization;
import org.laokou.common.security.config.entity.OAuth2AuthorizationGrantAuthorization;
import org.laokou.common.security.config.entity.OAuth2DeviceCodeGrantAuthorization;
import org.laokou.common.security.config.entity.OidcAuthorizationCodeGrantAuthorization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author spring-authorization-server
 * @author laokou
 */
@Repository
public interface OAuth2AuthorizationGrantAuthorizationRepository
		extends CrudRepository<OAuth2AuthorizationGrantAuthorization, String> {

	<T extends OAuth2AuthorizationCodeGrantAuthorization> T findByState(String state);

	<T extends OAuth2AuthorizationCodeGrantAuthorization> T findByAuthorizationCode_TokenValue(
			String authorizationCode);

	<T extends OAuth2AuthorizationCodeGrantAuthorization> T findByStateOrAuthorizationCode_TokenValue(String state,
			String authorizationCode);

	<T extends OAuth2AuthorizationGrantAuthorization> T findByAccessToken_TokenValue(String accessToken);

	<T extends OAuth2AuthorizationGrantAuthorization> T findByRefreshToken_TokenValue(String refreshToken);

	<T extends OAuth2AuthorizationGrantAuthorization> T findByAccessToken_TokenValueOrRefreshToken_TokenValue(
			String accessToken, String refreshToken);

	<T extends OidcAuthorizationCodeGrantAuthorization> T findByIdToken_TokenValue(String idToken);

	<T extends OAuth2DeviceCodeGrantAuthorization> T findByDeviceState(String deviceState);

	<T extends OAuth2DeviceCodeGrantAuthorization> T findByDeviceCode_TokenValue(String deviceCode);

	<T extends OAuth2DeviceCodeGrantAuthorization> T findByUserCode_TokenValue(String userCode);

	<T extends OAuth2DeviceCodeGrantAuthorization> T findByDeviceStateOrDeviceCode_TokenValueOrUserCode_TokenValue(
			String deviceState, String deviceCode, String userCode);

}
