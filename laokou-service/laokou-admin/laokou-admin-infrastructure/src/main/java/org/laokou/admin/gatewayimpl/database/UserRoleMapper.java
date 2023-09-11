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

package org.laokou.admin.gatewayimpl.database;

import com.baomidou.dynamic.datasource.annotation.Master;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.gatewayimpl.database.dataobject.UserRoleDO;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author laokou
 */
@Repository
@Mapper
@Master
public interface UserRoleMapper extends BatchMapper<UserRoleDO> {

	/**
	 * 根据用户ID查询IDS
	 * @param userId 用户ID
	 * @return List<Long>
	 */
	List<Long> getIdsByUserId(@Param("userId") Long userId);

	List<Long> getRoleIdsByUserId(@Param("userId") Long userId);

	Integer deleteUserRoleByIds(@Param("list") List<Long> list);

}
