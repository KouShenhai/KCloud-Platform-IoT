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

package org.laokou.admin.dto.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.StringUtil;

/**
 * @author laokou
 */
@Data
public class OperateLogListQry extends PageQuery {

	private Integer status;

	@Schema(name = "moduleName", description = "操作的模块名称")
	private String moduleName;

	public void setModuleName(String moduleName) {
		this.moduleName = StringUtil.like(moduleName);
	}

}
