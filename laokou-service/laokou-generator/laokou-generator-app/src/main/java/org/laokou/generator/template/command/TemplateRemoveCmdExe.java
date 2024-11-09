/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.generator.template.command;

import lombok.RequiredArgsConstructor;
import org.laokou.generator.template.dto.TemplateRemoveCmd;
import org.springframework.stereotype.Component;
import org.laokou.generator.template.ability.TemplateDomainService;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;

/**
 *
 * 删除代码生成器模板命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TemplateRemoveCmdExe {

	private final TemplateDomainService templateDomainService;

	private final TransactionalUtil transactionalUtil;

	public void executeVoid(TemplateRemoveCmd cmd) {
		// 校验参数
		transactionalUtil.executeInTransaction(() -> templateDomainService.delete(cmd.getIds()));
	}

}
