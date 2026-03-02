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

package org.laokou.admin.role.model;

import lombok.Getter;
import org.laokou.admin.role.model.entity.RoleE;
import org.laokou.admin.role.model.enums.OperateType;
import org.laokou.admin.role.model.validator.RoleParamValidator;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.common.i18n.common.ValidateName;
import org.laokou.common.i18n.common.enums.BizType;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;
import java.util.List;

/**
 * 角色领域对象【实体】.
 *
 * @author laokou
 */
@Entity
@Getter
public class RoleA extends AggregateRoot implements ValidateName {

	private RoleE roleE;

	private OperateType operateType;

	private final IdGenerator commonIdGenerator;

	private final RoleParamValidator saveRoleParamValidator;

	private final RoleParamValidator modifyRoleParamValidator;

	private final RoleParamValidator modifyRoleAuthorityParamValidator;

	public RoleA(@Qualifier("commonIdGenerator") IdGenerator commonIdGenerator,
			@Qualifier("saveRoleParamValidator") RoleParamValidator saveRoleParamValidator,
			@Qualifier("modifyRoleParamValidator") RoleParamValidator modifyRoleParamValidator,
			@Qualifier("modifyRoleAuthorityParamValidator") RoleParamValidator modifyRoleAuthorityParamValidator) {
		this.commonIdGenerator = commonIdGenerator;
		this.saveRoleParamValidator = saveRoleParamValidator;
		this.modifyRoleParamValidator = modifyRoleParamValidator;
		this.modifyRoleAuthorityParamValidator = modifyRoleAuthorityParamValidator;
	}

	public RoleA create(RoleE roleE, OperateType operateType) {
		this.roleE = roleE;
		Long primaryKey = this.roleE.getId();
		super.createTime = InstantUtils.now();
		super.id = ObjectUtils.isNotNull(primaryKey) ? primaryKey : commonIdGenerator.getId(BizType.ROLE);
		this.operateType = operateType;
		return this;
	}

	public void checkRoleParam() {
		switch (operateType) {
			case SAVE -> saveRoleParamValidator.validateRole(this);
			case MODIFY -> modifyRoleParamValidator.validateRole(this);
			case MODIFY_AUTHORITY -> modifyRoleAuthorityParamValidator.validateRole(this);
			default -> throw new UnsupportedOperationException("Unsupported operation");
		}
	}

	public List<Long> getIds(int num) {
		return commonIdGenerator.getIds(BizType.ROLE, num);
	}

	@Override
	public String getValidateName() {
		return "Role";
	}

	public boolean isSave() {
		return ObjectUtils.equals(OperateType.SAVE, this.operateType);
	}

	public boolean isModify() {
		return ObjectUtils.equals(OperateType.MODIFY, this.operateType);
	}

	public Instant getCreateTime() {
		return isSave() ? super.createTime : null;
	}

}
