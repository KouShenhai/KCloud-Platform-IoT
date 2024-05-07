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

package org.laokou.common.nacos.handler;

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.ResourceUtil;
import org.laokou.common.core.utils.TemplateUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务监听.
 *
 * @author laokou
 */
@Slf4j
@NonNullApi
@AutoConfiguration
@RequiredArgsConstructor
public class RouterListener implements ApplicationListener<ApplicationReadyEvent> {

	private final Environment env;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		String appName = env.getProperty("spring.application.name");
		Assert.isTrue(StringUtil.isNotEmpty(appName), " app name is empty");
		Map<String, Object> map = new HashMap<>(2);
		String abbr = appName.substring(7);
		map.put("appName", appName);
		map.put("abbr", abbr);
		String router = getRouter(map);
		log.info("""
				\n----------Nacos路由配置开始(请复制到router.json)----------
				{}
				----------Nacos路由配置结束(请复制到router.json)----------""", router);
	}

	/**
	 * 获取模板解析路由配置.
	 * @param dataMap map对象
	 * @return 路由配置
	 */
	@SneakyThrows
	private String getRouter(Map<String, Object> dataMap) {
		try (InputStream inputStream = ResourceUtil.getResource("templates/router_template.json").getInputStream()) {
			byte[] bytes = inputStream.readAllBytes();
			String template = new String(bytes, StandardCharsets.UTF_8);
			return TemplateUtil.getContent(template, dataMap);
		}
	}

}
