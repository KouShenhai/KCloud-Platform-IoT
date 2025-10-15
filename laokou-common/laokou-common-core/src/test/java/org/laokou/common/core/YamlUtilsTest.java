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

package org.laokou.common.core;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.YamlUtils;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author laokou
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor
@ContextConfiguration(classes = { YamlUtils.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class YamlUtilsTest {

	@Test
	void test_getPropertyAndLoad() throws IOException {
		Assertions.assertThat(YamlUtils.getProperty("application.yml", "spring.application.name"))
			.isEqualTo("laokou-common-core");
		Assertions.assertThat(YamlUtils.getProperty("application.yml", "spring.application.name2")).isEqualTo("");
		Assertions.assertThat(YamlUtils.getProperty("application.yml", "spring.application.name2", "default"))
			.isEqualTo("default");
		YamlTest yamlTest = YamlUtils.load("name: laokou", YamlTest.class);
		Assertions.assertThat(yamlTest.getName()).isEqualTo("laokou");
		String str = YamlUtils.dumpAsMap(yamlTest).trim();
		Assertions.assertThat(str).isEqualTo("name: laokou");
		YamlTest yamlTest2 = YamlUtils.load(str, YamlTest.class);
		Assertions.assertThat(yamlTest2.getName()).isEqualTo("laokou");
		Assertions.assertThat(yamlTest2).isEqualTo(yamlTest);
		YamlTest yamlTest1 = YamlUtils.load(ResourceExtUtils.getResource("test.yaml").getInputStream(), YamlTest.class);
		Assertions.assertThat(yamlTest1.getName()).isEqualTo("laokou");
		Assertions.assertThat(yamlTest1).isEqualTo(yamlTest);
	}

	@Data
	static class YamlTest implements Serializable {

		private String name;

	}

}
