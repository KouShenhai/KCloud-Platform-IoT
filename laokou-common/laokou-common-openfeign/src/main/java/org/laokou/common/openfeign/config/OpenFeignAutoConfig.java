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

package org.laokou.common.openfeign.config;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import feign.*;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.idempotent.utils.IdempotentUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Map;

import static org.laokou.common.i18n.common.constant.Constant.AUTHORIZATION;
import static org.laokou.common.i18n.common.constant.StringConstant.UNDER;
import static org.laokou.common.i18n.common.constant.TraceConstant.*;

// @formatter:off
/**
 * openfeign关闭ssl {@link FeignAutoConfiguration}
 * 开启MVC 请查看 {@link FeignClientsConfiguration}
 * 默认开启，支持@RequestLine @Header @RequestPart.
 *
 * @author laokou
 */
// @formatter:on

@Slf4j
@RequiredArgsConstructor
@Import({ FeignClientsConfiguration.class })
@AutoConfiguration(before = SentinelFeignAutoConfiguration.class)
public class OpenFeignAutoConfig extends ErrorDecoder.Default implements RequestInterceptor {

	private final IdempotentUtil idempotentUtil;

	@Bean
	public feign.Logger.Level loggerLevel() {
		return Logger.Level.NONE;
	}

	// @formatter:off
	/**
	 * Feign灰度路由请查看源码 OkHttpFeignLoadBalancerConfiguration > FeignBlockingLoadBalancerClient > ReactiveLoadBalancer(响应式变阻塞式) > NacosLoadBalancer.
	 * @param template 请求模板
	 */
	// @formatter:on
	@Override
	public void apply(RequestTemplate template) {
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		template.header(AUTHORIZATION, request.getHeader(AUTHORIZATION));
		template.header(SERVICE_HOST, request.getHeader(SERVICE_HOST));
		template.header(SERVICE_PORT, request.getHeader(SERVICE_PORT));
		template.header(SERVICE_GRAY, request.getHeader(SERVICE_GRAY));
		final boolean idempotent = IdempotentUtil.isIdempotent();
		if (idempotent) {
			// 获取当前Feign客户端的接口名称
			String clientName = template.feignTarget().type().getName();
			// 获取请求的URL
			String url = template.url();
			String method = template.method();
			// 将接口名称 + URL + 请求方式组合成一个key
			String uniqueKey = clientName + UNDER + url + UNDER + method;
			Map<String, String> idMap = IdempotentUtil.getRequestId();
			// 检查是否已经为这个键生成了ID
			String idempotentKey = idMap.get(uniqueKey);
			if (!idMap.containsKey(uniqueKey)) {
				// 如果没有，生成一个新的幂等性ID
				idempotentKey = idempotentUtil.getIdempotentKey();
				idMap.put(uniqueKey, idempotentKey);
			}
			template.header(REQUEST_ID, idempotentKey);
		}
	}

	@Bean
	public Retryer retryer() {
		// 最大请求次数为5，初始间隔时间为100ms
		// 下次间隔时间1.5倍递增，重试间最大间隔时间为1s
		return new Retryer.Default();
	}

	@Override
	public Exception decode(String methodKey, Response response) {
		return super.decode(methodKey, response);
	}

}
