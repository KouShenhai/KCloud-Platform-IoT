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

package org.laokou.auth.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

import java.io.Serial;

import static org.laokou.common.i18n.common.DSConstant.BOOT_SYS_LOGIN_LOG;
import static org.laokou.common.mybatisplus.config.DynamicTableNameHandler.PLACE_HOLDER;

/**
 * @author laokou
 */
@Data
@TableName(BOOT_SYS_LOGIN_LOG + PLACE_HOLDER)
@Schema(name = "LoginLogDO", description = "登录日志")
public class LoginLogDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 4289483981365827983L;

	@Schema(name = "username", description = "登录的用户名")
	private String username;

	@Schema(name = "ip", description = "登录的IP地址")
	private String ip;

	@Schema(name = "address", description = "登录的归属地")
	private String address;

	@Schema(name = "browser", description = "登录的浏览器")
	private String browser;

	@Schema(name = "os", description = "登录的操作系统")
	private String os;

	@Schema(name = "status", description = "登录状态 0登录成功 1登录失败")
	private Integer status;

	@Schema(name = "message", description = "登录信息")
	private String message;

	@Schema(name = "type", description = "登录类型")
	private String type;

	@Schema(name = "eventId", description = "事件ID")
	private Long eventId;

}
