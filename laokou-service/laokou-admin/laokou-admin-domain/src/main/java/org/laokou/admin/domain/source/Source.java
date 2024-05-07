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

package org.laokou.admin.domain.source;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.AggregateRoot;

/**
 * @author laokou
 */
@Data
@Schema(name = "Source", description = "数据源")
public class Source extends AggregateRoot<Long> {

	@Schema(name = "name", description = "数据源名称")
	private String name;

	@Schema(name = "driverClassName", description = "数据源的驱动名称")
	private String driverClassName;

	@Schema(name = "username", description = "数据源的用户名")
	private String username;

	@Schema(name = "password", description = "数据源的密码")
	private String password;

	@Schema(name = "url", description = "数据源的连接信息")
	private String url;

	public void checkName() {
		if (!RegexUtil.sourceRegex(name)) {
			throw new SystemException("数据源名称必须包含字母、下划线和数字");
		}
	}

	public void checkName(long count) {
		if (count > 0) {
			throw new SystemException("数据源名称已存在，请重新填写");
		}
	}

}
