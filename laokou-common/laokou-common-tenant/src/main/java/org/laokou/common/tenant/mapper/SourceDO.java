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

package org.laokou.common.tenant.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;
import org.laokou.common.tenant.constant.DSConstants;

/**
 * 数据源数据对象.
 *
 * @author laokou
 */
@Data
@TableName(DSConstants.Master.SOURCE_TABLE)
public class SourceDO extends BaseDO {

	/**
	 * 数据源名称.
	 */
	private String name;

	/**
	 * 数据源的驱动名称.
	 */
	private String driverClassName;

	/**
	 * 数据源的连接信息.
	 */
	private String url;

	/**
	 * 数据源的用户名.
	 */
	private String username;

	/**
	 * 数据源的密码.
	 */
	private String password;

}
