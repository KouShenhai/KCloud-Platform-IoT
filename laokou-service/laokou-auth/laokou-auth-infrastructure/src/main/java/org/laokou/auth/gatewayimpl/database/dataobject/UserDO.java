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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;
import org.laokou.common.mybatisplus.handler.CryptoTypeHandler;

import java.io.Serial;

import static org.laokou.common.i18n.common.DSConstant.BOOT_SYS_USER;

/**
 * @author laokou
 */
@Data
@TableName(BOOT_SYS_USER)
@Schema(name = "UserDO", description = "用户")
public class UserDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 1181289215379287683L;

	@Schema(name = "username", description = "用户名", example = "admin")
	private String username;

	@Schema(name = "password", description = "密码", example = "123456")
	private String password;

	@Schema(name = "superAdmin", description = "超级管理员标识 0否 1是", example = "1")
	private Integer superAdmin;

	@Schema(name = "avatar", description = "头像", example = "https://pic.cnblogs.com/avatar/simple_avatar.gif")
	private String avatar;

	@Schema(name = "mail", description = "邮箱", example = "2413176044@qq.com")
	@TableField(value = "mail", typeHandler = CryptoTypeHandler.class)
	private String mail;

	@Schema(name = "status", description = "用户状态 0启用 1禁用", example = "0")
	private Integer status;

	@Schema(name = "mobile", description = "手机号", example = "18974432500")
	@TableField(value = "mobile", typeHandler = CryptoTypeHandler.class)
	private String mobile;

}
