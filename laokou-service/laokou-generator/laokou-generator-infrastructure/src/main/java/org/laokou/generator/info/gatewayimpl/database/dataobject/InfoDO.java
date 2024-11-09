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

package org.laokou.generator.info.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

/**
 *
 * 代码生成器信息数据对象.
 *
 * @author laokou
 */
@Data
@TableName("boot_generator_info")
public class InfoDO extends BaseDO {

	/**
	 * 数据库名称.
	 */
	private String databaseName;

	/**
	 * 数据库表名称.
	 */
	private String tableName;

	/**
	 * 作者.
	 */
	private String author;

	/**
	 * 表描述.
	 */
	private String comment;

	/**
	 * 包名.
	 */
	private String packageName;

	/**
	 * 生成路径.
	 */
	private String path;

	/**
	 * 版本号.
	 */
	private String versionNumber;

	/**
	 * 数据库表前缀.
	 */
	private String tablePrefix;

	/**
	 * 模块名称.
	 */
	private String moduleName;

	/**
	 * 应用ID.
	 */
	private String appId;

	/**
	 * 数据源ID.
	 */
	private String sourceId;

}
