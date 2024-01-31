/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.domain.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.hc.core5.http.HttpHeaders;
import org.laokou.admin.domain.log.OperateLog;
import org.laokou.common.core.holder.UserContextHolder;
import org.laokou.common.core.utils.AddressUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.DateUtil;

import java.io.Serial;

import static org.laokou.common.i18n.common.EventStatusEnums.CREATED;
import static org.laokou.common.i18n.common.EventTypeEnums.OPERATE_SUCCEEDED;
import static org.laokou.common.i18n.common.NumberConstants.SUCCESS;
import static org.laokou.common.i18n.common.RocketMqConstants.LAOKOU_OPERATE_LOG_TOPIC;

/**
 * @author laokou
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "OperateLogEvent", description = "操作日志事件")
public class OperateSucceededEvent extends DomainEvent<Long> {

	@Serial
	private static final long serialVersionUID = -6523521638764501311L;

	@Schema(name = "name", description = "操作名称")
	private String name;

	@Schema(name = "moduleName", description = "操作的模块名称")
	private String moduleName;

	@Schema(name = "uri", description = "操作的URI")
	private String uri;

	@Schema(name = "methodName", description = "操作的方法名")
	private String methodName;

	@Schema(name = "requestType", description = "操作的请求类型")
	private String requestType;

	@Schema(name = "requestParams", description = "操作的请求参数")
	private String requestParams;

	@Schema(name = "userAgent", description = "操作的浏览器")
	private String userAgent;

	@Schema(name = "ip", description = "操作的IP地址")
	private String ip;

	@Schema(name = "address", description = "操作的归属地")
	private String address;

	@Schema(name = "status", description = "操作状态 0成功 1失败")
	private Integer status = SUCCESS;

	@Schema(name = "operator", description = "操作人")
	private String operator;

	@Schema(name = "errorMessage", description = "错误信息")
	private String errorMessage;

	@Schema(name = "takeTime", description = "操作的消耗时间(毫秒)")
	private Long takeTime;

	public OperateSucceededEvent(OperateLog operateLog, HttpServletRequest request, UserContextHolder.User user,
			String appName) {
		super(IdGenerator.defaultSnowflakeId(), user.getId(), OPERATE_SUCCEEDED, CREATED, LAOKOU_OPERATE_LOG_TOPIC,
				user.getSourceName(), appName, user.getId(), user.getId(), user.getDeptId(), user.getDeptPath(),
				user.getTenantId(), DateUtil.now(), DateUtil.now());
		this.takeTime = operateLog.getTakeTime();
		this.errorMessage = operateLog.getErrorMessage();
		this.operator = user.getUsername();
		String ip = IpUtil.getIpAddr(request);
		this.address = AddressUtil.getRealAddress(ip);
		this.ip = ip;
		this.userAgent = request.getHeader(HttpHeaders.USER_AGENT);
		this.requestParams = operateLog.getRequestParams();
		this.requestType = request.getMethod();
		this.methodName = operateLog.getMethodName();
		this.uri = request.getRequestURI();
		this.moduleName = operateLog.getModuleName();
		this.name = operateLog.getName();
	}

}
