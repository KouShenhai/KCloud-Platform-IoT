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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	private HandlerMapping handlerMapping;

	@Test
	void testRequest() throws Exception {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		Assertions.assertNotNull(request);
		Assertions.assertNotNull(handlerMapping);
		Assertions.assertFalse(request.getHeaderNames().hasMoreElements());
		UserAgentParser parser = RequestUtils.getUserAgentParser();
		Assertions.assertNotNull(parser);
		String defaultUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
		Capabilities capabilities = parser.parse(defaultUserAgent);
		Assertions.assertEquals("Chrome", capabilities.getBrowser());
		Assertions.assertEquals("Browser", capabilities.getBrowserType());
		Assertions.assertEquals("58", capabilities.getBrowserMajorVersion());
		Assertions.assertEquals("Win10", capabilities.getPlatform());
		Assertions.assertEquals("10.0", capabilities.getPlatformVersion());
		Assertions.assertEquals("Desktop", capabilities.getDeviceType());
		Assertions.assertEquals("Microsoft Corporation", capabilities.getValue(BrowsCapField.PLATFORM_MAKER));
		Assertions.assertEquals("Blink", capabilities.getValue(BrowsCapField.RENDERING_ENGINE_NAME));
		Assertions.assertEquals("Unknown", capabilities.getValue(BrowsCapField.RENDERING_ENGINE_VERSION));
		Assertions.assertEquals("Google Inc", capabilities.getValue(BrowsCapField.RENDERING_ENGINE_MAKER));
		Assertions.assertNull(RequestUtils.getHandlerMethod(request, handlerMapping));
		capabilities = RequestUtils.getCapabilities(request);
		Assertions.assertEquals("Unknown", capabilities.getValue(BrowsCapField.BROWSER));
		Assertions.assertEquals("Unknown", capabilities.getValue(BrowsCapField.BROWSER_TYPE));
		Assertions.assertEquals("Unknown", capabilities.getValue(BrowsCapField.BROWSER_MAJOR_VERSION));
		Assertions.assertEquals("Unknown", capabilities.getValue(BrowsCapField.PLATFORM));
		Assertions.assertEquals("Unknown", capabilities.getValue(BrowsCapField.PLATFORM_VERSION));
		Assertions.assertEquals("Unknown", capabilities.getValue(BrowsCapField.PLATFORM_MAKER));
		Assertions.assertEquals("Unknown", capabilities.getValue(BrowsCapField.DEVICE_TYPE));
		Assertions.assertEquals("Unknown", capabilities.getValue(BrowsCapField.RENDERING_ENGINE_NAME));
		Assertions.assertEquals("Unknown", capabilities.getValue(BrowsCapField.RENDERING_ENGINE_VERSION));
		Assertions.assertEquals("Unknown", capabilities.getValue(BrowsCapField.RENDERING_ENGINE_MAKER));
		Assertions.assertEquals("", RequestUtils.getParamValue(request, "test"));
		byte[] body = RequestUtils.getRequestBody(request);
		Assertions.assertEquals(0, body.length);
		Assertions.assertNotNull(RequestUtils.getInputStream(body));
		ServletRequest requestWrapper = new RequestUtils.RequestWrapper(request);
		Assertions.assertNotNull(requestWrapper);
		Assertions.assertNotNull(requestWrapper.getInputStream());
		Assertions.assertNotNull(requestWrapper.getReader());
	}

}
