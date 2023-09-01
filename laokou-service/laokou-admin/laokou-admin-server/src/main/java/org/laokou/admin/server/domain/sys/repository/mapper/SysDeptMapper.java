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
package org.laokou.admin.server.domain.sys.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.client.vo.SysDeptVO;
import org.laokou.admin.server.domain.sys.entity.SysDeptDO;
import org.laokou.admin.server.interfaces.qo.SysDeptQo;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface SysDeptMapper extends BatchMapper<SysDeptDO> {

	/**
	 * 查询部门列表
	 * @param qo
	 * @return
	 */
	List<SysDeptVO> getDeptList(@Param("qo") SysDeptQo qo);

	/**
	 * 根据id获取部门
	 * @param id
	 * @return
	 */
	SysDeptVO getDept(@Param("id") Long id);

	/**
	 * 根据角色id获取部门ids
	 * @param roleId
	 * @return
	 */
	List<Long> getDeptIdsByRoleId(@Param("roleId") Long roleId);

	/**
	 * 获取版本号
	 * @param id
	 * @return
	 */
	Integer getVersion(@Param("id") Long id);

}
