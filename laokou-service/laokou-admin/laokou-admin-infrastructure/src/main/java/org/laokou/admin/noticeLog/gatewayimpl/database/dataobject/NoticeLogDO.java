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

package org.laokou.admin.noticeLog.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

/**
 * 通知日志数据对象.
 *
 * @author laokou
 */
@Data
@TableName("boot_sys_notice_log")
public class NoticeLogDO extends BaseDO {

	/**
	 * 标识.
	 */
	private String code;

	/**
	 * 名称.
	 */
	private String name;

	/**
	 * 状态 0成功 1失败.
	 */
	private Integer status;

	/**
	 * 错误信息.
	 */
	private String errorMessage;

	/**
	 * 参数.
	 */
	private String param;

}
