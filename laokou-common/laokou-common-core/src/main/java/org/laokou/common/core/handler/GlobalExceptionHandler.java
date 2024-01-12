/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
package org.laokou.common.core.handler;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.*;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author laokou
 */
@Slf4j
@RestControllerAdvice
@ResponseBody
@Component
public class GlobalExceptionHandler {

	@ExceptionHandler({ FeignException.class, SystemException.class, ApiException.class, FlowException.class,
			DataSourceException.class, GlobalException.class })
	public Result<?> handle(GlobalException ex) {
		// log.error("错误码：{}，错误信息：{}", ex.getCode(), ex.getMsg());
		return Result.fail(ex.getCode(), ex.getMsg());
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class, ValidationException.class })
	public Result<?> handle(Exception ex) {
		if (ex instanceof MethodArgumentNotValidException mane) {
			FieldError fieldError = mane.getFieldError();
			if (ObjectUtil.isNotNull(fieldError)) {
				return Result.fail(fieldError.getDefaultMessage());
			}
		}
		return Result.fail("验证失败");
	}

}
