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

package org.laokou.common.nacos.handler;

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.ResourceUtils;
import org.laokou.common.core.util.SpringUtils;
import org.laokou.common.core.util.TemplateUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 路由处理.
 *
 * @author laokou
 */
@Slf4j
@NonNullApi
@RequiredArgsConstructor
public class RouterHandler implements ApplicationListener<ApplicationReadyEvent> {

	private final SpringUtils springUtils;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {
			String serviceId = springUtils.getServiceId();
			Assert.isTrue(StringUtils.isNotEmpty(serviceId), "ServiceID is empty");
			Map<String, Object> map = new HashMap<>(2);
			String abbr = serviceId.substring(7);
			map.put("serviceId", serviceId);
			map.put("abbr", abbr);
			String router = getRouter(map);
			log.info("\n----------Nacos路由配置开始(请复制到router.json)----------" + "{}"
					+ "----------Nacos路由配置结束(请复制到router.json)----------", router);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取模板解析路由配置.
	 * @param dataMap map对象
	 * @return 路由配置
	 */
	private String getRouter(Map<String, Object> dataMap) throws IOException {
		String template = ResourceUtils.getResource("templates/router_template.json")
			.getContentAsString(StandardCharsets.UTF_8)
			.trim();
		return TemplateUtils.getContent(template, dataMap);
	}

}
