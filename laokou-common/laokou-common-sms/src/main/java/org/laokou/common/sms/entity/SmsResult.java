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

package org.laokou.common.sms.entity;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author laokou
 */
@Getter
public class SmsResult implements Serializable {

	/**
	 * 编码.
	 */
	private final String code = "sendMobileCaptcha";

	/**
	 * 名称.
	 */
	private final String name;

	/**
	 * 状态 0成功 1失败.
	 */
	private final int status;

	/**
	 * 错误信息.
	 */
	private final String errorMessage;

	/**
	 * 参数.
	 */
	private final String param;

	/**
	 * 验证码.
	 */
	private final String captcha;

	public SmsResult(String name, int status, String errorMessage, String param, String captcha) {
		this.name = name;
		this.status = status;
		this.errorMessage = errorMessage;
		this.param = param;
		this.captcha = captcha;
	}

}
