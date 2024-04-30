/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import org.apache.ibatis.session.ResultHandler;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceDO;
import org.laokou.admin.gatewayimpl.database.dataobject.ResourceIndex;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;

/**
 * 资源.
 *
 * @author laokou
 */
@Repository
@Mapper
public interface ResourceMapper extends CrudMapper<Long, Integer, ResourceDO> {

	/**
	 * 查询资源列表.
	 * @param resource 资源数据模型
	 * @param pageQuery 分页参数
	 * @return 资源列表
	 */
	List<ResourceDO> selectListByCondition(@Param("resource") ResourceDO resource,
			@Param(PAGE_QUERY) PageQuery pageQuery);

	long selectCountByCondition(@Param("resource") ResourceDO resource, @Param(PAGE_QUERY) PageQuery pageQuery);

	/**
	 * 处理资源索引.
	 * @param handler 处理器
	 */
	void handleResourceIndex(ResultHandler<ResourceIndex> handler);

	/**
	 * 查询资源时间列表.
	 * @return 资源时间列表
	 */
	List<String> getResourceTime();

}
