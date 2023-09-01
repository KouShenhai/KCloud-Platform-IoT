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
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface SourceMapper extends BatchMapper<SourceDO> {

	// /**
	// * 分页查询多租户数据源
	// * @param qo
	// * @param page
	// * @return
	// */
	// IPage<SysSourceVO> querySourcePage(IPage<SysSourceVO> page, @Param("qo")
	// SysSourceQo qo);
	//
	/**
	 * 根据名词查询数据源
	 * @param name
	 * @return SourceDO
	 */
	SourceDO getSourceByName(@Param("name") String name);

	// /**
	// * 查询数据库源信息
	// * @param sourceName
	// * @return
	// */
	// SysSourceVO querySource(@Param("sourceName") String sourceName);
	//
	// /**
	// * 数据源详情
	// * @param id
	// * @return
	// */
	// SysSourceVO getSourceById(@Param("id") Long id);
	//
	// /**
	// * 获取下拉框
	// * @return
	// */
	// List<OptionVO> getOptionList();

}
