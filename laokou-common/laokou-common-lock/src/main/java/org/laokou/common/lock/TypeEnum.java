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

package org.laokou.common.lock;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TypeEnum", description = "类型枚举")
public enum TypeEnum {

	@Schema(name = "LOCK", description = "普通锁(默认)")
	LOCK,

	@Schema(name = "FAIR_LOCK", description = "公平锁")
	FAIR_LOCK,

	@Schema(name = "READ_LOCK", description = "读锁")
	READ_LOCK,

	@Schema(name = "WRITE_LOCK", description = "写锁")
	WRITE_LOCK,

	@Schema(name = "FENCED_LOCK", description = "强一致性锁(可以解决主从延迟)")
	FENCED_LOCK

}
