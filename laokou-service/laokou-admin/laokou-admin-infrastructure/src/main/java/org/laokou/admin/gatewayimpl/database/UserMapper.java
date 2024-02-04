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
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.repository.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;

/**
 * 用户.
 *
 * @author laokou
 */
@Repository
@Mapper
public interface UserMapper extends CrudMapper<Long,Integer,UserDO> {

	/**
	 * 新增用户.
	 * @param userDO 用户对象
	 * @param key 密钥
	 * @return 新增结果
	 */
	Integer insertObj(@Param("userDO") UserDO userDO, @Param("key") String key);

	/**
	 * 查询用户下拉框选择项列表.
	 * @param key 密钥
	 * @param pageQuery 分页参数
	 * @return 用户下拉框选择项列表
	 */
	List<UserDO> getOptionList(@Param(PAGE_QUERY) PageQuery pageQuery, @Param("key") String key);

	/**
	 * 查询用户列表.
	 * @param user 用户对象
	 * @param pageQuery 分页参数
	 * @param key 密钥
	 * @return 用户列表
	 */
	List<UserDO> getUserListFilter(@Param("user") UserDO user, @Param(PAGE_QUERY) PageQuery pageQuery,
			@Param("key") String key);

	/**
	 * 查看用户总数.
	 * @param user 用户对象
	 * @param pageQuery 分页参数
	 * @param key 密钥
	 * @return 用户总数
	 */
	Integer getUserListTotalFilter(@Param("user") UserDO user, @Param(PAGE_QUERY) PageQuery pageQuery,
			@Param("key") String key);

	/**
	 * 判断用户名重复.
	 * @param user 用户对象
	 * @param key 密钥
	 * @return 判断结果
	 */
	Integer getUserCount(@Param("user") UserDO user, @Param("key") String key);

	long selectUsernameCount(@Param("username") String username, @Param("key") String key);

}
