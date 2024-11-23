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

package org.laokou.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 * 发送验证码命令请求.
 *
 * @author laokou
 */
@Data
@Valid
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaSendCmd extends CommonCommand {

	/**
	 * 业务用例.
	 */
	public static final String USE_CASE_CAPTCHA = "captcha";

	/**
	 * 验证码标签表达式.
	 */
	private static final String CAPTCHA_TAG = "(mail|mobile)Captcha";

	@NotBlank(message = "UUID不能为空")
	private String uuid;

	@NotBlank(message = "标签不能为空")
	@Pattern(regexp = CAPTCHA_TAG, message = "标签错误")
	private String tag;

	@NotBlank(message = "租户编号不能为空")
	private String tenantCode;

}
