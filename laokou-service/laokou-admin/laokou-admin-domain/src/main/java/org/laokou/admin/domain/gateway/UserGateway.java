/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.admin.domain.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.admin.domain.user.User;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;

/**
 * @author laokou
 */
@Schema(name = "UserGateway", description = "用户网关")
public interface UserGateway {

	/**
	 * 新增用户
	 * @param user 用户对象
	 * @return 新增结果
	 */
	Boolean insert(User user);

	/**
	 * 修改用户
	 * @param user 用户对象
	 * @return 修改结果
	 */
	Boolean update(User user);

	/**
	 * 根据ID删除用户
	 * @param id ID
	 * @return 删除结果
	 */
	Boolean deleteById(Long id);

	/**
	 * 重置密码
	 * @param user 用户对象
	 * @return 重置结果
	 */
	Boolean resetPassword(User user);

	/**
	 * 修改用户信息
	 * @param user 用户对象
	 * @return 修改结果
	 */
	Boolean updateInfo(User user);

	/**
	 * 根据ID查看用户
	 * @param id ID
	 * @return 用户
	 */
	User getById(Long id);

	/**
	 * 查询用户列表
	 * @param user 用户对象
	 * @param pageQuery 分页参数
	 * @return 用户列表
	 */
	Datas<User> list(User user, PageQuery pageQuery);

}
