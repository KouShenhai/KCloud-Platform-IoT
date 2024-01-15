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

package org.laokou.admin.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;

/**
 * @author laokou
 */
@Repository
@Mapper
public interface UserMapper extends BatchMapper<UserDO> {

	/**
	 * 修改用户.
	 * @param userDO 用户
	 * @return int
	 */
	Integer updateUser(@Param("userDO") UserDO userDO);

	/**
	 * @param userDO
	 * @param key
	 * @return
	 */
	Integer insertUser(@Param("userDO") UserDO userDO, @Param("key") String key);

	/**
	 * 根据租户ID查询下拉列表.
	 * @return 用户列表
	 */
	List<UserDO> getOptionList(@Param(PAGE_QUERY) PageQuery pageQuery, @Param("key") String key);

	/**
	 * 查询用户列表.
	 * @param user 用户参数
	 * @param pageQuery 分页参数
	 * @return 用户列表
	 */
	List<UserDO> getUserListFilter(@Param("user") UserDO user, @Param(PAGE_QUERY) PageQuery pageQuery,
			@Param("key") String key);

	/**
	 * @param user
	 * @param pageQuery
	 * @param key
	 * @return
	 */
	Integer getUserListTotalFilter(@Param("user") UserDO user, @Param(PAGE_QUERY) PageQuery pageQuery,
			@Param("key") String key);

	/**
	 * @param user
	 * @param key
	 * @return
	 */
	Integer getUserCount(@Param("user") UserDO user, @Param("key") String key);

}
