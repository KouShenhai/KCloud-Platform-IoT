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

package org.laokou.admin.noticeLog.dto.clientobject;

import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.Instant;

/**
 * 通知日志客户端对象.
 *
 * @author laokou
 */
@Data
public class NoticeLogCO extends ClientObject {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 通知编码.
	 */
	private String code;

	/**
	 * 通知名称.
	 */
	private String name;

	/**
	 * 通知状态 0成功 1失败.
	 */
	private Integer status;

	/**
	 * 错误信息.
	 */
	private String errorMessage;

	/**
	 * 通知参数.
	 */
	private String param;

	/**
	 * 创建时间.
	 */
	private Instant createTime;

}
