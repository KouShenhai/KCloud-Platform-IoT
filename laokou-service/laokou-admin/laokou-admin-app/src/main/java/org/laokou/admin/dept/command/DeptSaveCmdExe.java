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

package org.laokou.admin.dept.command;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dept.ability.DeptDomainService;
import org.laokou.admin.dept.convertor.DeptConvertor;
import org.laokou.admin.dept.dto.DeptSaveCmd;
import org.laokou.admin.dept.model.DeptE;
import org.laokou.admin.dept.service.extensionpoint.DeptParamValidatorExtPt;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.constant.Constant.*;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;

/**
 * 保存部门命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptSaveCmdExe {

	private final DeptDomainService deptDomainService;

	private final TransactionalUtil transactionalUtil;

	private final ExtensionExecutor extensionExecutor;

	public void executeVoid(DeptSaveCmd cmd) {
		// 校验参数
		DeptE deptE = DeptConvertor.toEntity(cmd.getCo());
		extensionExecutor.executeVoid(DeptParamValidatorExtPt.class, BizScenario.valueOf(SAVE, DEPT, SCENARIO),
				extension -> extension.validate(deptE));
		transactionalUtil.executeInTransaction(() -> deptDomainService.create(deptE));
	}

}
