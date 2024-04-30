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
import lombok.Getter;

/**
 * @author laokou
 */
@Getter
@Schema(name = "AuditEnums", description = "审批状态枚举类")
public enum AuditEnum {

	@Schema(name = "PENDING_APPROVAL", description = "待审批")
	PENDING_APPROVAL(0),

	@Schema(name = "IN_APPROVAL", description = "审批中")
	IN_APPROVAL(1),

	@Schema(name = "REJECT_APPROVAL", description = "驳回审批")
	REJECT_APPROVAL(-1),

	@Schema(name = "APPROVED", description = "审批通过")
	APPROVED(2),

	@Schema(name = "PASS", description = "同意")
	PASS(1),

	@Schema(name = "REFUSE", description = "驳回")
	REFUSE(0);

	private final int value;

	AuditEnum(int value) {
		this.value = value;
	}

}
