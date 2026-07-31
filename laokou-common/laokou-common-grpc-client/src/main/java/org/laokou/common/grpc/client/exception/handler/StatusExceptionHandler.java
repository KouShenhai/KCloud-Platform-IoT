/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.grpc.client.exception.handler;

import io.grpc.StatusException;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.MessageUtils;
import org.laokou.common.i18n.util.ObjectUtils;

/**
 * @author laokou
 */
public final class StatusExceptionHandler {

	private static final String UNAUTHENTICATED = "UNAUTHENTICATED: Authentication failed";

	private static final String FORBIDDEN_MESSAGE = MessageUtils.getMessage(StatusCode.FORBIDDEN);

	private StatusExceptionHandler() {
	}

	public static BizException handle(StatusException ex1, BizException ex2, String serviceId) {
		String message = ex1.getMessage();
		if (ObjectUtils.equals(UNAUTHENTICATED, message)) {
			return new BizException(StatusCode.FORBIDDEN, String.format("【%s】" + FORBIDDEN_MESSAGE, serviceId));
		}
		return ex2;
	}

}
