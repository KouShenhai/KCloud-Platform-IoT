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

package org.laokou.auth.factory;

import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.CaptchaE;
import org.laokou.auth.model.LoginLogE;
import org.laokou.auth.model.NoticeLogE;
import org.laokou.auth.model.UserE;
import org.laokou.common.core.util.SpringContextUtils;

/**
 * @author laokou
 */
public final class DomainFactory {

	private DomainFactory() {
	}

	public static AuthA getAuth() {
		return SpringContextUtils.getBeanProvider(AuthA.class);
	}

	public static UserE getUser() {
		return SpringContextUtils.getBeanProvider(UserE.class);
	}

	public static LoginLogE getLoginLog() {
		return SpringContextUtils.getBeanProvider(LoginLogE.class);
	}

	public static CaptchaE getCaptcha() {
		return SpringContextUtils.getBeanProvider(CaptchaE.class);
	}

	public static NoticeLogE getNoticeLog() {
		return SpringContextUtils.getBeanProvider(NoticeLogE.class);
	}

}
