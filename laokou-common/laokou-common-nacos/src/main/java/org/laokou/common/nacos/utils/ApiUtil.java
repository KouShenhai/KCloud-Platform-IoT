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

package org.laokou.common.nacos.utils;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.common.core.utils.HttpUtil;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.nacos.proxy.ProtocolProxy;
import org.laokou.common.nacos.clientobject.ConfigCO;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.laokou.common.core.constant.BizConstant.*;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ApiUtil {

	private final NacosConfigProperties nacosConfigProperties;

	private final ProtocolProxy protocolProxy;

	private final ConfigUtil configUtil;

	private static final String ACCESS_TOKEN = "accessToken";

	public String getToken() {
		String tokenUri = protocolProxy.getTokenUri(nacosConfigProperties.getServerAddr());
		Map<String, String> params = new HashMap<>(2);
		String username = nacosConfigProperties.getUsername();
		String password = nacosConfigProperties.getPassword();
		params.put(USERNAME, username);
		params.put(PASSWORD, password);
		String result = HttpUtil.doPost(tokenUri, params, new HashMap<>(0), protocolProxy.sslEnabled());
		if (StringUtil.isEmpty(result)) {
			return "";
		}
		return JacksonUtil.readTree(result).get(ACCESS_TOKEN).asText();
	}

	public ConfigCO getConfigInfo(String token) {
		String configUri = protocolProxy.getConfigUri(nacosConfigProperties.getServerAddr());
		String username = nacosConfigProperties.getUsername();
		String group = configUtil.getGroup();
		String nameSpace = configUtil.getNameSpace();
		Map<String, String> params = new HashMap<>(7);
		params.put("dataId", ConfigUtil.ROUTER_DATA_ID);
		params.put("group", group);
		params.put("namespaceId", nameSpace);
		params.put("tenant", nameSpace);
		params.put("show", "all");
		params.put(ACCESS_TOKEN, token);
		params.put(USERNAME, username);
		String configInfo = HttpUtil.doGet(configUri, params, new HashMap<>(0), protocolProxy.sslEnabled());
		if (StringUtil.isEmpty(configInfo)) {
			return null;
		}
		return JacksonUtil.toBean(configInfo, ConfigCO.class);
	}

	public void doConfigInfo(ConfigCO co, String token) {
		String configUri = protocolProxy.getConfigUri(nacosConfigProperties.getServerAddr());
		HttpUtil.doPost(configUri, getMap(co, token), new HashMap<>(0), protocolProxy.sslEnabled());
	}

	@SneakyThrows
	private Map<String, String> getMap(ConfigCO co, String token) {
		String username = nacosConfigProperties.getUsername();
		Map<String, String> params = new HashMap<>(20);
		params.put(ACCESS_TOKEN, token);
		params.put(USERNAME, username);
		Field[] fields = co.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Object o = field.get(co);
			params.put(field.getName(), o == null ? "" : o.toString());
		}
		return params;
	}

}
