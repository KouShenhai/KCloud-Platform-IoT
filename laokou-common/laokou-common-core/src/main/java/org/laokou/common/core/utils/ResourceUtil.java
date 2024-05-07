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

package org.laokou.common.core.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

/**
 * 资源解析工具类.
 *
 * @author laokou
 */
public class ResourceUtil extends ResourceUtils {

	/**
	 * 资源解析器.
	 */
	private static final ResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();

	/**
	 * 根据路径获取资源.
	 * @param location 路径
	 * @return 资源
	 */
	public static Resource getResource(String location) {
		return RESOLVER.getResource(location);
	}

}
