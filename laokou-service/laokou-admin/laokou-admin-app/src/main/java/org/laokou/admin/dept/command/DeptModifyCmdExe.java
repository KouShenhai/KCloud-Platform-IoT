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

import org.laokou.admin.dept.dto.DeptModifyCmd;
import org.laokou.admin.dept.model.DeptE;
import org.laokou.admin.dept.service.extensionpoint.DeptParamValidatorExtPt;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.laokou.admin.dept.convertor.DeptConvertor;
import org.laokou.admin.dept.ability.DeptDomainService;

/**
 * 修改部门命令执行器.
 *
 * @author laokou
 */
@Component
public class DeptModifyCmdExe {

	@Autowired
	@Qualifier("modifyDeptParamValidator")
	private DeptParamValidatorExtPt modifyDeptParamValidator;

	private final DeptDomainService deptDomainService;

	private final TransactionalUtil transactionalUtil;

	public DeptModifyCmdExe(DeptDomainService deptDomainService, TransactionalUtil transactionalUtil) {
		this.deptDomainService = deptDomainService;
		this.transactionalUtil = transactionalUtil;
	}

	@CommandLog
	public void executeVoid(DeptModifyCmd cmd) {
		// 校验参数
		DeptE deptE = DeptConvertor.toEntity(cmd.getCo());
		modifyDeptParamValidator.validate(deptE);
		transactionalUtil.executeInTransaction(() -> deptDomainService.update(deptE));
	}

}
