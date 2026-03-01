/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.common.core.util.CollectionExtUtils;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.common.entity.OperateLogE;
import org.laokou.common.log.model.enums.Status;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 操作日志聚合.
 *
 * @author laokou
 */
@Entity
@Getter
public class OperateLogA extends AggregateRoot {

	private OperateLogE operateLogE;

	private final IdGenerator idGenerator;

	public OperateLogA(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public OperateLogA create(OperateLogE operateLogE) {
		this.operateLogE = operateLogE;
		super.createTime = InstantUtils.now();
		super.id = idGenerator.getId();
		return this;
	}

	public void getThrowable(Throwable throwable) {
		int status = Status.OK.getCode();
		String errorMessage = StringConstants.EMPTY;
		String stackTrace = StringConstants.EMPTY;
		if (ObjectUtils.isNotNull(throwable)) {
			if (throwable instanceof GlobalException globalException) {
				errorMessage = globalException.getMsg();
			}
			else {
				errorMessage = throwable.getMessage();
			}
			stackTrace = getStackTraceAsString(throwable);
			status = Status.FAIL.getCode();
		}
		this.operateLogE = this.operateLogE.toBuilder()
			.status(status)
			.errorMessage(errorMessage)
			.stackTrace(stackTrace)
			.build();
	}

	public void decorateMethodName(String className, String methodName) {
		this.operateLogE = this.operateLogE.toBuilder()
			.methodName(className + StringConstants.DOT + methodName + StringConstants.LEFT + StringConstants.RIGHT)
			.build();
	}

	public void calculateTaskTime(StopWatch stopWatch) {
		stopWatch.stop();
		this.operateLogE = this.operateLogE.toBuilder().costTime(stopWatch.getTotalTimeMillis()).build();
	}

	public void decorateRequestParams(Object[] args) {
		List<Object> params = new ArrayList<>(Arrays.asList(args)).stream().filter(this::filterArgs).toList();
		String requestParams = JacksonUtils.EMPTY_JSON;
		if (CollectionExtUtils.isNotEmpty(params)) {
			requestParams = JacksonUtils.toJsonStr(params.getFirst());
		}
		this.operateLogE = this.operateLogE.toBuilder().requestParams(requestParams).build();
	}

	private boolean filterArgs(Object arg) {
		return !(arg instanceof HttpServletRequest) && !(arg instanceof MultipartFile)
				&& !(arg instanceof HttpServletResponse);
	}

	private String getStackTraceAsString(Throwable throwable) {
		if (org.springframework.util.ObjectUtils.isEmpty(throwable)) {
			return StringConstants.EMPTY;
		}
		StringWriter stringWriter = new StringWriter();
		try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
			throwable.printStackTrace(printWriter);
		}
		return stringWriter.toString();
	}

}
