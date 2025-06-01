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

package org.laokou.auth.dto.domainevent;

import lombok.Getter;
import org.laokou.common.i18n.dto.DomainEvent;

/**
 * @author laokou
 */
@Getter
public class SendCaptchaEvent extends DomainEvent {

	private String uuid;

	// public LoginEvent(final Long id, final String username, final String ip, final
	// String address, final String browser,
	// final String os, final Integer status, final String errorMessage, final String
	// type,
	// final Instant loginTime, final Long tenantId, final Long userId) {
	// super.id = id;
	// super.userId = userId;
	// super.tenantId = tenantId;
	// this.username = username;
	// this.ip = ip;
	// this.address = address;
	// this.browser = browser;
	// this.os = os;
	// this.status = status;
	// this.errorMessage = errorMessage;
	// this.type = type;
	// this.loginTime = loginTime;
	// }

}
