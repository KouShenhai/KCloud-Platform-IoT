/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.user.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

/**
 * 用户数据对象.
 *
 * @author laokou
 */
@Data
@TableName("boot_sys_user")
public class UserDO extends BaseDO {

	/**
	 * 密码.
	 */
	private String password;

	/**
	 * 超级管理员标识 0否 1是.
	 */
	private Integer superAdmin;

	/**
	 * 邮箱.
	 */
	private String mail;

	/**
	 * 手机号.
	 */
	private String mobile;

	/**
	 * 用户状态 0启用 1禁用.
	 */
	private Integer status;

	/**
	 * 头像.
	 */
	private String avatar;

	/**
	 * 用户名短语.
	 */
	private String usernamePhrase;

	/**
	 * 邮箱短语.
	 */
	private String mailPhrase;

	/**
	 * 手机号短语.
	 */
	private String mobilePhrase;

	/**
	 * 用户名.
	 */
	private String username;

}
