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

package org.laokou.common.i18n.util;

import org.laokou.common.i18n.common.constant.StringConstants;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author laokou
 */
public final class YamlUtils {

	private YamlUtils() {
	}

	public static String getProperty(String path, String key) {
		return getProperty(path, key, StringConstants.EMPTY);
	}

	public static String getProperty(String path, String key, String defaultValue) {
		return createProperties(path).getProperty(key, defaultValue);
	}

	public static <T> T load(InputStream inputStream, Class<T> clazz) {
		return createYaml(clazz).load(inputStream);
	}

	public static <T> T load(String str, Class<T> clazz) {
		return createYaml(clazz).load(str);
	}

	public static String dumpAsMap(Object obj) {
		return createYaml().dumpAsMap(obj);
	}

	private static <T> Yaml createYaml(Class<T> clazz) {
		LoaderOptions loaderOptions = new LoaderOptions();
		loaderOptions.setAllowDuplicateKeys(false);
		loaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
		loaderOptions.setAllowRecursiveKeys(true);
		loaderOptions.setCodePointLimit(Integer.MAX_VALUE);
		return createYaml(clazz, loaderOptions);
	}

	private static Yaml createYaml() {
		return createYaml(Object.class);
	}

	private static <T> Yaml createYaml(Class<T> clazz, LoaderOptions loaderOptions) {
		BaseConstructor constructor = new Constructor(clazz, loaderOptions);
		DumperOptions dumperOptions = new DumperOptions();
		Representer representer = new Representer(dumperOptions);
		NoTimestampResolver resolver = new NoTimestampResolver();
		Yaml yaml = new Yaml(constructor, representer, dumperOptions, loaderOptions, resolver);
		yaml.setBeanAccess(BeanAccess.FIELD);
		return yaml;
	}

	private static Properties createProperties(String path) {
		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		factory.setResources(new ClassPathResource(path));
		return factory.getObject();
	}

	private static final class NoTimestampResolver extends Resolver {

		public void addImplicitResolver(Tag tag, Pattern regexp, String first, int limit) {
			if (tag != Tag.TIMESTAMP) {
				super.addImplicitResolver(tag, regexp, first, limit);
			}
		}

	}

}
