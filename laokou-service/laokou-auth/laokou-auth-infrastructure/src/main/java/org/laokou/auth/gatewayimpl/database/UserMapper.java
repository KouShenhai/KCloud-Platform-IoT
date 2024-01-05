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

package org.laokou.auth.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.auth.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface UserMapper extends BatchMapper<UserDO> {

	/**
	 * 根据用户名和租户ID查询用户
	 * @param username 用户名
	 * @param type 登录类型
	 * @return UserDO
	 */
	UserDO getUserByUsername(@Param("username") String username, @Param("type") String type, @Param("key")String key);

}
