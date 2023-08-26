/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.common.dynamic.router.utils;

import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.ResourceUtil;
import org.laokou.common.dynamic.router.RouteDefinition;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.nacos.utils.ApiUtil;
import org.laokou.common.nacos.clientobject.ConfigCO;
import org.laokou.common.core.utils.TemplateUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RouterUtil {

	private final Environment env;

	private final ApiUtil apiUtil;

	@SneakyThrows
	public void initRouter() {
		String appId = env.getProperty("spring.application.name");
		assert appId != null;
		Map<String, Object> dataMap = new HashMap<>(2);
		String name = appId.substring(7);
		dataMap.put("appId", appId);
		dataMap.put("name", name);
		String router = getRouter(dataMap);
		RouteDefinition routeDefinition = JacksonUtil.toBean(router, RouteDefinition.class);
		// 获取nacos的token
		String token = apiUtil.getToken();
		if (StringUtil.isEmpty(token)) {
			return;
		}
		// 拉取所有的路由配置
		ConfigCO configCO = apiUtil.getConfigInfo(token);
		if (configCO == null) {
			return;
		}
		List<RouteDefinition> routeDefinitions = JacksonUtil.toList(configCO.getContent(), RouteDefinition.class);
		// 判断是否已经配置
		if (CollectionUtil.isEmpty(routeDefinitions)) {
			log.error("请配置动态路由");
			return;
		}
		boolean isExist = routeDefinitions.stream().anyMatch(r -> r.getId().equals(routeDefinition.getId()));
		// 添加并发布
		if (!isExist) {
			routeDefinitions.add(routeDefinition);
			String toPrettyFormat = GsonUtil.toPrettyFormat(routeDefinitions);
			configCO.setContent(toPrettyFormat);
			apiUtil.doConfigInfo(configCO, token);
			log.info("服务路由已添加并发布");
		}
		else {
			log.info("服务路由已存在，无需添加");
		}
	}

	private String getRouter(Map<String, Object> dataMap) throws IOException, TemplateException {
		InputStream inputStream = ResourceUtil.getResource("init_router.json").getInputStream();
		byte[] bytes = inputStream.readAllBytes();
		String template = new String(bytes, StandardCharsets.UTF_8);
		return TemplateUtil.getContent(template, dataMap);
	}

}
