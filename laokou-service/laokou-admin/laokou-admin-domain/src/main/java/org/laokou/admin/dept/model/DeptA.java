/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.dept.model;

import lombok.Getter;
import org.laokou.admin.dept.model.entity.DeptE;
import org.laokou.admin.dept.model.enums.OperateType;
import org.laokou.admin.dept.model.validator.DeptParamValidator;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.common.i18n.common.ValidateName;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 部门领域对象【实体】.
 *
 * @author laokou
 */
@Entity
@Getter
public class DeptA extends AggregateRoot implements ValidateName {

	private DeptE deptE;

	/**
	 * 操作类型【保存/修改】.
	 */
	private OperateType operateType;

	private final IdGenerator idGenerator;

	private final DeptParamValidator saveDeptParamValidator;

	private final DeptParamValidator modifyDeptParamValidator;

	public DeptA(IdGenerator idGenerator,
			@Qualifier("modifyDeptParamValidator") DeptParamValidator saveDeptParamValidator,
			@Qualifier("saveDeptParamValidator") DeptParamValidator modifyDeptParamValidator) {
		this.idGenerator = idGenerator;
		this.saveDeptParamValidator = saveDeptParamValidator;
		this.modifyDeptParamValidator = modifyDeptParamValidator;
	}

	public DeptA create(DeptE deptE) {
		this.deptE = deptE;
		Long primaryKey = this.deptE.getId();
		super.createTime = InstantUtils.now();
		super.id = ObjectUtils.isNotNull(primaryKey) ? primaryKey : idGenerator.getId();
		this.operateType = ObjectUtils.isNotNull(primaryKey) ? OperateType.MODIFY : OperateType.SAVE;
		return this;
	}

	public void checkDeptParam() {
		switch (operateType) {
			case SAVE -> saveDeptParamValidator.validateDept(this);
			case MODIFY -> modifyDeptParamValidator.validateDept(this);
			default -> throw new UnsupportedOperationException("Unsupported operation type");
		}
	}

	public boolean isModify() {
		return ObjectUtils.equals(OperateType.MODIFY, this.operateType);
	}

	@Override
	public String getValidateName() {
		return "Dept";
	}

}
