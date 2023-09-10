/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.admin.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.database.dataobject.BaseDO;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@TableName("boot_sys_operate_log")
@Schema(name = "OperateLogDO", description = "操作日志")
public class OperateLogDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 834248318156804579L;

	/**
	 * ，如：系统菜单
	 */
	@Schema(name = "moduleName", description = "模块名称")
	private String moduleName;

	@Schema(name = "operationName", description = "操作名称")
	private String operationName;

	@Schema(name = "requestUri", description = "请求URI")
	private String requestUri;

	@Schema(name = "requestMethod", description = "请求方式")
	private String requestMethod;

	@Schema(name = "requestParams", description = "请求参数")
	private String requestParams;

	@Schema(name = "userAgent", description = "浏览器版本")
	private String userAgent;

	@Schema(name = "requestIp", description = "请求IP")
	private String requestIp;

	@Schema(name = "requestAddress", description = "归属地")
	private String requestAddress;

	@Schema(name = "requestStatus", description = "请求状态 0成功 1失败")
	private Integer requestStatus;

	@Schema(name = "operator", description = "操作人")
	private String operator;

	@Schema(name = "errorMsg", description = "错误信息")
	private String errorMsg;

	@Schema(name = "methodName", description = "方法名称")
	private String methodName;

	@Schema(name = "takeTime", description = "耗时")
	private Long takeTime;

}
