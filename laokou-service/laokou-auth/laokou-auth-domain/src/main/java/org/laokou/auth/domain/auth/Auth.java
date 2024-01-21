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
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author laokou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Auth", description = "认证")
public class Auth implements Serializable {

	@Schema(name = "username", description = "用户名")
	private String username;

	@Schema(name = "type", description = "登录类型 mail邮箱 mobile手机号 password密码 authorization_code授权码")
	private String type;

	@Schema(name = "key", description = "密钥Key")
	private String key;

}
