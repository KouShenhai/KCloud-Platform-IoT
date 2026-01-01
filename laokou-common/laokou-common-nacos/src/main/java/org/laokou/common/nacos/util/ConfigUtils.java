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

package org.laokou.common.nacos.util;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import java.util.Properties;

/**
 * @author laokou
 */
public final class ConfigUtils {

	private ConfigUtils() {
	}

	/**
	 * 创建配置服务.
	 * @param serverAddr 服务地址
	 */
	public static ConfigService createConfigService(String serverAddr) throws NacosException {
		return NacosFactory.createConfigService(serverAddr);
	}

	/**
	 * 创建服务地址.
	 * @param properties 配置
	 */
	public static ConfigService createConfigService(Properties properties) throws NacosException {
		return NacosFactory.createConfigService(properties);
	}

}
