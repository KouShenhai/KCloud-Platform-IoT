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

package org.laokou.common.crypto.constant;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 加密类型枚举
 * @author laokou
 */
public enum Algorithm {

	@Schema(name = "AES", description = "AES加密算法")
	AES,

	@Schema(name = "MD5", description = "MD5摘要算法")
	MD5

}
