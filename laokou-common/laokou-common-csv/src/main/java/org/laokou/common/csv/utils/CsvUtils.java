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

package org.laokou.common.csv.utils;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author laokou
 */
public final class CsvUtils {

	private CsvUtils() {
	}

	public static void execute(File file, Executor executor) throws IOException {
		try (CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(file, true)))) {
			executor.execute(csvWriter);
		}
	}

	@FunctionalInterface
	public interface Executor {

		void execute(CSVWriter csvWriter);

	}

}
