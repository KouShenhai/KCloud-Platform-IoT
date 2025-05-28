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

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * 登录事件.
 *
 * @author laokou
 */
@Getter
public class LoginEvent extends DomainEvent implements Serializable {

	@Serial
	private static final long serialVersionUID = -325094951800650353L;

	/**
	 * 登录的用户名.
	 */
	private final String username;

	/**
	 * 登录的IP地址.
	 */
	private final String ip;

	/**
	 * 登录的归属地.
	 */
	private final String address;

	/**
	 * 登录的浏览器.
	 */
	private final String browser;

	/**
	 * 登录的操作系统.
	 */
	private final String os;

	/**
	 * 登录状态 0登录成功 1登录失败.
	 */
	private final Integer status;

	/**
	 * 错误信息.
	 */
	private final String errorMessage;

	/**
	 * 登录类型.
	 */
	private final String type;

	/**
	 * 登录时间.
	 */
	private final Instant loginTime;

	public LoginEvent(final Long id, final String username, final String ip, final String address, final String browser,
			final String os, final Integer status, final String errorMessage, final String type,
			final Instant loginTime, final Long tenantId, final Long userId) {
		super.id = id;
		super.userId = userId;
		super.tenantId = tenantId;
		this.username = username;
		this.ip = ip;
		this.address = address;
		this.browser = browser;
		this.os = os;
		this.status = status;
		this.errorMessage = errorMessage;
		this.type = type;
		this.loginTime = loginTime;
	}

}
