/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DeptConvertor;
import org.laokou.admin.domain.dept.Dept;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.dto.dept.DeptTreeGetQry;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.common.core.utils.TreeUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.mybatisplus.constant.DsConstant.TENANT;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptTreeGetQryExe {

	private final DeptGateway deptGateway;

	private final DeptConvertor deptConvertor;

	@DS(TENANT)
	public Result<DeptCO> execute(DeptTreeGetQry qry) {
		List<Dept> list = deptGateway.list(new Dept());
		List<DeptCO> deptList = deptConvertor.convertClientObjectList(list);
		return Result.of(TreeUtil.buildTreeNode(deptList, DeptCO.class));
	}

}
