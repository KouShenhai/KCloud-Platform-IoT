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

package org.laokou.common.core;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.RequestUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author laokou
 */
@SpringBootTest
@ContextConfiguration(classes = { RequestUtils.class, RequestMappingHandlerMapping.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RequestUtilsTest {

	private final HandlerMapping handlerMapping;

	RequestUtilsTest(@Qualifier("requestMappingHandlerMapping") HandlerMapping handlerMapping) {
		this.handlerMapping = handlerMapping;
	}

	@Test
	void test_request() throws Exception {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		Assertions.assertThat(request).isNotNull();
		Assertions.assertThat(handlerMapping).isNotNull();
		Assertions.assertThat(request.getHeaderNames().hasMoreElements()).isFalse();
		UserAgentParser parser = RequestUtils.getUserAgentParser();
		Assertions.assertThat(parser).isNotNull();
		String defaultUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
		Capabilities capabilities = parser.parse(defaultUserAgent);
		Assertions.assertThat(capabilities.getBrowser()).isEqualTo("Chrome");
		Assertions.assertThat(capabilities.getBrowserType()).isEqualTo("Browser");
		Assertions.assertThat(capabilities.getBrowserMajorVersion()).isEqualTo("58");
		Assertions.assertThat(capabilities.getPlatform()).isEqualTo("Win10");
		Assertions.assertThat(capabilities.getPlatformVersion()).isEqualTo("10.0");
		Assertions.assertThat(capabilities.getDeviceType()).isEqualTo("Desktop");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.PLATFORM_MAKER)).isEqualTo("Microsoft Corporation");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.RENDERING_ENGINE_NAME)).isEqualTo("Blink");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.RENDERING_ENGINE_VERSION)).isEqualTo("Unknown");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.RENDERING_ENGINE_MAKER)).isEqualTo("Google Inc");
		Assertions.assertThat(RequestUtils.getHandlerMethod(request, handlerMapping)).isNull();
		capabilities = RequestUtils.getCapabilities(request);
		Assertions.assertThat(capabilities.getValue(BrowsCapField.BROWSER)).isEqualTo("Unknown");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.BROWSER_TYPE)).isEqualTo("Unknown");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.BROWSER_MAJOR_VERSION)).isEqualTo("Unknown");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.PLATFORM)).isEqualTo("Unknown");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.PLATFORM_VERSION)).isEqualTo("Unknown");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.PLATFORM_MAKER)).isEqualTo("Unknown");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.DEVICE_TYPE)).isEqualTo("Unknown");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.RENDERING_ENGINE_NAME)).isEqualTo("Unknown");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.RENDERING_ENGINE_VERSION)).isEqualTo("Unknown");
		Assertions.assertThat(capabilities.getValue(BrowsCapField.RENDERING_ENGINE_MAKER)).isEqualTo("Unknown");
		Assertions.assertThat(RequestUtils.getParamValue(request, "test")).isEqualTo("");
		byte[] body = RequestUtils.getRequestBody(request);
		Assertions.assertThat(body.length).isEqualTo(0);
		Assertions.assertThat(RequestUtils.getInputStream(body)).isNotNull();
		ServletRequest requestWrapper = new RequestUtils.RequestWrapper(request);
		Assertions.assertThat(requestWrapper).isNotNull();
		Assertions.assertThat(requestWrapper.getInputStream()).isNotNull();
		Assertions.assertThat(requestWrapper.getReader()).isNotNull();
	}

}
