package org.laokou.gateway.utils;

import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class RequestUtil {

	public static String getParamValue(ServerHttpRequest request, String paramName) {
		// 从header中获取
		String paramValue = request.getHeaders().getFirst(paramName);
		// 从参数中获取
		if (StringUtil.isEmpty(paramValue)) {
			paramValue = request.getQueryParams().getFirst(paramName);
		}
		return StringUtil.isEmpty(paramValue) ? "" : paramValue.trim();
	}

}
