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

import lombok.Data;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.ObjectUtil;

/**
 * 部门领域对象【实体】.
 *
 * @author laokou
 */
@Data
public class DeptE {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 部门父节点ID.
	 */
	private Long pid;

	/**
	 * 部门名称.
	 */
	private String name;

	/**
	 * 部门节点.
	 */
	private String path;

	/**
	 * 部门排序.
	 */
	private Integer sort;

	/**
	 * 部门父节点路径.
	 */
	private String parentPath;

	public void getParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public void checkParentPath() {
		if (ObjectUtil.isNull(this.parentPath)) {
			throw new SystemException("S_Dept_PathNotExist", "部门路径不存在");
		}
	}

	public String getNewPath() {
		return this.parentPath + "," + this.id;
	}

	public String getOldPrefix() {
		return "^" + this.path;
	}

	public String getNewPrefix() {
		return getNewPath();
	}

}
