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

package org.laokou.common.i18n;

import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.JacksonUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * @author laokou
 */
class JacksonUtilsTest {

	private Path testPath;

	@BeforeEach
	void setUp() {
		testPath = Path.of(System.getProperty("user.home", "d:/test"), "test", "jackson");
	}

	@Test
	void test() throws IOException {
		TestUser testUser = new TestUser();
		testUser.setId(1L);
		testUser.setName("laokou");
		Assertions.assertThat(JacksonUtils.toJsonStr(testUser)).isEqualTo("{\"id\":1,\"name\":\"laokou\"}");
		String str = JacksonUtils.toJsonStr(testUser, true);
		Assertions.assertThat(JacksonUtils.toBean(str, TestUser.class)).isEqualTo(testUser);
		Assertions.assertThat(JacksonUtils.toBean(str.getBytes(), TestUser.class)).isEqualTo(testUser);
		Assertions.assertThat(JacksonUtils.toBean(str.getBytes(), 0, str.getBytes().length, TestUser.class))
			.isEqualTo(testUser);
		Assertions.assertThat(JacksonUtils.toBean(new ByteArrayInputStream(str.getBytes()), TestUser.class))
			.isEqualTo(testUser);
		Assertions.assertThat(new String(JacksonUtils.toBytes(testUser))).isEqualTo("{\"id\":1,\"name\":\"laokou\"}");

		List<TestUser> list = List.of(testUser);
		Assertions.assertThat(JacksonUtils.toJsonStr(list)).isEqualTo("[{\"id\":1,\"name\":\"laokou\"}]");
		String str2 = JacksonUtils.toJsonStr(list, true);
		Assertions.assertThat(JacksonUtils.toList(str2, TestUser.class)).isEqualTo(list);

		if (Files.notExists(testPath)) {
			Files.createDirectories(testPath);
		}
		Path tempFilePath = Path.of(testPath.toString(), "test.json");
		if (Files.notExists(tempFilePath)) {
			Files.createFile(tempFilePath);
		}
		Assertions.assertThatNoException().isThrownBy(() -> Files.write(tempFilePath, str2.getBytes()));
		Assertions.assertThat(JacksonUtils.toList(tempFilePath.toFile(), TestUser.class)).isEqualTo(list);

		Map<String, Object> map = JacksonUtils.toMap(str, String.class, Object.class);
		Assertions.assertThat(map).containsEntry("id", 1);
		Assertions.assertThat(map).containsEntry("name", "laokou");

		Assertions.assertThat(JacksonUtils.toValue(map, TestUser.class)).isEqualTo(testUser);

		map = JacksonUtils.toMap(testUser, String.class, Object.class);
		Assertions.assertThat(map).isInstanceOf(Map.class);
		Assertions.assertThat(JacksonUtils.readTree(str).get("name").asString()).isEqualTo("laokou");
	}

	@Data
	static class TestUser {

		private Long id;

		private String name;

	}

}
