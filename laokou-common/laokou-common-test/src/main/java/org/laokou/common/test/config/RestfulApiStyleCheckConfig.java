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

package org.laokou.common.test.config;

import io.micrometer.common.lang.NonNullApi;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author why
 * @author laokou
 */
@Slf4j
@NonNullApi
public class RestfulApiStyleCheckConfig implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	private final Set<String> whiteList = Set.of("/error", "/v3/api-docs", "/swagger-ui.html", "/v3/api-docs/{group}",
			"/v3/api-docs.yaml", "/v3/api-docs/swagger-config", "/v3/api-docs.yaml/{group}");

	private final Set<String> whiteMethodList = Set.of("resetPwd");

	private final Set<String> methodNamePrefixList = Set.of("save", "modify", "remove", "import", "export", "page",
			"list", "count", "get", "upload");

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
		List<RestfulApiStyleCheckResult> list = new ArrayList<>();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			RequestMappingInfo requestMappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();
			RestfulApiStyleCheckResult result = new RestfulApiStyleCheckResult();
			Method method = handlerMethod.getMethod();
			Class<?> beanType = handlerMethod.getBeanType();
			String methodName = method.getName();
			Set<String> patternValues = requestMappingInfo.getPatternValues()
				.stream()
				.filter(StringUtils::isNotEmpty)
				.filter(item -> !checkWhiteUrl(item))
				.collect(Collectors.toSet());
			Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
			String desc = "";
			if (beanType.isAnnotationPresent(Tag.class)) {
				Tag tag = beanType.getAnnotation(Tag.class);
				desc = tag.name();
			}
			if (method.isAnnotationPresent(Operation.class)) {
				Operation operation = method.getAnnotation(Operation.class);
				desc += "-" + operation.summary();
			}
			for (String patternValue : patternValues) {
				String requestMethod = methods.stream().findFirst().map(Enum::name).orElse("");
				result.setRequestMethod(requestMethod);
				result.setUrl(patternValue);
				result.setMethodName(methodName);
				result.setDescription(desc + checkApiStyleAndResult(methodName, patternValue));
				list.add(result);
			}
		}
		// 排序
		list.sort(Comparator.comparing(RestfulApiStyleCheckResult::getDescription));
		printLog(list);
	}

	private String checkApiStyleAndResult(String methodName, String url) {
		List<String> list = new ArrayList<>(2);
		list.add(checkMethodNameAndResult(methodName));
		list.add(checkUrlFormatAndResult(url));
		list = list.stream().filter(StringUtils::isNotEmpty).toList();
		return list.isEmpty() ? "【✔】" : "【❌" + String.join("、", list) + "】";
	}

	private String checkMethodNameAndResult(String methodName) {
		boolean isExist = false;
		for (String prefix : whiteMethodList) {
			if (methodName.startsWith(prefix)) {
				return "";
			}
		}
		for (String prefix : methodNamePrefixList) {
			if (methodName.startsWith(prefix)) {
				isExist = true;
				break;
			}
		}
		if (!isExist) {
			return "方法名不符合规范，方法名必须以save/modify/remove/import/export/page/list/count/get/upload开头";
		}
		return "";
	}

	private String checkUrlFormatAndResult(String url) {
		String[] segments = url.split("/");
		String str = "";
		boolean isExist = false;
		if (url.contains("//")) {
			str += "URL格式不符合规范，URL中不能出现连续的斜杠、";
		}
		for (String s : methodNamePrefixList) {
			if (segments[segments.length - 1].startsWith(s)) {
				isExist = true;
				break;
			}
		}
		if (isExist) {
			str += "URL格式不符合规范，URL中最后一个路径不能以save/modify/remove/import/export/page/list/count/get/upload开头";
		}
		for (String segment : segments) {
			if (segment.isEmpty()) {
				continue;
			}
			// 路径参数跳过
			if (segment.contains("{") && segment.contains("}")) {
				continue;
			}
			// 检查restful路径【URL中只能包含小写字母、数字和横杠】
			if (!Pattern.matches("^[a-z0-9\\-]+$", segment)) {
				return str + "URL格式不符合规范，URL中只能包含小写字母、数字和横杠";
			}
		}
		return "";
	}

	private void printLog(List<RestfulApiStyleCheckResult> list) {
		log.info(
				"======================================== RESTFul API 风格检查结果 ========================================");
		log.info(String.format("%-50s %-10s %-30s %-100s", "URL", "请求方法", "方法名称", "描述"));
		for (RestfulApiStyleCheckResult restfulApiStyleCheckResult : list) {
			log.info(String.format("%-50s %-10s %-30s %-100s", restfulApiStyleCheckResult.getUrl(),
					restfulApiStyleCheckResult.getRequestMethod(), restfulApiStyleCheckResult.getMethodName(),
					restfulApiStyleCheckResult.getDescription()));
		}
		log.info(
				"======================================== RESTFul API 风格检查结果 ========================================");
	}

	private boolean checkWhiteUrl(String url) {
		for (String str : whiteList) {
			if (antPathMatcher.match(str, url)) {
				return true;
			}
		}
		return false;
	}

	@Data
	private static class RestfulApiStyleCheckResult {

		private String url;

		private String methodName;

		private String requestMethod;

		private String description;

	}

}
