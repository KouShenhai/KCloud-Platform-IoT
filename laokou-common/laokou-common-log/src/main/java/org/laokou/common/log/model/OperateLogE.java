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

package org.laokou.common.log.model;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.core.util.*;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.util.DateUtils;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.*;

import static org.laokou.common.i18n.util.JacksonUtils.EMPTY_JSON;
import static org.laokou.common.i18n.common.constant.StringConstants.*;
import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * 操作日志.
 *
 * @author laokou
 */
@Getter
@Entity
public class OperateLogE {

	private final Set<String> removeParams = Set.of("username", "password", "mail", "mobile");

	/**
	 * 操作名称.
	 */
	private String name;

	/**
	 * 操作的模块名称.
	 */
	private String moduleName;

	/**
	 * 操作的请求路径.
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

	public OperateLogE fillValue() {
		this.createTime = DateUtils.nowInstant();
		this.operator = UserContextHolder.get().getUsername();
		return this;
	}

	public void getProfile(String profile) {
		this.profile = profile;
	}

	public void getModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void getName(String name) {
		this.name = name;
	}

	public void getServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void getRequest(HttpServletRequest request) throws Exception {
		this.uri = request.getRequestURI();
		this.requestType = request.getMethod();
		this.userAgent = request.getHeader(USER_AGENT);
		this.ip = IpUtils.getIpAddr(request);
		this.address = AddressUtils.getRealAddress(this.ip);
		this.serviceAddress = System.getProperty("address");
	}

	public void getThrowable(Throwable throwable) {
		if (ObjectUtils.isNotNull(throwable)) {
			if (throwable instanceof GlobalException globalException) {
				this.errorMessage = globalException.getMsg();
			}
			else {
				this.errorMessage = throwable.getMessage();
			}
			this.stackTrace = getStackTraceAsString(throwable);
			this.status = StatusEnum.FAIL.getCode();
		}
		else {
			this.status = StatusEnum.OK.getCode();
		}
	}

	public void decorateMethodName(String className, String methodName) {
		this.methodName = className + DOT + methodName + LEFT + RIGHT;
	}

	public void calculateTaskTime(StopWatch stopWatch) {
		stopWatch.stop();
		this.costTime = stopWatch.getTotalTimeMillis();
	}

	public void decorateRequestParams(Object[] args) {
		List<Object> params = new ArrayList<>(Arrays.asList(args)).stream().filter(this::filterArgs).toList();
		if (CollectionUtils.isEmpty(params)) {
			this.requestParams = EMPTY_JSON;
		}
		else {
			Object obj = params.getFirst();
			try {
				Map<String, String> map = JacksonUtils.toMap(obj, String.class, String.class);
				deleteAny(map, removeParams.toArray(String[]::new));
				this.requestParams = JacksonUtils.toJsonStr(map, true);
			}
			catch (Exception e) {
				this.requestParams = JacksonUtils.toJsonStr(obj, true);
			}
		}
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

	private String getStackTraceAsString(Throwable throwable) {
		if (org.springframework.util.ObjectUtils.isEmpty(throwable)) {
			return EMPTY;
		}
		StringWriter stringWriter = new StringWriter();
		try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
			throwable.printStackTrace(printWriter);
		}
		return stringWriter.toString();
	}

}
