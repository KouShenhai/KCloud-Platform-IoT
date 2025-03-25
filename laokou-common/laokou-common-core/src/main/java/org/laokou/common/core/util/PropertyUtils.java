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

package org.laokou.common.core.util;

import org.laokou.common.i18n.util.ResourceUtils;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

/**
 * 属性工具类.
 *
 * @author laokou
 */
public final class PropertyUtils {

	private PropertyUtils() {
	}

	/**
	 * 绑定或创建属性.
	 * @param bindName 配置前缀
	 * @param clazz 类
	 * @param location 文件名称
	 * @param format 格式
	 * @param <T> 泛型
	 * @return 属性
	 */
	public static <T> T bindOrCreate(String bindName, Class<T> clazz, String location, String format)
			throws IOException {
		StandardEnvironment environment = new StandardEnvironment();
		Resource resource = ResourceUtils.getResource(location);
		List<PropertySource<?>> propertySourceList = new YamlPropertySourceLoader().load(format, resource);
		propertySourceList.forEach(propertySource -> environment.getPropertySources().addLast(propertySource));
		return Binder.get(environment).bindOrCreate(bindName, clazz);
	}

}
