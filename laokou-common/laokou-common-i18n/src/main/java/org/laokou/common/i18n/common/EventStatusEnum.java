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

package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "EventStatusEnums", description = "事件状态枚举")
public enum EventStatusEnum {

	@Schema(name = "CREATED", description = "创建")
	CREATED,

	@Schema(name = "PUBLISH_SUCCEED", description = "发布成功")
	PUBLISH_SUCCEED,

	@Schema(name = "PUBLISH_FAILED", description = "发布失败")
	PUBLISH_FAILED,

	@Schema(name = "CONSUME_SUCCEED", description = "消费成功")
	CONSUME_SUCCEED,

	@Schema(name = "CONSUME_FAILED", description = "消费失败")
	CONSUME_FAILED

}
