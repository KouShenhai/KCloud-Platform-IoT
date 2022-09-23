/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.common.exception;
import org.laokou.common.utils.HttpResultUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * custom异常处理器
 * @author Kou Shenhai
 * @since 1.0.0
 */
@RestControllerAdvice
@ResponseBody
@Component
public class CustomExceptionHandler {
	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler({CustomException.class})
	public HttpResultUtil<Boolean> handleRenException(CustomException ex){
		return new HttpResultUtil<Boolean>().error(ex.getCode(),ex.getMsg());
	}

}
