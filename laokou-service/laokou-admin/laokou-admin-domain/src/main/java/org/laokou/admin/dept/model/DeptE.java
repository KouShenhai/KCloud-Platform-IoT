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

package org.laokou.admin.dept.model;

import lombok.Getter;
import lombok.Setter;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.dto.Identifier;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 部门领域对象【实体】.
 *
 * @author laokou
 */
@Entity
public class DeptE extends Identifier {

	/**
	 * 部门父节点ID.
	 */
	@Setter
	@Getter
	private Long pid;

	/**
	 * 部门名称.
	 */
	@Setter
	@Getter
	private String name;

	/**
	 * 部门节点.
	 */
	@Setter
	@Getter
	private String path;

	/**
	 * 部门排序.
	 */
	@Setter
	@Getter
	private Integer sort;

	@Setter
	@Getter
	private DeptOperateTypeEnum deptOperateTypeEnum;

	/**
	 * 部门父节点路径.
	 */
	private String parentPath;

	/**
	 * 旧路径.
	 */
	@Getter
	private String oldPath;

	private final DeptParamValidator saveDeptParamValidator;

	private final DeptParamValidator modifyDeptParamValidator;

	private final IdGenerator idGenerator;

	// @formatter:off
	public DeptE(@Qualifier("modifyDeptParamValidator") DeptParamValidator saveDeptParamValidator,
				 @Qualifier("saveDeptParamValidator") DeptParamValidator modifyDeptParamValidator,
				 IdGenerator idGenerator) {
		this.saveDeptParamValidator = saveDeptParamValidator;
		this.modifyDeptParamValidator = modifyDeptParamValidator;
		this.idGenerator = idGenerator;
	}
	// @formatter:on

	public Long getPrimaryKey() {
		return idGenerator.getId();
	}

	public void checkDeptParam() {
		switch (deptOperateTypeEnum) {
			case SAVE -> saveDeptParamValidator.validateDept(this);
			case MODIFY -> modifyDeptParamValidator.validateDept(this);
			default -> {
			}
		}
	}

	public void getOldPath(String oldPath) {
		this.oldPath = oldPath;
	}

	public void getParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public void checkParentPath(Long id) {
		if (ObjectUtils.isNull(this.parentPath)) {
			throw new BizException("B_Dept_ParentPathNotExist", "父级部门路径不存在");
		}
		this.path = this.parentPath + "," + id;
	}

	public void checkOldPath() {
		if (ObjectUtils.isNull(this.oldPath)) {
			throw new BizException("B_Dept_OldPathNotExist", "旧部门路径不存在");
		}
	}

	public String getOldPrefix() {
		return "^" + this.oldPath;
	}

	public String getNewPrefix() {
		return this.path;
	}

}
