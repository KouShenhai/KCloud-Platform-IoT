/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.dept.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.common.FindTypeEnum;
import org.laokou.admin.dto.dept.DeptListQry;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.admin.gatewayimpl.database.DeptMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.common.core.utils.TreeUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查询部门列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptListQryExe {

	private final DeptMapper deptMapper;

	/**
	 * 执行查询部门列表.
	 * @param qry 查询部门列表参数
	 * @return 部门列表
	 */
	@DS(TENANT)
	public Result<List<DeptCO>> execute(DeptListQry qry) {
		return switch (FindTypeEnum.valueOf(qry.getType())) {
			case LIST -> Result.of(getDeptList(qry).stream().map(this::convert).toList());
			case TREE_LIST -> Result.of(buildTreeNode(getDeptList(qry).stream().map(this::convert).toList()));
			case USER_TREE_LIST -> null;
		};
	}

	private List<DeptCO> buildTreeNode(List<DeptCO> list) {
		return TreeUtil.buildTreeNode(list, DeptCO.class).getChildren();
	}

	private DeptCO convert(DeptDO deptDO) {
		return DeptCO.builder()
			.id(deptDO.getId())
			.pid(deptDO.getPid())
			.name(deptDO.getName())
			.sort(deptDO.getSort())
			.children(new ArrayList<>(16))
			.build();
	}

	private List<DeptDO> getDeptList(DeptListQry qry) {
		String name = qry.getName();
		LambdaQueryWrapper<DeptDO> wrapper = Wrappers.lambdaQuery(DeptDO.class)
			.like(StringUtil.isNotEmpty(name), DeptDO::getName, name)
			.orderByDesc(DeptDO::getSort)
			.select(DeptDO::getId, DeptDO::getPid, DeptDO::getName, DeptDO::getSort);
		return deptMapper.selectList(wrapper);
	}

}
