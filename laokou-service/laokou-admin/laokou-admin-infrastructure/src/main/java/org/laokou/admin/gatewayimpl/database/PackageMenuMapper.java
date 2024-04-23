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

package org.laokou.admin.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.gatewayimpl.database.dataobject.PackageMenuDO;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 套餐菜单.
 *
 * @author laokou
 */
@Mapper
@Repository
public interface PackageMenuMapper extends CrudMapper<Long, Integer, PackageMenuDO> {

	/**
	 * 根据套餐ID查看菜单IDS.
	 * @param packageId 套餐ID
	 * @return 菜单IDS
	 */
	List<Long> selectMenuIdsByPackageId(@Param("packageId") Long packageId);

	/**
	 * 根据套餐ID删除套餐菜单.
	 * @param packageId 套餐ID
	 * @return 删除结果
	 */
	Integer deleteByPackageId(@Param("packageId") Long packageId);

}
