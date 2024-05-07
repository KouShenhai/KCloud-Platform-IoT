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

package org.laokou.common.core.common.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.core.utils.AddressUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.i18n.common.constants.EventType;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.core.common.domain.OperateLog;

import java.io.Serial;

import static org.laokou.common.i18n.common.constants.EventStatus.CREATED;
import static org.laokou.common.i18n.common.RocketMqConstant.LAOKOU_OPERATE_EVENT_TOPIC;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
@Schema(name = "OperateEvent", description = "操作事件")
public class OperateEvent extends DomainEvent<Long> {

	@Serial
	private static final long serialVersionUID = -6523521638764501311L;

	@Schema(name = "name", description = "操作名称")
	protected String name;

	@Schema(name = "moduleName", description = "操作的模块名称")
	protected String moduleName;

	@Schema(name = "uri", description = "操作的URI")
	protected String uri;

	@Schema(name = "methodName", description = "操作的方法名")
	protected String methodName;

	@Schema(name = "requestType", description = "操作的请求类型")
	protected String requestType;

	@Schema(name = "requestParams", description = "操作的请求参数")
	protected String requestParams;

	@Schema(name = "userAgent", description = "操作的浏览器")
	protected String userAgent;

	@Schema(name = "ip", description = "操作的IP地址")
	protected String ip;

	@Schema(name = "address", description = "操作的归属地")
	protected String address;

	@Schema(name = "status", description = "操作状态 0成功 1失败")
	protected Integer status;

	@Schema(name = "operator", description = "操作人")
	protected String operator;

	@Schema(name = "errorMessage", description = "错误信息")
	protected String errorMessage;

	@Schema(name = "takeTime", description = "操作的消耗时间(毫秒)")
	protected Long takeTime;

	public OperateEvent(OperateLog operateLog, HttpServletRequest request, UserContextHolder.User user, String appName,
			Integer status, EventType eventType) {
		super(IdGenerator.defaultSnowflakeId(), user.getId(), eventType, CREATED, LAOKOU_OPERATE_EVENT_TOPIC,
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
		this.status = status;
	}

}
