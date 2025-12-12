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

package org.laokou.common.core.exception.handler;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.common.exception.ParamException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常统一处理.
 *
 * @author laokou
 */
@Slf4j
@Component
@ResponseBody
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 系统异常.
	 * @param ex 系统异常
	 * @return 响应结果
	 */
	@ExceptionHandler(SystemException.class)
	public Result<?> handle(SystemException ex) {
		// log.error("系统异常，错误码：{}，错误信息：{}", ex.getCode(), ex.getMsg(), ex.getData(), ex);
		return Result.fail(ex.getCode(), ex.getMsg(), ex.getData());
	}

	/**
	 * 业务异常.
	 * @param ex 业务异常
	 * @return 响应结果
	 */
	@ExceptionHandler(BizException.class)
	public Result<?> handle(BizException ex) {
		// log.error("业务异常，错误码：{}，错误信息：{}", ex.getCode(), ex.getMsg(), ex.getData(), ex);
		return Result.fail(ex.getCode(), ex.getMsg(), ex.getData());
	}

	/**
	 * 参数异常.
	 * @param ex 参数异常
	 * @return 响应结果
	 */
	@ExceptionHandler(ParamException.class)
	public Result<?> handle(ParamException ex) {
		// log.error("参数异常，错误码：{}，错误信息：{}", ex.getCode(), ex.getMsg(), ex.getData(), ex);
		return Result.fail(ex.getCode(), ex.getMsg(), ex.getData());
	}

	/**
	 * 参数校验异常.
	 * @param ex 参数校验异常
	 * @return 响应结果
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class, ValidationException.class })
	public Result<?> handle(Exception ex) {
		// log.error("参数校验异常，错误信息：{}", ex.getMsg(), ex);
		if (ex instanceof MethodArgumentNotValidException mane) {
			FieldError fieldError = mane.getFieldError();
			if (ObjectUtils.isNotNull(fieldError)) {
				return Result.fail(fieldError.getCode(), fieldError.getDefaultMessage());
			}
		}
		return Result.fail("S_UnKnow_Error", ex.getMessage());
	}

}
