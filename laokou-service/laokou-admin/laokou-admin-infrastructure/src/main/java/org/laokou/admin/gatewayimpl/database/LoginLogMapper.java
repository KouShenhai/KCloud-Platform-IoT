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
import org.laokou.admin.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;

/**
 * 登录日志.
 *
 * @author laokou
 */
@Repository
@Mapper
public interface LoginLogMapper extends CrudMapper<Long, Integer, LoginLogDO> {

	/**
	 * 查询登录日志列表.
	 * @param tables 表集合
	 * @param log 登录日志数据模型
	 * @param pageQuery 分页参数
	 * @return 登录日志列表
	 */
	List<LoginLogDO> selectListByCondition(@Param("tables") List<String> tables, @Param("log") LoginLogDO log,
			@Param(PAGE_QUERY) PageQuery pageQuery);

}
