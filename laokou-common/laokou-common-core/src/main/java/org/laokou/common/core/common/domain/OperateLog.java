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

package org.laokou.common.core.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.laokou.common.core.common.event.OperateFailedEvent;
import org.laokou.common.core.common.event.OperateSucceededEvent;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.laokou.common.core.utils.JacksonUtil.EMPTY_JSON;
import static org.laokou.common.i18n.common.constants.StringConstant.*;

/**
 * @author laokou
 */
@Data
@Schema(name = "OperateLog", description = "操作日志")
public class OperateLog extends AggregateRoot<Long> {

	private static final Set<String> REMOVE_PARAMS = Set.of("username", "password", "mail", "mobile");

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

	@Schema(name = "operator", description = "操作人")
	private String operator;

	@Schema(name = "errorMessage", description = "错误信息")
	private String errorMessage;

	@Schema(name = "status", description = "操作状态 0成功 1失败")
	private Integer status;

	@Schema(name = "takeTime", description = "操作的消耗时间(毫秒)")
	private Long takeTime;

	public OperateLog(String moduleName, String name) {
		this.moduleName = moduleName;
		this.name = name;
	}

	public void modifyStatus(Exception e, HttpServletRequest request, String appName) {
		if (ObjectUtil.isNotNull(e)) {
			operateFail(e, request, appName);
		}
		else {
			operateSuccess(request, appName);
		}
	}

	public void decorateMethodName(String className, String methodName) {
		this.methodName = className + DOT + methodName + LEFT + RIGHT;
	}

	public void calculateTaskTime(long startTime) {
		this.takeTime = IdGenerator.SystemClock.now() - startTime;
	}

	public void decorateRequestParams(Object[] args) {
		List<Object> params = new ArrayList<>(Arrays.asList(args)).stream().filter(this::filterArgs).toList();
		if (CollectionUtil.isEmpty(params)) {
			this.requestParams = EMPTY_JSON;
		}
		else {
			Object obj = params.getFirst();
			try {
				Map<String, String> map = JacksonUtil.toMap(obj, String.class, String.class);
				removeAny(map, REMOVE_PARAMS.toArray(String[]::new));
				this.requestParams = JacksonUtil.toJsonStr(map, true);
			}
			catch (Exception e) {
				this.requestParams = JacksonUtil.toJsonStr(obj, true);
			}
		}
	}

	private void operateSuccess(HttpServletRequest request, String appName) {
		addEvent(new OperateSucceededEvent(this, request, UserContextHolder.get(), appName));
	}

	private void operateFail(Exception e, HttpServletRequest request, String appName) {
		this.errorMessage = e.getMessage();
		addEvent(new OperateFailedEvent(this, request, UserContextHolder.get(), appName));
	}

	private void removeAny(Map<String, String> map, String... keys) {
		for (String key : keys) {
			map.remove(key);
		}
	}

	private boolean filterArgs(Object arg) {
		return !(arg instanceof HttpServletRequest) && !(arg instanceof MultipartFile)
				&& !(arg instanceof HttpServletResponse);
	}

}
