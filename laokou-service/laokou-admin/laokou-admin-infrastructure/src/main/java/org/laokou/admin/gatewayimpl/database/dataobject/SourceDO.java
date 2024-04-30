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

package org.laokou.admin.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@TableName("boot_sys_source")
@Schema(name = "SourceDO", description = "数据源")
public class SourceDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 7616743906900137371L;

	@Schema(name = "name", description = "数据源名称")
	private String name;

	@Schema(name = "driverClassName", description = "数据源的驱动名称")
	private String driverClassName;

	@Schema(name = "url", description = "数据源的连接信息")
	private String url;

	@Schema(name = "username", description = "数据源的用户名")
	private String username;

	@Schema(name = "password", description = "数据源的密码")
	private String password;

	public SourceDO(String name) {
		this.name = name;
	}

}
