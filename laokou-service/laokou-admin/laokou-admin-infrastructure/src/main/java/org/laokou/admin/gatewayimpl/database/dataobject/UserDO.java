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

package org.laokou.admin.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.repository.BaseDO;
import org.laokou.common.mybatisplus.handler.CryptoTypeHandler;

import java.io.Serial;

import static org.laokou.common.i18n.common.DatasourceConstant.BOOT_SYS_USER;

/**
 * @author laokou
 */
@Data
@TableName(value = BOOT_SYS_USER, autoResultMap = true)
@Schema(name = "UserDO", description = "用户")
public class UserDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 1181289215379287683L;

	@Schema(name = "username", description = "用户名", example = "admin")
	@TableField(value = "username", typeHandler = CryptoTypeHandler.class)
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

	@Schema(name = "status", description = "用户状态 0正常 1锁定", example = "0")
	private Integer status;

	@Schema(name = "mobile", description = "手机号", example = "18974432500")
	@TableField(value = "mobile", typeHandler = CryptoTypeHandler.class)
	private String mobile;

	@Schema(name = "usernamePhrase", description = "用户名短语", example = "xxx")
	private String usernamePhrase;

	@Schema(name = "mailPhrase", description = "邮箱短语", example = "xxx")
	private String mailPhrase;

	@Schema(name = "mobilePhrase", description = "手机号短语", example = "xxx")
	private String mobilePhrase;

}
