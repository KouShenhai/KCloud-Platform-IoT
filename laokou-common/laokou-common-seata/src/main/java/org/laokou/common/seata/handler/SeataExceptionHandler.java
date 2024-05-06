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

package org.laokou.common.seata.handler;

import io.seata.common.exception.FrameworkException;
import io.seata.core.exception.RmTransactionException;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static org.laokou.common.i18n.common.exception.StatusCode.INTERNAL_SERVER_ERROR;
import static org.laokou.common.i18n.common.exception.SystemException.DISTRIBUTED_TRANSACTION_DOWNTIME;
import static org.laokou.common.i18n.common.exception.SystemException.DISTRIBUTED_TRANSACTION_TIMEOUT;

/**
 * @author laokou
 */
@RestControllerAdvice
@ResponseBody
@Component
public class SeataExceptionHandler {

	@ExceptionHandler({ RmTransactionException.class, FrameworkException.class })
	public Result<?> handle(Exception ex) {
		if (ex instanceof RmTransactionException) {
			return Result.fail(DISTRIBUTED_TRANSACTION_TIMEOUT);
		}
		else if (ex instanceof FrameworkException) {
			return Result.fail(DISTRIBUTED_TRANSACTION_DOWNTIME);
		}
		return Result.fail(INTERNAL_SERVER_ERROR);
	}

}
