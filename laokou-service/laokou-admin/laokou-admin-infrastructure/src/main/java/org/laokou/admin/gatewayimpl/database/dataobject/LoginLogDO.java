/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.database.dataobject.BaseDO;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@TableName("boot_sys_login_log")
@Schema(name = "LoginLogDO", description = "登录日志")
public class LoginLogDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 4289483981365827983L;

	@Schema(name = "loginName", description = "登录用户")
	private String loginName;

	@Schema(name = "requestIp", description = "请求IP")
	private String requestIp;

	@Schema(name = "requestAddress", description = "请求地址")
	private String requestAddress;

	@Schema(name = "browser", description = "浏览器")
	private String browser;

	@Schema(name = "os", description = "操作系统")
	private String os;

	@Schema(name = "requestStatus", description = "请求状态 0成功 1失败")
	private Integer requestStatus;

	@Schema(name = "msg", description = "提示信息")
	private String msg;

	@Schema(name = "loginType", description = "登录类型")
	private String loginType;

	@Schema(name = "tenantId", description = "租户ID")
	private Long tenantId;

}
