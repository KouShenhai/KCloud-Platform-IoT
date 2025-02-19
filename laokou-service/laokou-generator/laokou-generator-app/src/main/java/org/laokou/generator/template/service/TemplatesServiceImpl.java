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

package org.laokou.generator.template.service;

import lombok.RequiredArgsConstructor;
import org.laokou.generator.template.api.TemplatesServiceI;
import org.laokou.generator.template.command.*;
import org.laokou.generator.template.command.query.TemplateGetQryExe;
import org.laokou.generator.template.command.query.TemplatePageQryExe;
import org.laokou.generator.template.dto.*;
import org.laokou.generator.template.dto.clientobject.TemplateCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 *
 * 代码生成器模板接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class TemplatesServiceImpl implements TemplatesServiceI {

	private final TemplateSaveCmdExe templateSaveCmdExe;

	private final TemplateModifyCmdExe templateModifyCmdExe;

	private final TemplateRemoveCmdExe templateRemoveCmdExe;

	private final TemplateImportCmdExe templateImportCmdExe;

	private final TemplateExportCmdExe templateExportCmdExe;

	private final TemplatePageQryExe templatePageQryExe;

	private final TemplateGetQryExe templateGetQryExe;

	@Override
	public void save(TemplateSaveCmd cmd) {
		templateSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(TemplateModifyCmd cmd) {
		templateModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(TemplateRemoveCmd cmd) {
		templateRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(TemplateImportCmd cmd) {
		templateImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(TemplateExportCmd cmd) {
		templateExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<TemplateCO>> page(TemplatePageQry qry) {
		return templatePageQryExe.execute(qry);
	}

	@Override
	public Result<TemplateCO> getById(TemplateGetQry qry) {
		return templateGetQryExe.execute(qry);
	}

}
