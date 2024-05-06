/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.gateway.exception;

import lombok.Getter;
import org.laokou.common.i18n.common.exception.AuthException;

/**
 * 拦截响应的异常枚举.
 *
 * @author laokou
 */
@Getter
public enum ExceptionEnum {

	/**
	 * 无效客户端.
	 */
	INVALID_CLIENT(AuthException.OAUTH2_INVALID_CLIENT),

	/**
	 * 无效请求.
	 */
	INVALID_REQUEST(AuthException.OAUTH2_INVALID_REQUEST);

	/**
	 * 编码.
	 */
	private final String code;

	ExceptionEnum(String code) {
		this.code = code;
	}

	public static ExceptionEnum getInstance(String code) {
		return ExceptionEnum.valueOf(code);
	}

}
