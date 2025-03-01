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

package org.laokou.common.log.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

import java.time.Instant;

import static org.laokou.common.tenant.constant.Constant.Domain.OPERATE_LOG_TABLE;

/**
 * @author laokou
 */
@Data
@TableName(OPERATE_LOG_TABLE)
public class OperateLogDO extends BaseDO {

	/**
	 * 操作名称.
	 */
	private String name;

	/**
	 * 操作的模块名称.
	 */
	private String moduleName;

	/**
	 * 操作的URI.
	 */
	private String uri;

	/**
	 * 操作的请求类型.
	 */
	private String requestType;

	/**
	 * 操作的浏览器.
	 */
	private String userAgent;

	/**
	 * 操作的归属地.
	 */
	private String address;

	/**
	 * 操作人.
	 */
	private String operator;

	/**
	 * 服务ID.
	 */
	private String serviceId;

	/**
	 * 创建时间.
	 */
	private Instant createTime;

	/**
	 * 操作的方法名.
	 */
	private String methodName;

	/**
	 * 操作的请求参数.
	 */
	private String requestParams;

	/**
	 * 错误信息.
	 */
	private String errorMessage;

	/**
	 * 操作状态 0成功 1失败.
	 */
	private Integer status;

	/**
	 * 操作的消耗时间(毫秒).
	 */
	private Long costTime;

	/**
	 * 操作的IP地址.
	 */
	private String ip;

	/**
	 * 操作的服务环境.
	 */
	private String profile;

	/**
	 * 操作的服务地址.
	 */
	private String serviceAddress;

	/**
	 * 操作的堆栈信息.
	 */
	private String stackTrace;

}
