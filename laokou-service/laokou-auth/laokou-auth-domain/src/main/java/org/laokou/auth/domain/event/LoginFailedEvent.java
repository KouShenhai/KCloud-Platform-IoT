/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.utils.AddressUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.DateUtil;

import java.io.Serial;

import static org.laokou.common.i18n.common.EventStatusEnums.CREATED;
import static org.laokou.common.i18n.common.EventTypeEnums.LOGIN_FAILED;
import static org.laokou.common.i18n.common.LoginStatusEnums.FAIL;

/**
 * @author laokou
 */
@Getter
@Builder
@Schema(name = "LoginFailedEvent", description = "登录失败事件")
public class LoginFailedEvent extends DomainEvent<Long> {

	@Serial
	private static final long serialVersionUID = -325094951800650353L;

	@Schema(name = "username", description = "登录的用户名")
	private String username;

	@Schema(name = "ip", description = "登录的IP地址")
	private String ip;

	@Schema(name = "address", description = "登录的归属地")
	private String address;

	@Schema(name = "browser", description = "登录的浏览器")
	private String browser;

	@Schema(name = "os", description = "登录的操作系统")
	private String os;

	@Schema(name = "status", description = "登录状态 0登录成功 1登录失败")
	private Integer status = FAIL.ordinal();

	@Schema(name = "message", description = "登录信息")
	private String message;

	public LoginFailedEvent(User user, HttpServletRequest request, String message) {
		super(IdGenerator.defaultSnowflakeId(), user.getId(), LOGIN_FAILED, CREATED, "", user.getId(), user.getId(),
				user.getDeptId(), user.getDeptPath(), user.getTenantId(), DateUtil.now(), DateUtil.now());
		this.username = user.getUsername();
		this.ip = IpUtil.getIpAddr(request);
		this.address = AddressUtil.getRealAddress(this.ip);
		UserAgent userAgent = RequestUtil.getUserAgent(request);
		this.os = userAgent.getOperatingSystem().getName();
		this.browser = userAgent.getBrowser().getName();
		this.message = message;
	}

}
