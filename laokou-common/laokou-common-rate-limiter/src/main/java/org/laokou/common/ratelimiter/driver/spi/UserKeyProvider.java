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

package org.laokou.common.ratelimiter.driver.spi;

import jakarta.servlet.http.HttpServletRequest;

import static org.laokou.common.i18n.common.constants.TraceConstant.USER_ID;
import static org.laokou.common.ratelimiter.driver.spi.TypeEnum.USER;

/**
 * @author laokou
 */
public class UserKeyProvider implements org.laokou.common.ratelimiter.driver.spi.KeyProvider {

	@Override
	public String resolve(HttpServletRequest request) {
		return request.getHeader(USER_ID);
	}

	@Override
	public TypeEnum accept() {
		return USER;
	}

}
