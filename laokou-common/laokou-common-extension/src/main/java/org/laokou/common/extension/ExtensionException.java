/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.extension;

import lombok.Getter;
import lombok.Setter;

/**
 * 扩展点初始化或者查找失败时，使用次异常.
 * <p>
 * 扩展点初始化或者查找失败时，使用次异常.
 * <p>
 *
 * @author ***flying@126.com
 * @since 1.0.0 2022/9/26
 * @version 1.0.0
 */
@Setter
@Getter
public class ExtensionException extends RuntimeException {

	private final String errCode;

	public ExtensionException(String errMessage, String errCode) {
		super(errMessage);
		this.errCode = errCode;
	}

	public ExtensionException(String errMessage, Throwable e, String errCode) {
		super(errMessage, e);
		this.errCode = errCode;
	}

	public ExtensionException(String errCode, String errMessage, Throwable e) {
		super(errMessage, e);
		this.errCode = errCode;
	}

}
