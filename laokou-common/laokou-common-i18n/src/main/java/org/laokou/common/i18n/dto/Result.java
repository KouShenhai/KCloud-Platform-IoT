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

package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.io.Serial;
import java.io.Serializable;

import static org.laokou.common.i18n.common.StatusCode.OK;

/**
 * @author laokou
 */
@Data
@Schema(name = "Result", description = "请求响应统一格式")
public class Result<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = -1286769110881865369L;

	@Schema(name = "code", description = "状态编码", example = "OK")
	private String code;

	@Schema(name = "msg", description = "响应描述", example = "请求成功")
	private String msg;

	@Schema(name = "data", description = "响应结果")
	private T data;

	@Schema(name = "traceId", description = "链路ID")
	private String traceId;

	public boolean success() {
		return ObjectUtil.equals(this.code, OK);
	}

	public boolean error() {
		return !success();
	}

	public static <T> Result<T> ok(T data) {
		Result<T> result = new Result<>();
		result.setData(data);
		result.setCode(OK);
		result.setMsg(MessageUtil.getMessage(OK));
		return result;
	}

	public static <T> Result<T> fail(String code) {
		Result<T> result = new Result<>();
		result.setCode(code);
		result.setMsg(MessageUtil.getMessage(code));
		return result;
	}

	public static <T> Result<T> fail(String code, String msg) {
		Result<T> result = new Result<>();
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}

}
