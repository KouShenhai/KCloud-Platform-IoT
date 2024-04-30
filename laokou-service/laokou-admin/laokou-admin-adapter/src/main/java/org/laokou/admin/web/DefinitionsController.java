/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.DefinitionsServiceI;
import org.laokou.admin.dto.definition.DefinitionTemplateCmd;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laokou
 */
@RestController
@Tag(name = "DefinitionsController", description = "流程定义")
@RequiredArgsConstructor
@RequestMapping("v1/definitions")
public class DefinitionsController {

	private final DefinitionsServiceI definitionsServiceI;

	@GetMapping("download-template")
	@Operation(summary = "流程定义", description = "下载模板")
	@PreAuthorize("hasAuthority('definitions:download-template')")
	public void downloadTemplate(HttpServletResponse response) {
		definitionsServiceI.downloadTemplate(new DefinitionTemplateCmd(response));
	}

}
