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

package org.laokou.common.nacos.util;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import java.util.Properties;

// @formatter:off
/**
 * @see <a href="https://nacos.io/docs/latest/manual/user/java-sdk/usage/?spm=5238cd80.47ee59c.0.0.189fcd3671Q8Cm">...</a>
 * @author laokou
 */
// @formatter:on
public final class NamingUtils {

	private NamingUtils() {
	}

	/**
	 * 创建服务发现.
	 * @param serverAddr 服务地址
	 */
	public static NamingService createNamingService(String serverAddr) throws NacosException {
		return NacosFactory.createNamingService(serverAddr);
	}

	/**
	 * 创建服务发现.
	 * @param properties 配置
	 */
	public static NamingService createNamingService(Properties properties) throws NacosException {
		return NacosFactory.createNamingService(properties);
	}

}
