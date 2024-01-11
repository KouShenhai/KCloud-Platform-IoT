/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import lombok.Getter;

/**
 * @author laokou
 */
public enum AuditEnums {

	/**
	 * 待审批.
	 */
	PENDING_APPROVAL(0, "待审批"),

	/**
	 * 审批中.
	 */
	IN_APPROVAL(1, "审批中"),

	/**
	 * 驳回审批.
	 */
	REJECT_APPROVAL(-1, "驳回审批"),

	/**
	 * 通过审批.
	 */
	APPROVED(2, "审批通过"),

	/**
	 * 同意.
	 */
	PASS(1, "同意"),

	/**
	 * 驳回.
	 */
	REFUSE(0, "驳回");

	@Getter
	private final int value;

	@Getter
	private final String desc;

	AuditEnums(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

}
