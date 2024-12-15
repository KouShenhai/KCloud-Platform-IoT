/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.model;

import com.blueconic.browscap.Capabilities;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.laokou.common.core.utils.AddressUtil;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Identifier;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.time.Instant;

/**
 * @author laokou
 */
@Getter
public class LogE extends Identifier {

	private final String username;

	private final String type;

	private final Instant instant;

	private final String os;

	private final String ip;

	private final String address;

	private final String browser;

	private Integer status;

	private String errorMessage;

	public LogE(String username, String type, Instant instant, GlobalException ex, HttpServletRequest request) {
		Capabilities capabilities = RequestUtil.getCapabilities(request);
		handleException(ex);
		this.username = username;
		this.type = type;
		this.instant = instant;
		this.ip = IpUtil.getIpAddr(request);
		this.address = AddressUtil.getRealAddress(ip);
		this.os = capabilities.getPlatform();
		this.browser = capabilities.getBrowser();
	}

	private void handleException(GlobalException ex) {
		if (ObjectUtil.isNull(ex)) {
			this.status = LoginStatus.OK.ordinal();
		}
		else if (ex instanceof SystemException e) {
			this.status = LoginStatus.FAIL.ordinal();
			this.errorMessage = e.getMessage();
		}
	}

}
