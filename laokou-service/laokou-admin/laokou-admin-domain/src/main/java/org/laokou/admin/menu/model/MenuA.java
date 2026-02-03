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

package org.laokou.admin.menu.model;

import lombok.Getter;
import org.laokou.admin.menu.model.entity.MenuE;
import org.laokou.admin.menu.model.enums.MenuStatus;
import org.laokou.admin.menu.model.enums.MenuType;
import org.laokou.admin.menu.model.enums.OperateType;
import org.laokou.admin.menu.model.validator.MenuParamValidator;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.common.i18n.common.ValidateName;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * 菜单领域对象【实体】.
 *
 * @author laokou
 */
@Entity
@Getter
public class MenuA extends AggregateRoot implements ValidateName {

	private MenuE menuE;

	/**
	 * 操作类型【新增/修改】.
	 */
	private OperateType operateType;

	/*
	 * ID生成器.
	 */
	private final IdGenerator idGenerator;

	/*
	 * 新增参数校验器.
	 */
	private final MenuParamValidator saveMenuParamValidator;

	/*
	 * 修改参数校验器.
	 */
	private final MenuParamValidator modifyMenuParamValidator;

	public MenuA(IdGenerator idGenerator,
			@Qualifier("saveMenuParamValidator") MenuParamValidator saveMenuParamValidator,
			@Qualifier("modifyMenuParamValidator") MenuParamValidator modifyMenuParamValidator) {
		this.idGenerator = idGenerator;
		this.saveMenuParamValidator = saveMenuParamValidator;
		this.modifyMenuParamValidator = modifyMenuParamValidator;
	}

	public MenuA create(MenuE menuE) {
		this.menuE = menuE;
		Long primaryKey = this.menuE.getId();
		super.createTime = InstantUtils.now();
		super.id = ObjectUtils.isNotNull(primaryKey) ? primaryKey : idGenerator.getId();
		this.operateType = ObjectUtils.isNotNull(primaryKey) ? OperateType.MODIFY : OperateType.SAVE;
		return this;
	}

	public void checkMenuParam() {
		switch (operateType) {
			case SAVE -> saveMenuParamValidator.validateMenu(this);
			case MODIFY -> modifyMenuParamValidator.validateMenu(this);
			default -> throw new UnsupportedOperationException("Unsupported operation");
		}
	}

	@Override
	public String getValidateName() {
		return "Menu";
	}

	public boolean isSave() {
		return ObjectUtils.equals(OperateType.SAVE, this.operateType);
	}

	public boolean isModify() {
		return ObjectUtils.equals(OperateType.MODIFY, this.operateType);
	}

	public boolean isMenu() {
		return ObjectUtils.isNotNull(menuE.getType()) && menuE.getType() == MenuType.MENU.getCode();
	}

	public boolean statusNotExist() {
		return !List.of(MenuStatus.DISABLE.getCode(), MenuStatus.ENABLE.getCode()).contains(this.menuE.getStatus());
	}

}
