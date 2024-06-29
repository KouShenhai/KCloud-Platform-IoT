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

package org.laokou.common.log.model;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.core.utils.*;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.laokou.common.core.utils.JacksonUtil.EMPTY_JSON;
import static org.laokou.common.i18n.common.constants.StringConstant.*;
import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * 操作日志.
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogA extends AggregateRoot<Long> {

	private static final Set<String> REMOVE_PARAMS = Set.of("username", "password", "mail", "mobile");

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
	 * 操作的方法名.
	 */
	private String methodName;

	/**
	 * 操作的请求类型.
	 */
	private String requestType;

	/**
	 * 操作的请求参数.
	 */
	private String requestParams;

	/**
	 * 操作的浏览器.
	 */
	private String userAgent;

	/**
	 * 操作的IP地址.
	 */
	private String ip;

	/**
	 * 操作的归属地.
	 */
	private String address;

	/**
	 * 操作人.
	 */
	private String operator;

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
	private Long takeTime;

	private static final int OK = 0;

	private static final int FAIL = 1;

	public LogA(String moduleName, String name, HttpServletRequest request, String appName) {
		UserContextHolder.User user = UserContextHolder.get();
		this.moduleName = moduleName;
		this.name = name;
		this.uri = request.getRequestURI();
		this.requestType = request.getMethod();
		this.userAgent = request.getHeader(USER_AGENT);
		this.ip = IpUtil.getIpAddr(request);
		this.address = AddressUtil.getRealAddress(ip);
		this.deptId = user.getDeptId();
		this.deptPath = user.getDeptPath();
		this.tenantId = user.getTenantId();
		this.id = IdGenerator.defaultSnowflakeId();
		this.createDate = DateUtil.now();
		this.updateDate = this.createDate;
		this.creator = user.getId();
		this.editor = this.creator;
		this.appName = appName;
		this.sourceName = user.getSourceName();
		this.operator = user.getUsername();
	}

	public void updateStatus(Exception e) {
		if (ObjectUtil.isNotNull(e)) {
			fail(e);
		}
		else {
			ok();
		}
	}

	public void decorateMethodName(String className, String methodName) {
		this.methodName = className + DOT + methodName + LEFT + RIGHT;
	}

	public void calculateTaskTime(long startTime) {
		this.takeTime = System.currentTimeMillis() - startTime;
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
				deleteAny(map, REMOVE_PARAMS.toArray(String[]::new));
				this.requestParams = JacksonUtil.toJsonStr(map, true);
			}
			catch (Exception e) {
				this.requestParams = JacksonUtil.toJsonStr(obj, true);
			}
		}
	}

	private void ok() {
		this.status = OK;
	}

	private void fail(Exception e) {
		this.errorMessage = e.getMessage();
		this.status = FAIL;
	}

	private void deleteAny(Map<String, String> map, String... keys) {
		for (String key : keys) {
			map.remove(key);
		}
	}

	private boolean filterArgs(Object arg) {
		return !(arg instanceof HttpServletRequest) && !(arg instanceof MultipartFile)
				&& !(arg instanceof HttpServletResponse);
	}

}
