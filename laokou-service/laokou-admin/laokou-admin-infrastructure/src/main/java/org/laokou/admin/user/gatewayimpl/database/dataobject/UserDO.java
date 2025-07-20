/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import static org.laokou.common.tenant.constant.DSConstants.Master.USER_TABLE;

/**
 * 用户数据对象.
 *
 * @author laokou
 */
@Data
@TableName(USER_TABLE)
public class UserDO extends BaseDO {

	/**
	 * 用户密码.
	 */
	private String password;

	/**
	 * 超级管理员标识 0否 1是.
	 */
	private Integer superAdmin;

	/**
	 * 用户邮箱.
	 */
	private String mail;

	/**
	 * 用户手机号.
	 */
	private String mobile;

	/**
	 * 用户状态 0启用 1禁用.
	 */
	private Integer status;

	/**
	 * 用户头像.
	 */
	private Long avatar;

	/**
	 * 用户名短语.
	 */
	private String usernamePhrase;

	/**
	 * 用户邮箱短语.
	 */
	private String mailPhrase;

	/**
	 * 用户手机号短语.
	 */
	private String mobilePhrase;

	/**
	 * 用户名.
	 */
	private String username;

}
