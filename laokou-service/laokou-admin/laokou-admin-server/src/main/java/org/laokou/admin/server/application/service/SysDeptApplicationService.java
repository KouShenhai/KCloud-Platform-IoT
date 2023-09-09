/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.admin.server.application.service;

import org.laokou.admin.dto.SysDeptDTO;
import org.laokou.admin.server.interfaces.qo.SysDeptQo;
import org.laokou.admin.vo.SysDeptVO;
import java.util.*;

/**
 * @author laokou
 */
public interface SysDeptApplicationService {

	/**
	 * 获取部门列表
	 * @return
	 */
	SysDeptVO treeDept();

	/**
	 * 查询部门列表
	 * @param qo
	 * @return
	 */
	List<SysDeptVO> queryDeptList(SysDeptQo qo);

	/**
	 * 新增部门
	 * @param dto
	 * @return
	 */
	Boolean insertDept(SysDeptDTO dto);

	/**
	 * 修改部门
	 * @param dto
	 * @return
	 */
	Boolean updateDept(SysDeptDTO dto);

	/**
	 * 根据id删除部门
	 * @param id
	 * @return
	 */
	Boolean deleteDept(Long id);

	/**
	 * 根据id查询部门
	 * @param id
	 * @return
	 */
	SysDeptVO getDept(Long id);

	/**
	 * 根据角色id获取部门ids
	 * @param roleId
	 * @return
	 */
	List<Long> getDeptIdsByRoleId(Long roleId);

}
