/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.auth.domain.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.common.exception.AuthException;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.laokou.common.i18n.common.exception.AuthException.INVALID_SCOPE;

/**
 * @author laokou
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@Schema(name = "Auth", description = "认证")
public class SecretKey {

	@Schema(name = "type", description = "类型 mail邮箱 mobile手机号 password密码 authorization_code授权码")
	String type;

	@Schema(name = "secretKey", description = "密钥Key")
	String secretKey;

	public void checkScopes(List<String> scopes) {
		if (CollectionUtil.isNotEmpty(scopes) && scopes.size() != 1) {
			throw new AuthException(INVALID_SCOPE);
		}
	}

}
