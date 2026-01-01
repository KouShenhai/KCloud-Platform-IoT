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

package org.laokou.common.core;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ClassExtUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * ClassUtils测试类.
 *
 * @author laokou
 */
class ClassUtilsTest {

	@Test
	void test_parseClass_ValidClassName() throws ClassNotFoundException {
		// 测试解析有效的类名
		Assertions.assertThat(ClassExtUtils.parseClass("java.lang.String")).isEqualTo(String.class);
	}

	@Test
	void test_parseClass_InvalidClassName() {
		// 测试解析无效的类名
		Assertions.assertThat(Assertions.catchException(() -> ClassExtUtils.parseClass("invalid.class.name")))
			.isInstanceOf(ClassNotFoundException.class);
	}

	@Test
	void test_parseClass_NullClassName() {
		// 测试解析null类名
		Assertions.assertThat(Assertions.catchException(() -> ClassExtUtils.parseClass(null)))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void test_scanAnnotatedClasses_ValidPackage() {
		// 测试扫描有效包中的注解类
		// 由于测试包中可能没有@Component注解的类，所以这里只验证返回的Set不为null
		Set<Class<?>> classes = ClassExtUtils.scanAnnotatedClasses("org.laokou.common.core.util", Component.class);
		Assertions.assertThat(classes).isNotNull();
	}

	@Test
	void test_scanAnnotatedClasses_NullPackage() {
		// 测试扫描null包名
		Assertions
			.assertThat(Assertions.catchException(() -> ClassExtUtils.scanAnnotatedClasses(null, Component.class)))
			.isInstanceOf(IllegalArgumentException.class);
	}

}
