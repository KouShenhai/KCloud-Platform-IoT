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

package org.laokou.generator.info.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;
import java.time.Instant;

/**
 *
 * 代码生成器信息客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "代码生成器信息客户端对象", description = "代码生成器信息客户端对象")
public class InfoCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "数据库名称", description = "数据库名称")
	private String databaseName;

	@Schema(name = "数据库表名称", description = "数据库表名称")
	private String tableName;

	@Schema(name = "作者", description = "作者")
	private String author;

	@Schema(name = "表描述", description = "表描述")
	private String comment;

	@Schema(name = "包名", description = "包名")
	private String packageName;

	@Schema(name = "生成路径", description = "生成路径")
	private String path;

	@Schema(name = "版本号", description = "版本号")
	private String versionNumber;

	@Schema(name = "数据库表前缀", description = "数据库表前缀")
	private String tablePrefix;

	@Schema(name = "模块名称", description = "模块名称")
	private String moduleName;

	@Schema(name = "应用ID", description = "应用ID")
	private String appId;

	@Schema(name = "数据源ID", description = "数据源ID")
	private String sourceId;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createTime;

}
