/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.i18n.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public class ResponseUtil {

	public static void response(HttpServletResponse response, int code, String message) throws IOException {
		response.setStatus(HttpStatus.OK.value());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
		try (PrintWriter writer = response.getWriter()) {
			writer.write(JacksonUtil.toJsonStr(Result.fail(code, message)));
			writer.flush();
		}
	}

}
