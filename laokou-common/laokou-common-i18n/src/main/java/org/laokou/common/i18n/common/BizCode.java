/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "BizCode", description = "业务码")
public interface BizCode {

	@Schema(name = "IP_BLACK", description = "IP已列入黑名单")
	int IP_BLACK = 100001;

	@Schema(name = "IP_WHITE", description = "IP被限制")
	int IP_WHITE = 100002;

	@Schema(name = "LOGIN_SUCCEEDED", description = "登录成功")
	int LOGIN_SUCCEEDED = 200001;

	@Schema(name = "ACCOUNT_FORCE_KILL", description = "账号已强制踢出")
	int ACCOUNT_FORCE_KILL = 300001;

}
