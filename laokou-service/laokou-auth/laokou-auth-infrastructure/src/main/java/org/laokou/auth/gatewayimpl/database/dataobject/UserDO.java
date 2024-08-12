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

package org.laokou.auth.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.handler.CryptoTypeHandler;
import org.laokou.common.mybatisplus.mapper.BaseDO;

import java.io.Serial;

import static org.laokou.auth.common.constant.Constant.TABLE_USER;

/**
 * 用户.
 *
 * @author laokou
 */
@Data
@TableName(TABLE_USER)
public class UserDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 1181289215379287683L;

	/**
	 * 用户名.
	 */
	@TableField(value = "username", typeHandler = CryptoTypeHandler.class)
	private String username;

	/**
	 * 密码.
	 */
	private String password;

	/**
	 * 超级管理员标识 0否 1是.
	 */
	private Integer superAdmin;

	/**
	 * 头像.
	 */
	private String avatar;

	/**
	 * 邮箱.
	 */
	@TableField(value = "mail", typeHandler = CryptoTypeHandler.class)
	private String mail;

	/**
	 * 用户状态 0启用 1禁用.
	 */
	private Integer status;

	/**
	 * 手机号.
	 */
	@TableField(value = "mobile", typeHandler = CryptoTypeHandler.class)
	private String mobile;

}
