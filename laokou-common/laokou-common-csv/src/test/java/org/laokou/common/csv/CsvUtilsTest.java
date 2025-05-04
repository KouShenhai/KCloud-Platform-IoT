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

package org.laokou.common.csv;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.csv.utils.CsvUtils;
import org.springframework.boot.system.SystemProperties;

import java.io.File;
import java.io.IOException;

/**
 * @author laokou
 */
class CsvUtilsTest {

	private String testPath;

	@BeforeEach
	void setUp() {
		testPath = SystemProperties.get("user.home") + "/test";
	}

	@Test
	void test() throws IOException {
		String fileName = "test.csv";
		String[] arr = { "1", "2", "3" };
		File file = new File(testPath, fileName);
		Assertions.assertDoesNotThrow(() -> CsvUtils.execute(file, csvWriter -> csvWriter.writeNext(arr), false));
		Assertions.assertEquals("\"1\",\"2\",\"3\"", FileUtils.readFileToString(file, "UTF-8").trim());
		Assertions.assertDoesNotThrow(() -> FileUtils.forceDeleteOnExit(file));
	}

}
