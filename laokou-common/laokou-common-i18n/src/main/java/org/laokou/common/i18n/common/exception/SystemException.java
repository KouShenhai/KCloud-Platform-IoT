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

package org.laokou.common.i18n.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "SystemException", description = "系统异常")
public final class SystemException extends GlobalException {

	@Schema(name = "IP_BLACKED", description = "IP已列入黑名单")
	public static final String IP_BLACKED = "S_Ip_Blacked";

	@Schema(name = "IP_RESTRICTED", description = "IP被限制")
	public static final String IP_RESTRICTED = "S_Ip_Restricted";

	@Schema(name = "ROUTER_NOT_EXIST", description = "路由不存在")
	public static final String ROUTER_NOT_EXIST = "S_Gateway_RouterNotExist";

	@Schema(name = "AUTHORITY", description = "授权规则错误")
	public static final String AUTHORITY = "S_Sentinel_Authority";

	@Schema(name = "SYSTEM_BLOCKED", description = "系统规则错误")
	public static final String SYSTEM_BLOCKED = "S_Sentinel_SystemBlocked";

	@Schema(name = "PARAM_FLOWED", description = "热点参数已限流")
	public static final String PARAM_FLOWED = "S_Sentinel_ParamFlowed";

	@Schema(name = "DEGRADED", description = "已降级")
	public static final String DEGRADED = "S_Sentinel_Degraded";

	@Schema(name = "FLOWED", description = "已限流")
	public static final String FLOWED = "S_Sentinel_Flowed";

	@Schema(name = "DISTRIBUTED_TRANSACTION_DOWNTIME", description = "分布式事务已宕机")
	public static final String DISTRIBUTED_TRANSACTION_DOWNTIME = "S_Seata_TransactionDowntime";

	@Schema(name = "DISTRIBUTED_TRANSACTION_TIMEOUT", description = "分布式事务已超时")
	public static final String DISTRIBUTED_TRANSACTION_TIMEOUT = "S_Seata_TransactionTimeout";

	@Schema(name = "S_DS_TableNotExist", description = "表不存在")
	public static final String TABLE_NOT_EXIST = "S_DS_TableNotExist";

	public SystemException(String code) {
		super(code);
	}

	public SystemException(String code, String msg) {
		super(code, msg);
	}

}
