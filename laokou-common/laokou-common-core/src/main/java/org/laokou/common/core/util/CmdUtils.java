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

import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
public final class CmdUtils {

	private CmdUtils() {
	}

	public static String executeCmd(String... command) throws IOException, InterruptedException {
		StringBuilder output = new StringBuilder();
		Process process = null;
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			processBuilder.redirectErrorStream(true);
			process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}
			boolean finished = process.waitFor(15, TimeUnit.SECONDS);
			if (!finished) {
				process.destroy();
				throw new SystemException("S_Cmd_ExecuteTimeout", "执行命令超时");
			}
			int exitValue = process.exitValue();
			if (exitValue != 0) {
				throw new SystemException("S_Cmd_ExecuteFailed", output.toString());
			}
			return output.toString();
		}
		finally {
			if (ObjectUtils.isNotNull(process)) {
				process.destroy();
			}
		}
	}

}
