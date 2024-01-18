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

package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "RequestSecretConstants", description = "请求密钥常量")
public final class RequestSecretConstants {

	private RequestSecretConstants() {
	}

	@Schema(name = "APP_KEY", description = "应用Key")
	public static final String APP_KEY = "laokou2023";

	@Schema(name = "APP_SECRET", description = "应用密钥")
	public static final String APP_SECRET = "vb05f6c45d67340zaz95v7fa6d49v99zx";

}
