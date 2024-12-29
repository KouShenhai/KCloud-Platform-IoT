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

package org.laokou.auth.dto.clientobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.Instant;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeLogCO extends ClientObject {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 编码.
	 */
	private String code;
	/**
	 * 名称.
	 */
	private String name;
	/**
	 * 状态 0成功 1失败.
	 */
	private int status;
	/**
	 * 错误信息.
	 */
	private String errorMessage;
	/**
	 * 参数.
	 */
	private String param;

	/**
	 * 时间.
	 */
	private Instant instant;

	/**
	 * 租户ID.
	 */
	private Long tenantId;

	/**
	 * 唯一标识.
	 */
	private String uuid;

	/**
	 * 验证码.
	 */
	private String captcha;

}
