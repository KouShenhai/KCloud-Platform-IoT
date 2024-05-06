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

package org.laokou.auth.domain.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.auth.domain.model.auth.AuthA;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.constants.EventType;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.DateUtil;

import java.io.Serial;

import static org.laokou.common.i18n.common.constants.EventStatus.CREATED;
import static org.laokou.common.i18n.common.RocketMqConstant.LAOKOU_LOGIN_EVENT_TOPIC;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
@Schema(name = "LoginEvent", description = "登录事件")
public class LoginEvent extends DomainEvent<Long> {

	@Serial
	private static final long serialVersionUID = -325094951800650353L;

	@Schema(name = "username", description = "登录的用户名")
	protected String username;

	@Schema(name = "ip", description = "登录的IP地址")
	protected String ip;

	@Schema(name = "address", description = "登录的归属地")
	protected String address;

	@Schema(name = "browser", description = "登录的浏览器")
	protected String browser;

	@Schema(name = "os", description = "登录的操作系统")
	protected String os;

	@Schema(name = "status", description = "登录状态 0登录成功 1登录失败")
	protected Integer status;

	@Schema(name = "message", description = "登录信息")
	protected String message;

	@Schema(name = "type", description = "登录类型")
	protected String type;

	public LoginEvent(AuthA authA, EventType eventType, String message, Integer status) {
		super(IdGenerator.defaultSnowflakeId(), authA.getId(), eventType, CREATED, LAOKOU_LOGIN_EVENT_TOPIC,
				authA.getSourceName(), authA.getAppName(), authA.getCreator(), authA.getEditor(), authA.getDeptId(),
				authA.getDeptPath(), authA.getTenantId(), DateUtil.now(), DateUtil.now());
		this.username = authA.getUsername();
		this.ip = authA.getLog().loginIp();
		this.address = authA.getLog().address();
		this.os = authA.getLog().os();
		this.browser = authA.getLog().browser();
		this.message = message;
		this.type = authA.getGrantType();
		this.status = status;
	}

}
