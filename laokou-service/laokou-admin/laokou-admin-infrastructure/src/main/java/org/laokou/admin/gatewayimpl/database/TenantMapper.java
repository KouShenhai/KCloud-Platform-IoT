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

import org.apache.ibatis.annotations.Mapper;
import org.laokou.admin.gatewayimpl.database.dataobject.TenantDO;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface TenantMapper extends BatchMapper<TenantDO> {

	/**
	 * 根据租户ID查询下拉列表
	 * @return List<UserDO>
	 */
	List<TenantDO> getOptionList();

	// /**
	// * 查询版本号
	// * @param id
	// * @return
	// */
	// Integer getVersion(@Param("id") Long id);
	//
	// /**
	// * 下拉选择框
	// * @return
	// */
	// List<OptionVO> getOptionList();
	//
	// /**
	// * 分页查询
	// * @param page
	// * @param qo
	// * @return
	// */
	// IPage<SysTenantVO> queryTenantPage(IPage<SysTenantVO> page, @Param("qo")
	// SysTenantQo qo);
	//
	// /**
	// * 获取租户
	// * @param id
	// * @return
	// */
	// SysTenantVO getTenantById(@Param("id") Long id);

}
