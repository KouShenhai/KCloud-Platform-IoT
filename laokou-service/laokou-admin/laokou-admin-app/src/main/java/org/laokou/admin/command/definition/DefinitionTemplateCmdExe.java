/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.admin.command.definition;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.laokou.admin.dto.definition.DefinitionTemplateCmd;
import org.laokou.common.core.utils.ResourceUtil;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.laokou.common.i18n.common.Constant.SLASH;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DefinitionTemplateCmdExe {

	private static final String TEMPLATE_NAME = "audit.bpmn20.xml";

	private static final String TEMPLATE_PATH = "/templates";

	@SneakyThrows
	public void executeVoid(DefinitionTemplateCmd cmd) {
		HttpServletResponse response = cmd.getResponse();
		response.setContentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=".concat(TEMPLATE_NAME));
		try (InputStream inputStream = ResourceUtil.getResource(TEMPLATE_PATH + SLASH + TEMPLATE_NAME).getInputStream();
				ServletOutputStream outputStream = response.getOutputStream()) {
			IOUtils.write(inputStream.readAllBytes(), outputStream);
		}

	}

}
