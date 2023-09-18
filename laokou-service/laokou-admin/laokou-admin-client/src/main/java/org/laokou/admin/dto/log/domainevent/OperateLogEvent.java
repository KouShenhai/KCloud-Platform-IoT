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
package org.laokou.admin.dto.log.domainevent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.io.Serializable;
import java.time.Clock;

/**
 * @author laokou
 */
@Getter
@Setter
public class OperateLogEvent extends ApplicationEvent implements Serializable {

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
	private Integer status;

	@Schema(name = "operator", description = "操作人")
	private String operator;

	@Schema(name = "errorMessage", description = "错误信息")
	private String errorMessage;

	@Schema(name = "takeTime", description = "操作的消耗时间(毫秒)")
	private Long takeTime;

	public OperateLogEvent(Object source) {
		super(source);
	}

	public OperateLogEvent(Object source, Clock clock) {
		super(source, clock);
	}

}
